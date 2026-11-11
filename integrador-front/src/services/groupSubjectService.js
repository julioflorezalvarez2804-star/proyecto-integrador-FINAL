import api from '../config/api'

// Una "materia por grupo" une un grupo, una materia y el profesor que la dicta.
// El backend no permite editarla: se crea o se elimina.
export async function getGroupSubjects() {
  const { data } = await api.get('/api/group-subjects')
  return data
}

export async function createGroupSubject({ groupId, subjectId, teacherId }) {
  const { data } = await api.post('/api/group-subjects', {
    groupId: Number(groupId),
    subjectId: Number(subjectId),
    teacherId: Number(teacherId),
  })
  return data
}

export async function deleteGroupSubject(id) {
  await api.delete(`/api/group-subjects/${id}`)
}
