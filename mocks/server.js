import { Low } from 'lowdb'
import { JSONFile } from 'lowdb/node'
import { App } from '@tinyhttp/app'
import { cors } from '@tinyhttp/cors'

// ── Load DB ───────────────────────────────────────────────────────────────────
const adapter = new JSONFile('db.json')
const db = new Low(adapter, { streamings: [], consumo_mensal: [], pagamentos: [], notificacoes: [] })
await db.read()

// ── App ───────────────────────────────────────────────────────────────────────
const app = new App()

app.use(cors())

// ── Helpers ───────────────────────────────────────────────────────────────────
/**
 * Normaliza nomes de serviço para comparação tolerante.
 * Estratégia: remove TUDO que não seja letra ou dígito, converte para lowercase.
 * "Claro TV+"  → "clarotv"
 * "claro tv+"  → "clarotv"
 * "claro%20tv%2B" chega decodificado como "claro tv+" → "clarotv"
 * "Disney+"    → "disney"
 * "Amazon Prime Video" → "amazonprimevideo"
 * Isso garante match independente de espaços, pontuação, +, -, etc.
 */
function normalizarNome(nome) {
  return nome
    .toLowerCase()
    .replace(/[^a-z0-9]/g, '') // mantém só letras e dígitos
}

// ── GET /api/v1/streamings/search?nome=<query> ────────────────────────────────
app.get('/api/v1/streamings/search', (req, res) => {
  const query = normalizarNome((req.query['nome'] ?? '').toString())
  const all = db.data.streamings ?? []
  const results = query
    ? all.filter(s => normalizarNome(s.nome).includes(query))
    : all
  res.json(results)
})

// ── GET /api/v1/streamings/:username/consumo_mensal ───────────────────────────
// Query params:
//   nome    — nome do serviço (case-insensitive)
//   anomes  — referência no formato YYYY-MM  (ex: 2026-05)
//
// Retorno quando o serviço é conhecido no db:
//   { id, username, mes_referencia, total_dias_no_mes, dias_utilizados, total_minutos_mes }
//
// Fallback para serviços não cadastrados em db.json:
//   dias_utilizados = 3  (≤ 10% do mês → "pouco usada")
//   total_minutos_mes = 60
// ─────────────────────────────────────────────────────────────────────────────
app.get('/api/v1/streamings/:username/consumo_mensal', (req, res) => {
  const username = (req.params['username'] ?? '').toString().toLowerCase().trim()
  const nome     = normalizarNome((req.query['nome']   ?? '').toString())
  const anomes   = (req.query['anomes'] ?? '').toString().trim()

  // Validate required params
  if (!nome) {
    return res.status(400).json({ error: 'Parâmetro "nome" é obrigatório.' })
  }
  if (!anomes || !/^\d{4}-\d{2}$/.test(anomes)) {
    return res.status(400).json({ error: 'Parâmetro "anomes" inválido. Use o formato YYYY-MM.' })
  }

  // Calculate total days in the requested month
  const [year, month] = anomes.split('-').map(Number)
  const totalDias = new Date(year, month, 0).getDate()

  const consumos = db.data.consumo_mensal ?? []

  // Compara usando nomes normalizados para tolerar "tv+", "tv plus", "%2B" etc.
  const registro = consumos.find(c =>
    normalizarNome(c.nome) === nome &&
    c.mes_referencia === anomes &&
    (c.username === '*' || c.username === username)
  )

  if (registro) {
    return res.json({
      id:               registro.streaming_id,
      username:         username === '*' ? username : username,
      streaming:        {
        id: registro.streaming_id,
        nome: registro.nome,
      },
      mes_referencia:   registro.mes_referencia,
      total_dias_no_mes: registro.total_dias_no_mes,
      dias_utilizados:  registro.dias_utilizados,
      total_minutos_mes: registro.total_minutos_mes
    })
  }

  // ── Fallback: serviço não cadastrado → retorna "pouco usada" ─────────────
  // dias_utilizados ≤ 3 garante score < 10% → pouco usada no app Android
  return res.json({
    id:               null,
    username:         username,
    mes_referencia:   anomes,
    total_dias_no_mes: totalDias,
    dias_utilizados:  3,
    total_minutos_mes: 60
  })
})

// ── GET /api/v1/streamings/pagamentos?nome=<nome> ──────────────────────────────
app.get('/api/v1/streamings/pagamentos', (req, res) => {
  const nomeservico = (req.query['nomeservico'] ?? '').toString().toLowerCase().trim()
  const pagamentos = db.data.pagamentos ?? []

  // Filter by service name (case-insensitive)
  const results = pagamentos.filter(p => p.nomeservico.toLowerCase().includes(nomeservico))
  res.json(results)
})

// ── GET /api/v1/streamings — lista todos ──────────────────────────────────────
app.get('/api/v1/streamings', (_req, res) => {
  res.json(db.data.streamings ?? [])
})

// ── GET /api/v1/notificacoes/:username ────────────────────────────────────────
// Retorna todas as notificações do usuário, ordenadas por criadaEm DESC
app.get('/api/v1/notificacoes/:username', (req, res) => {
  const username = (req.params['username'] ?? '').toString().toLowerCase().trim()
  const todas = db.data.notificacoes ?? []
  const resultado = todas
    .filter(n => n.username === username)
    .sort((a, b) => new Date(b.criadaEm) - new Date(a.criadaEm))
  res.json(resultado)
})

// ── PATCH /api/v1/notificacoes/:id/lida ──────────────────────────────────────
// Marca uma notificação como lida
app.patch('/api/v1/notificacoes/:id/lida', async (req, res) => {
  const id = parseInt(req.params['id'], 10)
  const notif = (db.data.notificacoes ?? []).find(n => n.id === id)
  if (!notif) return res.status(404).json({ error: 'Notificação não encontrada.' })
  notif.lida = true
  await db.write()
  res.json(notif)
})

// ── PATCH /api/v1/notificacoes/:username/ler-todas ───────────────────────────
// Marca todas as notificações do usuário como lidas
app.patch('/api/v1/notificacoes/:username/ler-todas', async (req, res) => {
  const username = (req.params['username'] ?? '').toString().toLowerCase().trim()
  const todas = db.data.notificacoes ?? []
  todas.filter(n => n.username === username).forEach(n => { n.lida = true })
  await db.write()
  res.json({ updated: todas.filter(n => n.username === username).length })
})

// ── Start ─────────────────────────────────────────────────────────────────────
const PORT = 3000
const HOST = '0.0.0.0'

app.listen(PORT, () => {
  console.log('\n  Mock API rodando!\n')
  console.log(`  GET   http://localhost:${PORT}/api/v1/streamings`)
  console.log(`  GET   http://localhost:${PORT}/api/v1/streamings/search?nome=%s`)
  console.log(`  GET   http://localhost:${PORT}/api/v1/streamings/:username/consumo_mensal?nome=%s&anomes=%s`)
  console.log(`  GET   http://localhost:${PORT}/api/v1/notificacoes/:username`)
  console.log(`  GET http://localhost:${PORT}/api/v1/streamings/pagamentos?nome=%s\n`)
  console.log(`  PATCH http://localhost:${PORT}/api/v1/notificacoes/:id/lida`)
  console.log(`  PATCH http://localhost:${PORT}/api/v1/notificacoes/:username/ler-todas\n`)
}, HOST)
