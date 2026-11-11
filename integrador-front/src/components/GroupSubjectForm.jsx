import { useEffect, useState } from 'react'
import Modal from './Modal'
import Field, { inputClass } from './Field'
import PrimaryButton from './PrimaryButton'
import { getErrorMessage } from '../config/api'

function GroupSubjectForm({ open, groups, subjects, teachers, onSubmit, onCancel }) {
  const [groupId, setGroupId] = useState('')
  const [subjectId, setSubjectId] = useState('')
  const [teacherId, setTeacherId] = useState('')
  const [error, setError] = useState('')
  const [saving, setSaving] = useState(false)

  useEffect(() => {
    if (open) {
      setGroupId('')
      setSubjectId('')
      setTeacherId('')
      setError('')
    }
  }, [open])

  // El backend exige que la materia sea del mismo programa del grupo,
  // asi que solo se ofrecen las materias de ese programa.
  const group = groups.find((g) => String(g.groupId) === groupId)
  const availableSubjects = group ? subjects.filter((s) => s.programId === group.programId) : []

  function handleGroupChange(value) {
    setGroupId(value)
    setSubjectId('')
  }

  async function handleSubmit(e) {
    e.preventDefault()
    if (!groupId || !subjectId || !teacherId) {
      setError('Selecciona un grupo, una materia y un profesor.')
      return
    }
    setSaving(true)
    try {
      await onSubmit({ groupId, subjectId, teacherId })
    } catch (err) {
      setError(getErrorMessage(err, 'No se pudo asignar la materia al grupo.'))
    } finally {
      setSaving(false)
    }
  }

  const missingData = groups.length === 0 || subjects.length === 0 || teachers.length === 0

  return (
    <Modal open={open} title="Asignar materia a un grupo" onClose={onCancel}>
      <form onSubmit={handleSubmit} className="flex flex-col gap-3">
        {missingData && (
          <p className="rounded-md bg-straw/20 px-3 py-2 text-sm text-ink/80">
            Necesitas al menos un grupo, una materia y un profesor.
          </p>
        )}
        <Field label="Grupo">
          <select value={groupId} onChange={(e) => handleGroupChange(e.target.value)} className={inputClass}>
            <option value="">Selecciona un grupo</option>
            {groups.map((g) => (
              <option key={g.groupId} value={g.groupId}>
                {g.code} - {g.shift}
              </option>
            ))}
          </select>
        </Field>
        <Field label="Materia">
          <select
            value={subjectId}
            onChange={(e) => setSubjectId(e.target.value)}
            disabled={!group}
            className={`${inputClass} disabled:bg-mist/60`}
          >
            <option value="">{group ? 'Selecciona una materia' : 'Primero elige un grupo'}</option>
            {availableSubjects.map((s) => (
              <option key={s.subjectId} value={s.subjectId}>
                {s.code} - {s.name}
              </option>
            ))}
          </select>
        </Field>
        {group && availableSubjects.length === 0 && (
          <p className="text-sm text-ink/70">El programa de este grupo todavía no tiene materias.</p>
        )}
        <Field label="Profesor">
          <select value={teacherId} onChange={(e) => setTeacherId(e.target.value)} className={inputClass}>
            <option value="">Selecciona un profesor</option>
            {teachers.map((t) => (
              <option key={t.userID} value={t.userID}>
                {t.code} - {t.firstName} {t.lastName}
              </option>
            ))}
          </select>
        </Field>

        {error && <p className="text-sm text-rose">{error}</p>}

        <div className="mt-3 flex justify-end gap-2">
          <PrimaryButton variant="secondary" onClick={onCancel}>
            Cancelar
          </PrimaryButton>
          <PrimaryButton type="submit" disabled={saving}>
            {saving ? 'Guardando...' : 'Asignar'}
          </PrimaryButton>
        </div>
      </form>
    </Modal>
  )
}

export default GroupSubjectForm
