import { useEffect, useMemo, useState } from 'react'
import { getGrades, createGrade, updateGrade, deleteGrade } from '../services/gradeService'
import { getGroupSubjects } from '../services/groupSubjectService'
import { getGroups } from '../services/groupService'
import { getSubjects } from '../services/subjectService'
import { getStudents } from '../services/studentService'
import { getEnrollments } from '../services/enrollmentService'
import { getErrorMessage } from '../config/api'
import PageTitle from '../components/PageTitle'
import PrimaryButton from '../components/PrimaryButton'
import SearchBox from '../components/SearchBox'
import EmptyState from '../components/EmptyState'
import TableShell from '../components/TableShell'
import RowActions from '../components/RowActions'
import GradeForm from '../components/GradeForm'
import ConfirmDialog from '../components/ConfirmDialog'

const PASSING_SCORE = 3.0

function Grades() {
  const [grades, setGrades] = useState([])
  const [groupSubjects, setGroupSubjects] = useState([])
  const [groups, setGroups] = useState([])
  const [subjects, setSubjects] = useState([])
  const [students, setStudents] = useState([])
  const [enrollments, setEnrollments] = useState([])
  const [loading, setLoading] = useState(true)
  const [error, setError] = useState('')
  const [search, setSearch] = useState('')

  const [formOpen, setFormOpen] = useState(false)
  const [editing, setEditing] = useState(null)
  const [deleteTarget, setDeleteTarget] = useState(null)
  const [deleteError, setDeleteError] = useState('')

  async function loadData() {
    setLoading(true)
    try {
      const [gradesData, gsData, groupsData, subjectsData, studentsData, enrollmentsData] = await Promise.all([
        getGrades(),
        getGroupSubjects(),
        getGroups(),
        getSubjects(),
        getStudents(),
        getEnrollments(),
      ])
      setGrades(gradesData)
      setGroupSubjects(gsData)
      setGroups(groupsData)
      setSubjects(subjectsData)
      setStudents(studentsData)
      setEnrollments(enrollmentsData)
      setError('')
    } catch (err) {
      setError(getErrorMessage(err, 'No se pudieron cargar las notas.'))
    } finally {
      setLoading(false)
    }
  }

  useEffect(() => {
    loadData()
  }, [])

  // Cada materia de un grupo se muestra como "G1A · MAT-101 Logica de programacion".
  const groupSubjectOptions = useMemo(
    () =>
      groupSubjects.map((gs) => {
        const group = groups.find((g) => g.groupId === gs.groupId)
        const subject = subjects.find((s) => s.subjectId === gs.subjectId)
        return {
          ...gs,
          label: `${group?.code ?? '?'} · ${subject ? `${subject.code} ${subject.name}` : '?'}`,
        }
      }),
    [groupSubjects, groups, subjects],
  )

  const rows = useMemo(
    () =>
      grades.map((grade) => {
        const student = students.find((s) => s.userID === grade.studentId)
        const option = groupSubjectOptions.find((gs) => gs.groupSubjectId === grade.groupSubjectId)
        return {
          ...grade,
          studentName: student ? `${student.firstName} ${student.lastName}` : '—',
          subjectLabel: option?.label ?? '—',
        }
      }),
    [grades, students, groupSubjectOptions],
  )

  const filtered = useMemo(() => {
    const term = search.trim().toLowerCase()
    if (!term) return rows
    return rows.filter((r) =>
      [r.studentName, r.subjectLabel, r.observation].some((field) => String(field ?? '').toLowerCase().includes(term)),
    )
  }, [rows, search])

  function openCreate() {
    setEditing(null)
    setFormOpen(true)
  }

  function openEdit(row) {
    setEditing(row)
    setFormOpen(true)
  }

  function closeForm() {
    setFormOpen(false)
    setEditing(null)
  }

  async function handleSubmit(form) {
    if (editing) {
      await updateGrade(editing.gradeId, form)
    } else {
      await createGrade(form)
    }
    closeForm()
    await loadData()
  }

  function closeDelete() {
    setDeleteTarget(null)
    setDeleteError('')
  }

  async function confirmDelete() {
    try {
      await deleteGrade(deleteTarget.gradeId)
      closeDelete()
      await loadData()
    } catch (err) {
      setDeleteError(getErrorMessage(err, 'No se pudo eliminar la nota.'))
    }
  }

  return (
    <div>
      <PageTitle
        title="Notas"
        subtitle="Nota final de cada estudiante en cada materia. Se aprueba con 3.0 o más."
        action={
          <PrimaryButton onClick={openCreate} disabled={loading}>
            Nueva nota
          </PrimaryButton>
        }
      />

      <SearchBox value={search} onChange={setSearch} placeholder="Buscar por estudiante, materia u observación" />

      {loading ? (
        <p className="text-ink/70">Cargando notas...</p>
      ) : error ? (
        <p className="text-sm text-rose">{error}</p>
      ) : filtered.length === 0 ? (
        <EmptyState
          message={search ? 'No hay notas que coincidan con la búsqueda.' : 'Aún no hay notas. Para registrar una, el estudiante debe estar matriculado en el grupo de la materia.'}
        />
      ) : (
        <TableShell headers={['Estudiante', 'Materia', 'Nota', 'Observación', 'Acciones']}>
          {filtered.map((row) => {
            const passed = row.finalScore >= PASSING_SCORE
            return (
              <tr key={row.gradeId} className="border-t border-line hover:bg-tint/5">
                <td className="px-4 py-3 font-medium">{row.studentName}</td>
                <td className="px-4 py-3">{row.subjectLabel}</td>
                <td className="whitespace-nowrap px-4 py-3">
                  <span className="mr-2 font-medium tabular-nums">{Number(row.finalScore).toFixed(1)}</span>
                  <span
                    className={`rounded-full px-2 py-1 text-xs font-medium ${
                      passed ? 'bg-pine/15 text-pine-dark' : 'bg-rose/15 text-rose-dark'
                    }`}
                  >
                    {passed ? 'Aprobada' : 'Reprobada'}
                  </span>
                </td>
                <td className="max-w-xs truncate px-4 py-3 text-ink/70" title={row.observation ?? ''}>
                  {row.observation || '—'}
                </td>
                <RowActions onEdit={() => openEdit(row)} onDelete={() => setDeleteTarget(row)} />
              </tr>
            )
          })}
        </TableShell>
      )}

      <GradeForm
        open={formOpen}
        initialData={editing}
        groupSubjectOptions={groupSubjectOptions}
        students={students}
        enrollments={enrollments}
        onSubmit={handleSubmit}
        onCancel={closeForm}
      />

      <ConfirmDialog
        open={!!deleteTarget}
        title="Eliminar nota"
        message={`¿Eliminar la nota de ${deleteTarget?.studentName ?? ''} en ${deleteTarget?.subjectLabel ?? ''}?`}
        error={deleteError}
        onConfirm={confirmDelete}
        onCancel={closeDelete}
      />
    </div>
  )
}

export default Grades
