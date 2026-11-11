import api from '../config/api'

// Asistencia de una materia del grupo; con "date" (2026-10-20) solo esa clase.
export async function getAttendances(groupSubjectId, date) {
  const { data } = await api.get('/api/attendances', {
    params: { groupSubjectId, ...(date ? { date } : {}) },
  })
  return data
}

// Resumen por estudiante; sin groupSubjectId resume todas las materias.
export async function getAttendanceSummary(groupSubjectId) {
  const { data } = await api.get('/api/attendances/summary', {
    params: groupSubjectId ? { groupSubjectId } : {},
  })
  return data
}

// Guarda la lista de una clase completa. Si ya habia registros de esa fecha, los actualiza.
export async function registerAttendanceSession({ groupSubjectId, classDate, records }) {
  const { data } = await api.post('/api/attendances/sessions', {
    groupSubjectId: Number(groupSubjectId),
    classDate,
    records: records.map((record) => ({
      studentId: Number(record.studentId),
      status: record.status,
      observation: record.observation?.trim() || null,
    })),
  })
  return data
}

export async function deleteAttendance(id) {
  await api.delete(`/api/attendances/${id}`)
}
