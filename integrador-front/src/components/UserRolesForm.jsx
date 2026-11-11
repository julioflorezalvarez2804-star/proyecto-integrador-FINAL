import { useEffect, useState } from 'react'
import Modal from './Modal'
import PrimaryButton from './PrimaryButton'
import { getErrorMessage } from '../config/api'

function UserRolesForm({ open, user, roles, onSubmit, onCancel }) {
  const [selected, setSelected] = useState([])
  const [error, setError] = useState('')
  const [saving, setSaving] = useState(false)

  useEffect(() => {
    if (open && user) {
      setSelected(user.roles)
      setError('')
    }
  }, [open, user])

  function toggle(name) {
    setSelected((current) => (current.includes(name) ? current.filter((n) => n !== name) : [...current, name]))
  }

  async function handleSubmit(e) {
    e.preventDefault()
    setSaving(true)
    try {
      await onSubmit(selected)
    } catch (err) {
      setError(getErrorMessage(err, 'No se pudieron guardar los roles.'))
    } finally {
      setSaving(false)
    }
  }

  return (
    <Modal open={open} title={user ? `Roles de ${user.username}` : 'Roles'} onClose={onCancel}>
      <form onSubmit={handleSubmit} className="flex flex-col gap-3">
        {roles.length === 0 ? (
          <p className="text-sm text-ink/70">Todavía no hay roles creados.</p>
        ) : (
          <fieldset className="flex flex-col gap-2">
            <legend className="sr-only">Roles</legend>
            {roles.map((role) => (
              <label key={role.roleId} className="flex items-center gap-3 rounded-md border border-line px-3 py-2 text-sm">
                <input
                  type="checkbox"
                  checked={selected.includes(role.name)}
                  onChange={() => toggle(role.name)}
                  className="h-4 w-4 accent-pine"
                />
                {role.name}
              </label>
            ))}
          </fieldset>
        )}
        <p className="text-xs leading-relaxed text-ink/70">
          ADMIN puede activar cuentas y asignar roles. PROFESOR es el rol que reciben las cuentas nuevas.
        </p>

        {error && <p className="text-sm text-rose">{error}</p>}

        <div className="mt-3 flex justify-end gap-2">
          <PrimaryButton variant="secondary" onClick={onCancel}>
            Cancelar
          </PrimaryButton>
          <PrimaryButton type="submit" disabled={saving || roles.length === 0}>
            {saving ? 'Guardando...' : 'Guardar'}
          </PrimaryButton>
        </div>
      </form>
    </Modal>
  )
}

export default UserRolesForm
