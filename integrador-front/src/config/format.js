// Convierte "2026-10-17" (o "2026-10-17T15:36:03") en "17/10/2026".
export function formatDate(value) {
  if (!value) return '—'
  const [year, month, day] = String(value).slice(0, 10).split('-')
  return `${day}/${month}/${year}`
}

// Fecha de hoy en formato "2026-10-28" con la hora LOCAL (toISOString usaria UTC y de noche
// en Colombia daria el dia siguiente).
export function todayISO() {
  const now = new Date()
  const month = String(now.getMonth() + 1).padStart(2, '0')
  const day = String(now.getDate()).padStart(2, '0')
  return `${now.getFullYear()}-${month}-${day}`
}

// 100 -> "100%", 66.7 -> "66.7%"
export function formatRate(rate) {
  const value = Number(rate)
  return `${Number.isInteger(value) ? value : value.toFixed(1)}%`
}
