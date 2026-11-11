import { useEffect, useMemo, useState } from 'react'
import Modal from './Modal'
import Field, { inputClass } from './Field'
import PrimaryButton from './PrimaryButton'
import { getErrorMessage } from '../config/api'

// Al crear: se elige la materia del grupo y un estudiante matriculado en ese grupo.
// Al editar: el estudiante y la materia no cambian; solo la nota y la observacion.
function GradeForm({ open, initialData, groupSubjectOptions, students, enrollments, onSubmit, onCancel }) {
  const editing = !!initialData

  const [groupSubjectId, setGroupSubjectId] = useState('')
  const [studentId, setStudentId] = useState('')
  const [finalScore, setFinalScore] = useState('')
  const [observation, setObservation] = useState('')
  const [error, setError] = useState('')
  const [saving, setSaving] = useState(false)

  useEffect(() => {
    if (!open) return
    if (initialData) {
      setGroupSubjectId(String(initialData.groupSubjectId))
      setStudentId(String(initialData.studentId))
      setFinalScore(String(initialData.finalScore))
      setObservation(initialData.observation ?? '')
    } else {
      setGroupSubjectId('')
      setStudentId('')
      setFinalScore('')
      setObservation('')
    }
    setError('')
  }, [initialData, open])

  const selected = groupSubjectOptions.find((gs) => String(gs.groupSubjectId) === groupSubjectId)

  // El backend solo deja calificar a quien tiene matricula activa o completada en el grupo.
  const eligibleStudents = useMemo(() => {
    if (!selected) return []
    const enrolledIds = new Set(
      enrollments
        .filter((e) => e.groupId === selected.groupId && (e.status === 'ACTIVE' || e.status === 'COMPLETED'))
        .map((e) => e.studentId),
    )
    return students.filter((s) => enrolledIds.has(s.userID))
  }, [selected, enrollments, students])

  function handleGroupSubjectChange(value) {
    setGroupSubjectId(value)
    setStudentId('')
  }

  async function handleSubmit(e) {
    e.preventDefault()
    if (!editing && (!groupSubjectId || !studentId)) {
      setError('Selecciona la materia del grupo y el estudiante.')
      return
    }
    const score = Number(finalScore)
    if (finalScore === '' || Number.isNaN(score) || score < 0 || score > 5) {
      setError('La nota debe estar entre 0.0 y 5.0.')
      return
    }
    setSaving(true)
    try {
      await onSubmit({ groupSubjectId, studentId, finalScore, observation })
    } catch (err) {
      setError(getErrorMessage(err, 'No se pudo guardar la nota.'))
    } finally {
      setSaving(false)
    }
  }

  return (
    <Modal open={open} title={editing ? 'Editar nota' : 'Nueva nota'} onClose={onCancel}>
      <form onSubmit={handleSubmit} className="flex flex-col gap-3" noValidate>
        {editing ? (
          <div className="rounded-md bg-mist/60 px-3 py-2 text-sm">
            <p className="font-medium">{initialData.studentName}</p>
            <p className="text-ink/70">{initialData.subjectLabel}</p>
          </div>
        ) : (
          <>
            {groupSubjectOptions.length === 0 && (
              <p className="rounded-md bg-straw/20 px-3 py-2 text-sm text-ink/80">
                Primero asigna una materia a un grupo en la sección Materias por grupo.
              </p>
            )}
            <Field label="Materia del grupo">
              <select
                value={groupSubjectId}
                onChange={(e) => handleGroupSubjectChange(e.target.value)}
                className={inputClass}
              >
                <option value="">Selecciona una materia</option>
                {groupSubjectOptions.map((gs) => (
                  <option key={gs.groupSubjectId} value={gs.groupSubjectId}>
                    {gs.label}
                  </option>
                ))}
              </select>
            </Field>
            <Field label="Estudiante">
              <select
                value={studentId}
                onChange={(e) => setStudentId(e.target.value)}
                disabled={!selected}
                className={`${inputClass} disabled:bg-mist/60`}
              >
                <option value="">{selected ? 'Selecciona un estudiante' : 'Primero elige la materia'}</option>
                {eligibleStudents.map((s) => (
                  <option key={s.userID} value={s.userID}>
                    {s.code} - {s.firstName} {s.lastName}
                  </option>
                ))}
              </select>
            </Field>
            {selected && eligibleStudents.length === 0 && (
              <p className="text-sm text-ink/70">Ese grupo no tiene estudiantes con matrícula activa o completada.</p>
            )}
          </>
        )}

        <Field label="Nota (0.0 a 5.0)">
          <input
            type="number"
            min="0"
            max="5"
            step="0.1"
            value={finalScore}
            onChange={(e) => setFinalScore(e.target.value)}
            className={inputClass}
          />
        </Field>
        <Field label="Observación (opcional)">
          <textarea
            rows={3}
            maxLength={255}
            value={observation}
            onChange={(e) => setObservation(e.target.value)}
            className={inputClass}
          />
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

export default GradeForm
