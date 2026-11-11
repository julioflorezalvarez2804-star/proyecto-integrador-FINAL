import api from '../config/api'

export async function getSubjects() {
  const { data } = await api.get('/api/subjects')
  return data
}

export async function createSubject(subject) {
  const { data } = await api.post('/api/subjects', toBody(subject))
  return data
}

export async function updateSubject(id, subject) {
  const { data } = await api.put(`/api/subjects/${id}`, toBody(subject))
  return data
}

export async function deleteSubject(id) {
  await api.delete(`/api/subjects/${id}`)
}

function toBody(subject) {
  return {
    code: subject.code,
    name: subject.name,
    credits: Number(subject.credits),
    programId: Number(subject.programId),
  }
}
