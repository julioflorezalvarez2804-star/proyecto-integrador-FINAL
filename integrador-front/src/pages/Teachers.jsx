import { useEffect, useMemo, useState } from 'react'
import { getTeachers, createTeacher, updateTeacher, deleteTeacher } from '../services/teacherService'
import { getErrorMessage } from '../config/api'
import PageTitle from '../components/PageTitle'
import PrimaryButton from '../components/PrimaryButton'
import SearchBox from '../components/SearchBox'
import EmptyState from '../components/EmptyState'
import TableShell from '../components/TableShell'
import RowActions from '../components/RowActions'
import TeacherForm from '../components/TeacherForm'
import ConfirmDialog from '../components/ConfirmDialog'

function Teachers() {
  const [teachers, setTeachers] = useState([])
  const [loading, setLoading] = useState(true)
  const [error, setError] = useState('')
  const [search, setSearch] = useState('')

  const [formOpen, setFormOpen] = useState(false)
  const [editing, setEditing] = useState(null)
  const [deleteTarget, setDeleteTarget] = useState(null)
  const [deleteError, setDeleteError] = useState('')

  async function loadTeachers() {
    setLoading(true)
    try {
      setTeachers(await getTeachers())
      setError('')
    } catch (err) {
      setError(getErrorMessage(err, 'No se pudieron cargar los profesores.'))
    } finally {
      setLoading(false)
    }
  }

  useEffect(() => {
    loadTeachers()
  }, [])

  const filtered = useMemo(() => {
    const term = search.trim().toLowerCase()
    if (!term) return teachers
    return teachers.filter((t) =>
      [t.code, t.documentNumber, t.firstName, t.lastName].some((field) =>
        String(field ?? '').toLowerCase().includes(term),
      ),
    )
  }, [teachers, search])

  function openCreate() {
    setEditing(null)
    setFormOpen(true)
  }

  function openEdit(teacher) {
    setEditing(teacher)
    setFormOpen(true)
  }

  function closeForm() {
    setFormOpen(false)
    setEditing(null)
  }

  async function handleSubmit(form) {
    if (editing) {
      await updateTeacher(editing.userID, form)
    } else {
      await createTeacher(form)
    }
    closeForm()
    await loadTeachers()
  }

  function closeDelete() {
    setDeleteTarget(null)
    setDeleteError('')
  }

  async function confirmDelete() {
    try {
      await deleteTeacher(deleteTarget.userID)
      closeDelete()
      await loadTeachers()
    } catch (err) {
      setDeleteError(getErrorMessage(err, 'No se pudo eliminar el profesor.'))
    }
  }

  return (
    <div>
      <PageTitle
        title="Profesores"
        subtitle="Docentes de la institución."
        action={<PrimaryButton onClick={openCreate}>Nuevo profesor</PrimaryButton>}
      />

      <SearchBox value={search} onChange={setSearch} placeholder="Buscar por nombre, código o documento" />

      {loading ? (
        <p className="text-ink/70">Cargando profesores...</p>
      ) : error ? (
        <p className="text-sm text-rose">{error}</p>
      ) : filtered.length === 0 ? (
        <EmptyState
          message={search ? 'No hay profesores que coincidan con la búsqueda.' : 'Aún no hay profesores. Crea el primero con el botón de arriba.'}
        />
      ) : (
        <TableShell headers={['Código', 'Documento', 'Nombre', 'Estado', 'Acciones']}>
          {filtered.map((teacher) => (
            <tr key={teacher.userID} className="border-t border-line hover:bg-tint/5">
              <td className="px-4 py-3 font-medium">{teacher.code}</td>
              <td className="px-4 py-3">{teacher.documentNumber}</td>
              <td className="px-4 py-3">
                {teacher.firstName} {teacher.lastName}
              </td>
              <td className="px-4 py-3">
                <span
                  className={`rounded-full px-2 py-1 text-xs font-medium ${
                    teacher.status ? 'bg-pine/15 text-pine-dark' : 'bg-mist text-ink/70'
                  }`}
                >
                  {teacher.status ? 'Activo' : 'Inactivo'}
                </span>
              </td>
              <RowActions onEdit={() => openEdit(teacher)} onDelete={() => setDeleteTarget(teacher)} />
            </tr>
          ))}
        </TableShell>
      )}

      <TeacherForm open={formOpen} initialData={editing} onSubmit={handleSubmit} onCancel={closeForm} />

      <ConfirmDialog
        open={!!deleteTarget}
        title="Eliminar profesor"
        message={`¿Eliminar a ${deleteTarget?.firstName ?? ''} ${deleteTarget?.lastName ?? ''}? Esta acción no se puede deshacer.`}
        error={deleteError}
        onConfirm={confirmDelete}
        onCancel={closeDelete}
      />
    </div>
  )
}

export default Teachers
