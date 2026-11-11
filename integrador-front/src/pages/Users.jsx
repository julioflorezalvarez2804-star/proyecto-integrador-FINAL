import { useEffect, useMemo, useState } from 'react'
import { deleteUser, getUsersWithRoles, setUserRoles, updateUserStatus } from '../services/userService'
import { getUser } from '../config/auth'
import { getErrorMessage } from '../config/api'
import PageTitle from '../components/PageTitle'
import SearchBox from '../components/SearchBox'
import EmptyState from '../components/EmptyState'
import TableShell from '../components/TableShell'
import UserRolesForm from '../components/UserRolesForm'
import ConfirmDialog from '../components/ConfirmDialog'

function Users() {
  const [users, setUsers] = useState([])
  const [roles, setRoles] = useState([])
  const [loading, setLoading] = useState(true)
  const [error, setError] = useState('')
  const [actionError, setActionError] = useState('')
  const [search, setSearch] = useState('')

  const [rolesTarget, setRolesTarget] = useState(null)
  const [deleteTarget, setDeleteTarget] = useState(null)
  const [deleteError, setDeleteError] = useState('')

  const currentUser = getUser()

  async function loadData() {
    setLoading(true)
    try {
      const data = await getUsersWithRoles()
      setUsers(data.users)
      setRoles(data.roles)
      setError('')
    } catch (err) {
      setError(getErrorMessage(err, 'No se pudieron cargar los usuarios.'))
    } finally {
      setLoading(false)
    }
  }

  useEffect(() => {
    loadData()
  }, [])

  const pending = users.filter((user) => user.status !== 'ACTIVE').length

  const filtered = useMemo(() => {
    const term = search.trim().toLowerCase()
    if (!term) return users
    return users.filter((user) =>
      [user.username, user.email, ...user.roles].some((field) => String(field ?? '').toLowerCase().includes(term)),
    )
  }, [users, search])

  async function handleStatus(user, status) {
    setActionError('')
    try {
      await updateUserStatus(user, status)
      await loadData()
    } catch (err) {
      setActionError(getErrorMessage(err, 'No se pudo cambiar el estado de la cuenta.'))
    }
  }

  async function handleRoles(selectedNames) {
    await setUserRoles(rolesTarget, selectedNames, roles)
    setRolesTarget(null)
    await loadData()
  }

  function closeDelete() {
    setDeleteTarget(null)
    setDeleteError('')
  }

  async function confirmDelete() {
    try {
      await deleteUser(deleteTarget)
      closeDelete()
      await loadData()
    } catch (err) {
      setDeleteError(getErrorMessage(err, 'No se pudo eliminar la cuenta.'))
    }
  }

  return (
    <div>
      <PageTitle title="Usuarios" subtitle="Cuentas del sistema. Activa las cuentas nuevas y asigna roles." />

      {pending > 0 && (
        <p className="mb-4 rounded-md bg-straw/20 px-3 py-2 text-sm text-ink/80" role="status">
          {pending === 1 ? '1 cuenta espera activación.' : `${pending} cuentas esperan activación.`}
        </p>
      )}

      <SearchBox value={search} onChange={setSearch} placeholder="Buscar por usuario, correo o rol" />

      {actionError && <p className="mb-3 text-sm text-rose">{actionError}</p>}

      {loading ? (
        <p className="text-ink/70">Cargando usuarios...</p>
      ) : error ? (
        <p className="text-sm text-rose">{error}</p>
      ) : filtered.length === 0 ? (
        <EmptyState message="No hay usuarios que coincidan con la búsqueda." />
      ) : (
        <TableShell headers={['Usuario', 'Correo', 'Estado', 'Roles', 'Acciones']}>
          {filtered.map((user) => {
            const isSelf = user.username === currentUser?.username
            const isBuiltIn = user.userId === 0
            const active = user.status === 'ACTIVE'

            return (
              <tr key={user.userId} className="border-t border-line hover:bg-tint/5">
                <td className="px-4 py-3 font-medium">{user.username}</td>
                <td className="px-4 py-3">{user.email}</td>
                <td className="px-4 py-3">
                  <span
                    className={`rounded-full px-2 py-1 text-xs font-medium ${
                      active ? 'bg-pine/15 text-pine-dark' : 'bg-straw/30 text-ink'
                    }`}
                  >
                    {active ? 'Activo' : 'Inactivo'}
                  </span>
                </td>
                <td className="px-4 py-3">
                  {user.roles.length === 0 ? (
                    <span className="text-ink/70">Sin roles</span>
                  ) : (
                    <span className="flex flex-wrap gap-1.5">
                      {user.roles.map((role) => (
                        <span key={role} className="rounded-md bg-mist px-2 py-0.5 text-xs font-medium text-ink/80">
                          {role}
                        </span>
                      ))}
                    </span>
                  )}
                </td>
                <td className="space-x-3 whitespace-nowrap px-4 py-3 text-right">
                  {isSelf || isBuiltIn ? (
                    <span className="text-xs text-ink/70">{isSelf ? 'Tu cuenta' : 'Cuenta de demostración'}</span>
                  ) : (
                    <>
                      <button
                        onClick={() => handleStatus(user, active ? 'INACTIVE' : 'ACTIVE')}
                        className={`text-sm font-medium hover:underline ${active ? 'text-harbor-dark' : 'text-pine'}`}
                      >
                        {active ? 'Desactivar' : 'Activar'}
                      </button>
                      <button onClick={() => setRolesTarget(user)} className="text-sm font-medium text-pine hover:underline">
                        Roles
                      </button>
                      <button onClick={() => setDeleteTarget(user)} className="text-sm font-medium text-rose hover:underline">
                        Eliminar
                      </button>
                    </>
                  )}
                </td>
              </tr>
            )
          })}
        </TableShell>
      )}

      <UserRolesForm
        open={!!rolesTarget}
        user={rolesTarget}
        roles={roles}
        onSubmit={handleRoles}
        onCancel={() => setRolesTarget(null)}
      />

      <ConfirmDialog
        open={!!deleteTarget}
        title="Eliminar cuenta"
        message={`¿Eliminar la cuenta de ${deleteTarget?.username ?? ''}? Esta acción no se puede deshacer.`}
        error={deleteError}
        onConfirm={confirmDelete}
        onCancel={closeDelete}
      />
    </div>
  )
}

export default Users
