import { Low } from 'lowdb'
import { JSONFile } from 'lowdb/node'
import { App } from '@tinyhttp/app'
import { cors } from '@tinyhttp/cors'

// ── Load DB ───────────────────────────────────────────────────────────────────
const adapter = new JSONFile('db.json')
const db = new Low(adapter, { streamings: [], consumo_mensal: [] })
await db.read()

// ── App ───────────────────────────────────────────────��───────────────────────
const app = new App()

app.use(cors())

// ── GET /api/v1/streamings/search?nome=<query> ────────────────────────────────
app.get('/api/v1/streamings/search', (req, res) => {
  const query = (req.query['nome'] ?? '').toString().toLowerCase().trim()
  const all = db.data.streamings ?? []
  const results = query ? all.filter(s => s.nome.toLowerCase().includes(query)) : all
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
  const nome     = (req.query['nome']   ?? '').toString().toLowerCase().trim()
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
  const totalDias = new Date(year, month, 0).getDate()  // day 0 of next month = last day of current

  const consumos = db.data.consumo_mensal ?? []

  // Look for an exact record: nome (case-insensitive) + mes_referencia
  // username "*" means "shared / any user" — acts as a wildcard seed
  const registro = consumos.find(c =>
    c.nome === nome &&
    c.mes_referencia === anomes &&
    (c.username === '*' || c.username === username)
  )

  if (registro) {
    return res.json({
      id:               registro.streaming_id,
      username:         username === '*' ? username : username,
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

// ── GET /api/v1/streamings — lista todos ──────────────────────────────────────
app.get('/api/v1/streamings', (_req, res) => {
  res.json(db.data.streamings ?? [])
})

// ── Start ─────────────────────────────────────────────────────────────────────
const PORT = 3000
const HOST = '0.0.0.0'

app.listen(PORT, () => {
  console.log('\n  Mock API rodando!\n')
  console.log(`  GET http://localhost:${PORT}/api/v1/streamings`)
  console.log(`  GET http://localhost:${PORT}/api/v1/streamings/search?nome=%s`)
  console.log(`  GET http://localhost:${PORT}/api/v1/streamings/:username/consumo_mensal?nome=%s&anomes=%s\n`)
}, HOST)
