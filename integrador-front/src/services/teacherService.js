import api from '../config/api'

export async function getTeachers() {
  const { data } = await api.get('/api/teachers')
  return data
}

export async function createTeacher(teacher) {
  const { data } = await api.post('/api/teachers', toBody(teacher))
  return data
}

export async function updateTeacher(id, teacher) {
  const { data } = await api.put(`/api/teachers/${id}`, toBody(teacher))
  return data
}

export async function deleteTeacher(id) {
  await api.delete(`/api/teachers/${id}`)
}

function toBody(teacher) {
  return {
    code: teacher.code,
    documentNumber: teacher.documentNumber,
    firstName: teacher.firstName,
    lastName: teacher.lastName,
    status: teacher.status,
  }
}
