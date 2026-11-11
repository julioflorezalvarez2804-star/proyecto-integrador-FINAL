import { useEffect, useState } from 'react'
import Modal from './Modal'
import Field, { inputClass } from './Field'
import PrimaryButton from './PrimaryButton'
import { getErrorMessage } from '../config/api'

const emptyForm = { code: '', name: '', credits: '', programId: '' }

function SubjectForm({ open, initialData, programs, onSubmit, onCancel }) {
  const [form, setForm] = useState(emptyForm)
  const [error, setError] = useState('')
  const [saving, setSaving] = useState(false)

  useEffect(() => {
    if (initialData) {
      setForm({
        code: initialData.code ?? '',
        name: initialData.name ?? '',
        credits: String(initialData.credits ?? ''),
        programId: String(initialData.programId ?? ''),
      })
    } else {
      setForm(emptyForm)
    }
    setError('')
  }, [initialData, open])

  function handleChange(e) {
    setForm({ ...form, [e.target.name]: e.target.value })
  }

  async function handleSubmit(e) {
    e.preventDefault()
    if (!form.code || !form.name || !form.programId) {
      setError('Código, nombre y programa son obligatorios.')
      return
    }
    if (!Number.isInteger(Number(form.credits)) || Number(form.credits) < 1) {
      setError('Los créditos deben ser un número entero de 1 o más.')
      return
    }
    setSaving(true)
    try {
      await onSubmit(form)
    } catch (err) {
      setError(getErrorMessage(err, 'No se pudo guardar la materia.'))
    } finally {
      setSaving(false)
    }
  }

  return (
    <Modal open={open} title={initialData ? 'Editar materia' : 'Nueva materia'} onClose={onCancel}>
      <form onSubmit={handleSubmit} className="flex flex-col gap-3" noValidate>
        {programs.length === 0 && (
          <p className="rounded-md bg-straw/20 px-3 py-2 text-sm text-ink/80">
            Para crear una materia primero necesitas al menos un programa.
          </p>
        )}
        <div className="grid grid-cols-[1fr_6rem] gap-3">
          <Field label="Código">
            <input name="code" value={form.code} onChange={handleChange} className={inputClass} />
          </Field>
          <Field label="Créditos">
            <input type="number" min="1" step="1" name="credits" value={form.credits} onChange={handleChange} className={inputClass} />
          </Field>
        </div>
        <Field label="Nombre">
          <input name="name" value={form.name} onChange={handleChange} className={inputClass} />
        </Field>
        <Field label="Programa">
          <select name="programId" value={form.programId} onChange={handleChange} className={inputClass}>
            <option value="">Selecciona un programa</option>
            {programs.map((program) => (
              <option key={program.programId} value={program.programId}>
                {program.code} - {program.name}
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
            {saving ? 'Guardando...' : 'Guardar'}
          </PrimaryButton>
        </div>
      </form>
    </Modal>
  )
}

export default SubjectForm
