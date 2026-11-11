import { useEffect, useMemo, useState } from 'react'
import { getGroupSubjects } from '../services/groupSubjectService'
import { getGroups } from '../services/groupService'
import { getSubjects } from '../services/subjectService'
import { getStudents } from '../services/studentService'
import { getEnrollments } from '../services/enrollmentService'
import { getAttendances, getAttendanceSummary, registerAttendanceSession } from '../services/attendanceService'
import { getErrorMessage } from '../config/api'
import { formatRate, todayISO } from '../config/format'
import PageTitle from '../components/PageTitle'
import PrimaryButton from '../components/PrimaryButton'
import EmptyState from '../components/EmptyState'
import TableShell from '../components/TableShell'
import Field, { inputClass } from '../components/Field'
import AttendanceStatusPicker from '../components/AttendanceStatusPicker'

const ATTENDANCE_GOAL = 80

const statusMeta = {
  PRESENT: { label: 'Presentes', chip: 'bg-pine/15 text-pine-dark' },
  ABSENT: { label: 'Ausentes', chip: 'bg-rose/15 text-rose-dark' },
  LATE: { label: 'Tardes', chip: 'bg-straw/30 text-straw-dark' },
  EXCUSED: { label: 'Excusados', chip: 'bg-harbor/20 text-harbor-dark' },
}

function rateBadge(rate) {
  if (rate >= ATTENDANCE_GOAL) return 'bg-pine/15 text-pine-dark'
  if (rate >= 60) return 'bg-straw/30 text-straw-dark'
  return 'bg-rose/15 text-rose-dark'
}

