import { useEffect, useMemo, useState } from 'react'
import { getSubjects, createSubject, updateSubject, deleteSubject } from '../services/subjectService'
import { getPrograms } from '../services/programService'
import { getErrorMessage } from '../config/api'
import PageTitle from '../components/PageTitle'
import PrimaryButton from '../components/PrimaryButton'
import SearchBox from '../components/SearchBox'
import EmptyState from '../components/EmptyState'
import TableShell from '../components/TableShell'
import RowActions from '../components/RowActions'
import SubjectForm from '../components/SubjectForm'
import ConfirmDialog from '../components/ConfirmDialog'

function Subjects() {
  const [subjects, setSubjects] = useState([])
  const [programs, setPrograms] = useState([])
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
      const [subjectsData, programsData] = await Promise.all([getSubjects(), getPrograms()])
      setSubjects(subjectsData)
      setPrograms(programsData)
      setError('')
    } catch (err) {
      setError(getErrorMessage(err, 'No se pudieron cargar las materias.'))
    } finally {
      setLoading(false)
    }
  }

  useEffect(() => {
    loadData()
  }, [])

  const rows = useMemo(
    () =>
      subjects.map((subject) => ({
        ...subject,
        programName: programs.find((p) => p.programId === subject.programId)?.name ?? '—',
      })),
    [subjects, programs],
  )

  const filtered = useMemo(() => {
    const term = search.trim().toLowerCase()
    if (!term) return rows
    return rows.filter((r) =>
      [r.code, r.name, r.programName].some((field) => String(field ?? '').toLowerCase().includes(term)),
    )
  }, [rows, search])

  function openCreate() {
    setEditing(null)
    setFormOpen(true)
  }

  function openEdit(subject) {
    setEditing(subject)
    setFormOpen(true)
  }

  function closeForm() {
    setFormOpen(false)
    setEditing(null)
  }

  async function handleSubmit(form) {
    if (editing) {
      await updateSubject(editing.subjectId, form)
    } else {
      await createSubject(form)
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
      await deleteSubject(deleteTarget.subjectId)
      closeDelete()
      await loadData()
    } catch (err) {
      setDeleteError(getErrorMessage(err, 'No se pudo eliminar la materia.'))
    }
  }

  return (
    <div>
      <PageTitle
        title="Materias"
        subtitle="Materias de cada programa, con sus créditos."
        action={
          <PrimaryButton onClick={openCreate} disabled={loading}>
            Nueva materia
          </PrimaryButton>
        }
      />

      <SearchBox value={search} onChange={setSearch} placeholder="Buscar por código, nombre o programa" />

      {loading ? (
        <p className="text-ink/70">Cargando materias...</p>
      ) : error ? (
        <p className="text-sm text-rose">{error}</p>
      ) : filtered.length === 0 ? (
        <EmptyState
          message={search ? 'No hay materias que coincidan con la búsqueda.' : 'Aún no hay materias. Necesitas un programa para crear la primera.'}
        />
      ) : (
        <TableShell headers={['Código', 'Nombre', 'Créditos', 'Programa', 'Acciones']}>
          {filtered.map((subject) => (
            <tr key={subject.subjectId} className="border-t border-line hover:bg-tint/5">
              <td className="px-4 py-3 font-medium">{subject.code}</td>
              <td className="px-4 py-3">{subject.name}</td>
              <td className="px-4 py-3">{subject.credits}</td>
              <td className="px-4 py-3">{subject.programName}</td>
              <RowActions onEdit={() => openEdit(subject)} onDelete={() => setDeleteTarget(subject)} />
            </tr>
          ))}
        </TableShell>
      )}

      <SubjectForm open={formOpen} initialData={editing} programs={programs} onSubmit={handleSubmit} onCancel={closeForm} />

      <ConfirmDialog
        open={!!deleteTarget}
        title="Eliminar materia"
        message={`¿Eliminar la materia ${deleteTarget?.name ?? ''}? Esta acción no se puede deshacer.`}
        error={deleteError}
        onConfirm={confirmDelete}
        onCancel={closeDelete}
      />
    </div>
  )
}

export default Subjects
