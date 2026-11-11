import api from '../config/api'

export async function getGrades() {
  const { data } = await api.get('/api/grades')
  return data
}

export async function createGrade({ groupSubjectId, studentId, finalScore, observation }) {
  const { data } = await api.post('/api/grades', {
    groupSubjectId: Number(groupSubjectId),
    studentId: Number(studentId),
    finalScore: Number(finalScore),
    observation: observation?.trim() || null,
  })
  return data
}

// Al editar solo cambian la nota y la observacion.
export async function updateGrade(id, { finalScore, observation }) {
  const { data } = await api.put(`/api/grades/${id}`, {
    finalScore: Number(finalScore),
    observation: observation?.trim() || null,
  })
  return data
}

export async function deleteGrade(id) {
  await api.delete(`/api/grades/${id}`)
}