function Attendance() {
  const [groupSubjects, setGroupSubjects] = useState([])
  const [groups, setGroups] = useState([])
  const [subjects, setSubjects] = useState([])
  const [students, setStudents] = useState([])
  const [enrollments, setEnrollments] = useState([])
  const [loading, setLoading] = useState(true)
  const [error, setError] = useState('')

  const [tab, setTab] = useState('registrar')
  const [groupSubjectId, setGroupSubjectId] = useState('')
  const [date, setDate] = useState(todayISO())
  const [entries, setEntries] = useState({}) // studentId -> { status, observation }
  const [existingCount, setExistingCount] = useState(0)
  const [loadingSession, setLoadingSession] = useState(false)
  const [saving, setSaving] = useState(false)
  const [message, setMessage] = useState('')
  const [formError, setFormError] = useState('')
  const [reloadKey, setReloadKey] = useState(0)

  const [summary, setSummary] = useState([])
  const [loadingSummary, setLoadingSummary] = useState(false)
  const [summaryError, setSummaryError] = useState('')

  useEffect(() => {
    async function load() {
      try {
        const [gsData, groupsData, subjectsData, studentsData, enrollmentsData] = await Promise.all([
          getGroupSubjects(),
          getGroups(),
          getSubjects(),
          getStudents(),
          getEnrollments(),
        ])
        setGroupSubjects(gsData)
        setGroups(groupsData)
        setSubjects(subjectsData)
        setStudents(studentsData)
        setEnrollments(enrollmentsData)
      } catch (err) {
        setError(getErrorMessage(err, 'No se pudieron cargar los datos.'))
      } finally {
        setLoading(false)
      }
    }
    load()
  }, [])

  // Cada materia de un grupo se muestra como "G1A · MAT-101 Logica de programacion".
  const options = useMemo(
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

  const selected = options.find((o) => String(o.groupSubjectId) === groupSubjectId)

  // Solo se pasa lista a quien tiene matricula ACTIVA en el grupo de la materia (asi lo exige el backend).
  const roster = useMemo(() => {
    if (!selected) return []
    const activeIds = new Set(
      enrollments.filter((e) => e.groupId === selected.groupId && e.status === 'ACTIVE').map((e) => e.studentId),
    )
    return students
      .filter((s) => activeIds.has(s.userID))
      .sort((a, b) => `${a.firstName} ${a.lastName}`.localeCompare(`${b.firstName} ${b.lastName}`))
  }, [selected, enrollments, students])

  // Al elegir materia o fecha se cargan los registros que ya existan; el resto empieza como "Presente".
  useEffect(() => {
    if (!groupSubjectId || !date) {
      setEntries({})
      setExistingCount(0)
      return
    }
    let active = true
    async function loadSession() {
      setLoadingSession(true)
      setFormError('')
      try {
        const existing = await getAttendances(groupSubjectId, date)
        if (!active) return
        const byStudent = new Map(existing.map((record) => [record.studentId, record]))
        const next = {}
        roster.forEach((student) => {
          const record = byStudent.get(student.userID)
          next[student.userID] = { status: record?.status ?? 'PRESENT', observation: record?.observation ?? '' }
        })
        setEntries(next)
        setExistingCount(existing.length)
      } catch (err) {
        if (active) setFormError(getErrorMessage(err, 'No se pudo cargar la asistencia de esa fecha.'))
      } finally {
        if (active) setLoadingSession(false)
      }
    }
    loadSession()
    return () => {
      active = false
    }
  }, [groupSubjectId, date, roster, reloadKey])

  // Resumen de la materia elegida.
  useEffect(() => {
    if (tab !== 'resumen' || !groupSubjectId) return
    let active = true
    async function loadSummary() {
      setLoadingSummary(true)
      setSummaryError('')
      try {
        const data = await getAttendanceSummary(groupSubjectId)
        if (active) setSummary(data)
      } catch (err) {
        if (active) setSummaryError(getErrorMessage(err, 'No se pudo cargar el resumen.'))
      } finally {
        if (active) setLoadingSummary(false)
      }
    }
    loadSummary()
    return () => {
      active = false
    }
  }, [tab, groupSubjectId, reloadKey])

  const counts = useMemo(() => {
    const result = { PRESENT: 0, ABSENT: 0, LATE: 0, EXCUSED: 0 }
    roster.forEach((student) => {
      const status = entries[student.userID]?.status
      if (status in result) result[status] += 1
    })
    return result
  }, [roster, entries])

  function changeEntry(studentId, patch) {
    setMessage('')
    setEntries((current) => ({ ...current, [studentId]: { ...current[studentId], ...patch } }))
  }

  function markAll(status) {
    setMessage('')
    setEntries((current) => {
      const next = { ...current }
      roster.forEach((student) => {
        next[student.userID] = { ...next[student.userID], status }
      })
      return next
    })
  }

  async function handleSave() {
    setFormError('')
    setMessage('')
    setSaving(true)
    try {
      const records = roster.map((student) => ({
        studentId: student.userID,
        status: entries[student.userID]?.status ?? 'PRESENT',
        observation: entries[student.userID]?.observation ?? '',
      }))
      await registerAttendanceSession({ groupSubjectId, classDate: date, records })
      setMessage(`Asistencia guardada: ${records.length} ${records.length === 1 ? 'estudiante' : 'estudiantes'}.`)
      setReloadKey((key) => key + 1)
    } catch (err) {
      setFormError(getErrorMessage(err, 'No se pudo guardar la asistencia.'))
    } finally {
      setSaving(false)
    }
  }

  const studentName = (id) => {
    const student = students.find((s) => s.userID === id)
    return student ? `${student.firstName} ${student.lastName}` : `Estudiante ${id}`
  }

  if (loading) return <p className="text-ink/70">Cargando...</p>
  if (error) return <p className="text-sm text-rose">{error}</p>

  return (
    <div>
      <PageTitle
        title="Asistencia"
        subtitle="Marca quién asistió a cada clase y consulta el porcentaje de cada estudiante."
      />

      {options.length === 0 ? (
        <EmptyState message="Primero asigna una materia a un grupo en la sección Materias por grupo." />
      ) : (
        <>
          <div className="mb-5 grid gap-4 rounded-xl border border-line bg-white p-5 sm:grid-cols-[1fr_12rem]">
            <Field label="Materia del grupo">
              <select
                value={groupSubjectId}
                onChange={(e) => {
                  setMessage('')
                  setGroupSubjectId(e.target.value)
                }}
                className={inputClass}
              >
                <option value="">Selecciona una materia</option>
                {options.map((o) => (
                  <option key={o.groupSubjectId} value={o.groupSubjectId}>
                    {o.label}
                  </option>
                ))}
              </select>
            </Field>
            <Field label="Fecha de la clase">
              <input
                type="date"
                value={date}
                max={todayISO()}
                onChange={(e) => {
                  setMessage('')
                  setDate(e.target.value)
                }}
                className={inputClass}
              />
            </Field>
          </div>

          <div role="tablist" className="mb-5 inline-flex rounded-lg border border-line bg-white p-1">
            {[
              ['registrar', 'Registrar'],
              ['resumen', 'Resumen'],
            ].map(([id, label]) => (
              <button
                key={id}
                role="tab"
                aria-selected={tab === id}
                onClick={() => setTab(id)}
                className={`rounded-md px-4 py-1.5 text-sm font-medium transition-colors ${
                  tab === id ? 'bg-pine text-white' : 'text-ink/80 hover:bg-tint/10'
                }`}
              >
                {label}
              </button>
            ))}
          </div>

          {!selected ? (
            <EmptyState message="Elige una materia del grupo para continuar." />
          ) : tab === 'registrar' ? (
            roster.length === 0 ? (
              <EmptyState message="Este grupo no tiene estudiantes con matrícula activa." />
            ) : (
              <div className="rounded-xl border border-line bg-white">
                {existingCount > 0 && (
                  <p className="rounded-t-xl bg-straw/20 px-5 py-2.5 text-sm text-ink/80" role="status">
                    Ya hay asistencia registrada para esta fecha. Al guardar se actualizará.
                  </p>
                )}

                <div className="flex flex-wrap items-center justify-between gap-3 border-b border-line px-5 py-3">
                  <ul className="flex flex-wrap gap-2 text-xs font-medium">
                    {Object.entries(statusMeta).map(([status, meta]) => (
                      <li key={status} className={`rounded-full px-2.5 py-1 ${meta.chip}`}>
                        {meta.label}: {counts[status]}
                      </li>
                    ))}
                  </ul>
                  <PrimaryButton variant="secondary" onClick={() => markAll('PRESENT')}>
                    Todos presentes
                  </PrimaryButton>
                </div>

                <ul className={`divide-y divide-line ${loadingSession ? 'opacity-60' : ''}`}>
                  {roster.map((student) => (
                    <li
                      key={student.userID}
                      className="grid items-center gap-3 px-5 py-3 lg:grid-cols-[minmax(0,1fr)_auto_minmax(0,15rem)]"
                    >
                      <div className="min-w-0">
                        <p className="truncate font-medium">
                          {student.firstName} {student.lastName}
                        </p>
                        <p className="text-sm text-ink/70">{student.code}</p>
                      </div>
                      <AttendanceStatusPicker
                        name={`status-${student.userID}`}
                        label={`Asistencia de ${student.firstName} ${student.lastName}`}
                        value={entries[student.userID]?.status ?? 'PRESENT'}
                        onChange={(status) => changeEntry(student.userID, { status })}
                      />
                      <input
                        type="text"
                        maxLength={255}
                        placeholder="Observación (opcional)"
                        aria-label={`Observación de ${student.firstName} ${student.lastName}`}
                        value={entries[student.userID]?.observation ?? ''}
                        onChange={(e) => changeEntry(student.userID, { observation: e.target.value })}
                        className={inputClass}
                      />
                    </li>
                  ))}
                </ul>

                <div className="flex flex-wrap items-center justify-between gap-3 border-t border-line px-5 py-4">
                  <div className="min-h-5 text-sm">
                    {formError && <p className="text-rose">{formError}</p>}
                    {message && (
                      <p className="font-medium text-pine-dark" role="status">
                        {message}
                      </p>
                    )}
                  </div>
                  <PrimaryButton onClick={handleSave} disabled={saving || loadingSession || !date}>
                    {saving ? 'Guardando...' : 'Guardar asistencia'}
                  </PrimaryButton>
                </div>
              </div>
            )
          ) : loadingSummary ? (
            <p className="text-ink/70">Cargando resumen...</p>
          ) : summaryError ? (
            <p className="text-sm text-rose">{summaryError}</p>
          ) : summary.length === 0 ? (
            <EmptyState message="Aún no hay asistencia registrada para esta materia." />
          ) : (
            <>
              <TableShell headers={['Estudiante', 'Presente', 'Ausente', 'Tarde', 'Excusado', 'Clases', 'Asistencia']}>
                {summary.map((row) => (
                  <tr key={row.studentId} className="border-t border-line hover:bg-tint/5">
                    <td className="px-4 py-3 font-medium">{studentName(row.studentId)}</td>
                    <td className="px-4 py-3 tabular-nums">{row.present}</td>
                    <td className="px-4 py-3 tabular-nums">{row.absent}</td>
                    <td className="px-4 py-3 tabular-nums">{row.late}</td>
                    <td className="px-4 py-3 tabular-nums">{row.excused}</td>
                    <td className="px-4 py-3 tabular-nums">{row.total}</td>
                    <td className="px-4 py-3">
                      <span className={`rounded-full px-2.5 py-1 text-xs font-medium ${rateBadge(row.attendanceRate)}`}>
                        {formatRate(row.attendanceRate)}
                      </span>
                    </td>
                  </tr>
                ))}
              </TableShell>
              <p className="mt-3 text-sm text-ink/70">
                El porcentaje cuenta como asistencia las clases en que el estudiante estuvo presente o llegó tarde.
                Verde: {ATTENDANCE_GOAL}% o más.
              </p>
            </>
          )}
        </>
      )}
    </div>
  )
}

export default Attendance
