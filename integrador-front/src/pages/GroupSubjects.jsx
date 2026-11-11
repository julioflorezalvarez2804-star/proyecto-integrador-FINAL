import { useEffect, useMemo, useState } from 'react'
import { getGroupSubjects, createGroupSubject, deleteGroupSubject } from '../services/groupSubjectService'
import { getGroups } from '../services/groupService'
import { getSubjects } from '../services/subjectService'
import { getTeachers } from '../services/teacherService'
import { getErrorMessage } from '../config/api'
import PageTitle from '../components/PageTitle'
import PrimaryButton from '../components/PrimaryButton'
import SearchBox from '../components/SearchBox'
import EmptyState from '../components/EmptyState'
import TableShell from '../components/TableShell'
import GroupSubjectForm from '../components/GroupSubjectForm'
import ConfirmDialog from '../components/ConfirmDialog'

function GroupSubjects() {
  const [groupSubjects, setGroupSubjects] = useState([])
  const [groups, setGroups] = useState([])
  const [subjects, setSubjects] = useState([])
  const [teachers, setTeachers] = useState([])
  const [loading, setLoading] = useState(true)
  const [error, setError] = useState('')
  const [search, setSearch] = useState('')

  const [formOpen, setFormOpen] = useState(false)
  const [deleteTarget, setDeleteTarget] = useState(null)
  const [deleteError, setDeleteError] = useState('')

  async function loadData() {
    setLoading(true)
    try {
      const [gsData, groupsData, subjectsData, teachersData] = await Promise.all([
        getGroupSubjects(),
        getGroups(),
        getSubjects(),
        getTeachers(),
      ])
      setGroupSubjects(gsData)
      setGroups(groupsData)
      setSubjects(subjectsData)
      setTeachers(teachersData)
      setError('')
    } catch (err) {
      setError(getErrorMessage(err, 'No se pudieron cargar las materias por grupo.'))
    } finally {
      setLoading(false)
    }
  }

  useEffect(() => {
    loadData()
  }, [])

  // El backend solo devuelve ids; aqui se buscan los nombres para mostrarlos.
  const rows = useMemo(
    () =>
      groupSubjects.map((gs) => {
        const group = groups.find((g) => g.groupId === gs.groupId)
        const subject = subjects.find((s) => s.subjectId === gs.subjectId)
        const teacher = teachers.find((t) => t.userID === gs.teacherId)
        return {
          ...gs,
          groupLabel: group ? `${group.code} - ${group.shift}` : '—',
          subjectLabel: subject ? `${subject.code} - ${subject.name}` : '—',
          teacherName: teacher ? `${teacher.firstName} ${teacher.lastName}` : '—',
        }
      }),
    [groupSubjects, groups, subjects, teachers],
  )

  const filtered = useMemo(() => {
    const term = search.trim().toLowerCase()
    if (!term) return rows
    return rows.filter((r) =>
      [r.groupLabel, r.subjectLabel, r.teacherName].some((field) => field.toLowerCase().includes(term)),
    )
  }, [rows, search])

  async function handleSubmit(form) {
    await createGroupSubject(form)
    setFormOpen(false)
    await loadData()
  }

  function closeDelete() {
    setDeleteTarget(null)
    setDeleteError('')
  }

  async function confirmDelete() {
    try {
      await deleteGroupSubject(deleteTarget.groupSubjectId)
      closeDelete()
      await loadData()
    } catch (err) {
      setDeleteError(getErrorMessage(err, 'No se pudo eliminar la asignación.'))
    }
  }

  return (
    <div>
      <PageTitle
        title="Materias por grupo"
        subtitle="Qué materia se dicta en cada grupo y quién la dicta."
        action={
          <PrimaryButton onClick={() => setFormOpen(true)} disabled={loading}>
            Asignar materia
          </PrimaryButton>
        }
      />

      <SearchBox value={search} onChange={setSearch} placeholder="Buscar por grupo, materia o profesor" />

      {loading ? (
        <p className="text-ink/70">Cargando...</p>
      ) : error ? (
        <p className="text-sm text-rose">{error}</p>
      ) : filtered.length === 0 ? (
        <EmptyState
          message={search ? 'No hay resultados para la búsqueda.' : 'Aún no hay materias asignadas. Necesitas un grupo, una materia y un profesor.'}
        />
      ) : (
        <TableShell headers={['Grupo', 'Materia', 'Profesor', 'Acciones']}>
          {filtered.map((row) => (
            <tr key={row.groupSubjectId} className="border-t border-line hover:bg-tint/5">
              <td className="px-4 py-3 font-medium">{row.groupLabel}</td>
              <td className="px-4 py-3">{row.subjectLabel}</td>
              <td className="px-4 py-3">{row.teacherName}</td>
              <td className="whitespace-nowrap px-4 py-3 text-right">
                <button onClick={() => setDeleteTarget(row)} className="text-sm font-medium text-rose hover:underline">
                  Eliminar
                </button>
              </td>
            </tr>
          ))}
        </TableShell>
      )}

      <GroupSubjectForm
        open={formOpen}
        groups={groups}
        subjects={subjects}
        teachers={teachers}
        onSubmit={handleSubmit}
        onCancel={() => setFormOpen(false)}
      />

      <ConfirmDialog
        open={!!deleteTarget}
        title="Eliminar asignación"
        message={`¿Quitar ${deleteTarget?.subjectLabel ?? 'la materia'} del grupo ${deleteTarget?.groupLabel ?? ''}?`}
        error={deleteError}
        onConfirm={confirmDelete}
        onCancel={closeDelete}
      />
    </div>
  )
}

export default GroupSubjects
