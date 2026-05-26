-- Seed de assinaturas para SQLite usando os streamings de mocks/db.json.
-- Regra aplicada:
--   - ativa = 1  => dataInicio preenchida e dataFim = NULL
--   - ativa = 0  => dataInicio e dataFim preenchidas

BEGIN TRANSACTION;

DELETE FROM assinaturas;
DELETE FROM sqlite_sequence WHERE name = 'assinaturas';

INSERT INTO assinaturas (
  nomeServico,
  valor,
  modalidade,
  diaVencimento,
  categoria,
  urlServico,
  ativa,
  dataInicio,
  dataFim
)
VALUES
  (
    'Amazon Prime Video',
    19.90,
    'MENSAL',
    5,
    'STREAMING',
    'https://upload.wikimedia.org/wikipedia/commons/thumb/1/11/Amazon_Prime_Video_logo.svg/250px-Amazon_Prime_Video_logo.svg.png',
    1,
    CAST(julianday('2025-01-10') - julianday('1970-01-01') AS INTEGER),
    NULL
  ),
  (
    'Netflix',
    55.90,
    'MENSAL',
    10,
    'STREAMING',
    'https://upload.wikimedia.org/wikipedia/commons/thumb/0/08/Netflix_2015_logo.svg/250px-Netflix_2015_logo.svg.png',
    1,
    CAST(julianday('2024-11-15') - julianday('1970-01-01') AS INTEGER),
    NULL
  ),
  (
    'Disney+',
    43.90,
    'MENSAL',
    8,
    'STREAMING',
    'https://upload.wikimedia.org/wikipedia/commons/thumb/3/3e/Disney%2B_logo.svg/250px-Disney%2B_logo.svg.png',
    1,
    CAST(julianday('2025-02-20') - julianday('1970-01-01') AS INTEGER),
    NULL
  ),
  (
    'HBO Max',
    34.90,
    'MENSAL',
    12,
    'STREAMING',
    'https://upload.wikimedia.org/wikipedia/commons/thumb/1/1f/Max_logo.svg/250px-Max_logo.svg.png',
    0,
    CAST(julianday('2024-06-01') - julianday('1970-01-01') AS INTEGER),
    CAST(julianday('2025-01-31') - julianday('1970-01-01') AS INTEGER)
  ),
  (
    'Globoplay',
    27.90,
    'MENSAL',
    14,
    'STREAMING',
    'https://upload.wikimedia.org/wikipedia/commons/thumb/4/4b/Globoplay_logo.svg/250px-Globoplay_logo.svg.png',
    1,
    CAST(julianday('2025-03-03') - julianday('1970-01-01') AS INTEGER),
    NULL
  ),
  (
    'Apple TV+',
    21.90,
    'MENSAL',
    20,
    'STREAMING',
    'https://upload.wikimedia.org/wikipedia/commons/thumb/2/28/Apple_TV_Plus_Logo.svg/250px-Apple_TV_Plus_Logo.svg.png',
    0,
    CAST(julianday('2024-08-10') - julianday('1970-01-01') AS INTEGER),
    CAST(julianday('2025-02-28') - julianday('1970-01-01') AS INTEGER)
  ),
  (
    'Paramount+',
    19.90,
    'MENSAL',
    22,
    'STREAMING',
    'https://upload.wikimedia.org/wikipedia/commons/thumb/a/a5/Paramount_Plus_logo.svg/250px-Paramount_Plus_logo.svg.png',
    0,
    CAST(julianday('2024-05-05') - julianday('1970-01-01') AS INTEGER),
    CAST(julianday('2024-12-31') - julianday('1970-01-01') AS INTEGER)
  ),
  (
    'Crunchyroll',
    14.99,
    'MENSAL',
    3,
    'STREAMING',
    'https://upload.wikimedia.org/wikipedia/commons/thumb/0/08/Crunchyroll_logo.svg/250px-Crunchyroll_logo.svg.png',
    1,
    CAST(julianday('2025-04-01') - julianday('1970-01-01') AS INTEGER),
    NULL
  ),
  (
    'Looke',
    16.90,
    'MENSAL',
    18,
    'STREAMING',
    'https://play-lh.googleusercontent.com/1JBgHxrM7HnmJ7B8KW_4oo7A2M8Vo3Pj3DjGLXTFfj4bCbYH3Zi-FTbAjVjhJBApg=w240-h480-rw',
    0,
    CAST(julianday('2024-02-12') - julianday('1970-01-01') AS INTEGER),
    CAST(julianday('2024-09-30') - julianday('1970-01-01') AS INTEGER)
  ),
  (
    'Mubi',
    34.90,
    'MENSAL',
    25,
    'STREAMING',
    'https://upload.wikimedia.org/wikipedia/commons/thumb/b/b8/MUBI_logo.svg/250px-MUBI_logo.svg.png',
    1,
    CAST(julianday('2025-01-25') - julianday('1970-01-01') AS INTEGER),
    NULL
  ),
  (
    'Spotify',
    21.90,
    'MENSAL',
    7,
    'MUSICA',
    'https://upload.wikimedia.org/wikipedia/commons/thumb/1/19/Spotify_logo_without_text.svg/250px-Spotify_logo_without_text.svg.png',
    1,
    CAST(julianday('2024-10-01') - julianday('1970-01-01') AS INTEGER),
    NULL
  ),
  (
    'Deezer',
    24.90,
    'MENSAL',
    9,
    'MUSICA',
    'https://upload.wikimedia.org/wikipedia/commons/thumb/4/4a/Deezer_logo.svg/250px-Deezer_logo.svg.png',
    0,
    CAST(julianday('2024-04-20') - julianday('1970-01-01') AS INTEGER),
    CAST(julianday('2025-03-15') - julianday('1970-01-01') AS INTEGER)
  ),
  (
    'Amazon Music',
    19.90,
    'MENSAL',
    6,
    'MUSICA',
    'https://upload.wikimedia.org/wikipedia/commons/thumb/9/9b/Amazon_Music_logo.svg/250px-Amazon_Music_logo.svg.png',
    1,
    CAST(julianday('2025-02-10') - julianday('1970-01-01') AS INTEGER),
    NULL
  ),
  (
    'YouTube Music',
    20.90,
    'MENSAL',
    13,
    'MUSICA',
    'https://upload.wikimedia.org/wikipedia/commons/thumb/6/6a/Youtube_Music_icon.svg/250px-Youtube_Music_icon.svg.png',
    1,
    CAST(julianday('2024-12-05') - julianday('1970-01-01') AS INTEGER),
    NULL
  ),
  (
    'Smart Fit',
    99.90,
    'MENSAL',
    15,
    'SAUDE',
    'https://upload.wikimedia.org/wikipedia/commons/thumb/1/1d/Smart_Fit_logo.svg/250px-Smart_Fit_logo.svg.png',
    1,
    CAST(julianday('2025-01-03') - julianday('1970-01-01') AS INTEGER),
    NULL
  ),
  (
    'Bluefit',
    119.90,
    'MENSAL',
    16,
    'SAUDE',
    'https://upload.wikimedia.org/wikipedia/commons/thumb/6/66/Bluefit_logo.png/250px-Bluefit_logo.png',
    0,
    CAST(julianday('2024-03-11') - julianday('1970-01-01') AS INTEGER),
    CAST(julianday('2025-02-10') - julianday('1970-01-01') AS INTEGER)
  ),
  (
    'Gympass',
    89.90,
    'MENSAL',
    11,
    'SAUDE',
    'https://upload.wikimedia.org/wikipedia/commons/thumb/3/34/Gympass_logo.svg/250px-Gympass_logo.svg.png',
    1,
    CAST(julianday('2025-03-08') - julianday('1970-01-01') AS INTEGER),
    NULL
  ),
  (
    'TotalPass',
    79.90,
    'MENSAL',
    17,
    'SAUDE',
    'https://seeklogo.com/images/T/totalpass-logo-FA0FB9D3F8-seeklogo.com.png',
    0,
    CAST(julianday('2024-07-01') - julianday('1970-01-01') AS INTEGER),
    CAST(julianday('2025-01-20') - julianday('1970-01-01') AS INTEGER)
  ),
  (
    'SKY',
    109.90,
    'MENSAL',
    21,
    'STREAMING',
    'https://upload.wikimedia.org/wikipedia/commons/thumb/5/57/Sky_Group_logo_2020.svg/250px-Sky_Group_logo_2020.svg.png',
    1,
    CAST(julianday('2024-09-09') - julianday('1970-01-01') AS INTEGER),
    NULL
  ),
  (
    'Claro tv+',
    69.90,
    'MENSAL',
    23,
    'STREAMING',
    'https://upload.wikimedia.org/wikipedia/commons/thumb/a/a1/Claro_logo.svg/250px-Claro_logo.svg.png',
    1,
    CAST(julianday('2025-01-15') - julianday('1970-01-01') AS INTEGER),
    NULL
  ),
  (
    'Vivo Play',
    59.90,
    'MENSAL',
    24,
    'STREAMING',
    'https://upload.wikimedia.org/wikipedia/commons/thumb/4/44/Vivo_logo_2020.svg/250px-Vivo_logo_2020.svg.png',
    1,
    CAST(julianday('2025-02-01') - julianday('1970-01-01') AS INTEGER),
    NULL
  ),
  (
    'Oi TV',
    49.90,
    'MENSAL',
    26,
    'STREAMING',
    'https://upload.wikimedia.org/wikipedia/commons/thumb/8/8b/Oi_logo_2016.svg/250px-Oi_logo_2016.svg.png',
    0,
    CAST(julianday('2024-01-01') - julianday('1970-01-01') AS INTEGER),
    CAST(julianday('2024-11-30') - julianday('1970-01-01') AS INTEGER)
  );

COMMIT;

