import { useEffect, useMemo, useState } from 'react'
import { Link } from 'react-router-dom'
import { Award, CalendarCheck, ClipboardCheck, UserRound, UsersRound } from 'lucide-react'
import { getStudents } from '../services/studentService'
import { getPrograms } from '../services/programService'
import { getGroups } from '../services/groupService'
import { getEnrollments } from '../services/enrollmentService'
import { getGrades } from '../services/gradeService'
import { getAttendanceSummary } from '../services/attendanceService'
import { getUser } from '../config/auth'
import { getErrorMessage } from '../config/api'
import { formatDate, formatRate } from '../config/format'
import { tones } from '../config/navigation'
import PageTitle from '../components/PageTitle'
import Panel from '../components/Panel'

const PASSING_SCORE = 3.0

const statusInfo = {
  ACTIVE: { plural: 'Activas', single: 'Activa', badge: 'bg-pine/15 text-pine-dark' },
  COMPLETED: { plural: 'Completadas', single: 'Completada', badge: 'bg-harbor/20 text-harbor-dark' },
  CANCELLED: { plural: 'Canceladas', single: 'Cancelada', badge: 'bg-rose/15 text-rose-dark' },
}

// Tarjeta de resumen: lleva a la pagina de ese tema.
function StatTile({ to, label, value, hint, icon: Icon, tone }) {
  return (
    <Link
      to={to}
      className={`block rounded-xl border p-5 transition-shadow hover:shadow-md ${tones[tone].tile}`}
    >
      <div className="flex min-h-10 items-start justify-between gap-3">
        <p className="text-sm font-medium text-ink/80">{label}</p>
        <span className={`flex h-9 w-9 shrink-0 items-center justify-center rounded-lg ${tones[tone].chip}`}>
          <Icon size={18} aria-hidden="true" />
        </span>
      </div>
      <p className="mt-3 font-display text-4xl font-semibold tabular-nums">{value}</p>
      <p className="mt-1 text-sm text-ink/70">{hint}</p>
    </Link>
  )
}

function Dashboard() {
  const [students, setStudents] = useState([])
  const [programs, setPrograms] = useState([])
  const [groups, setGroups] = useState([])
  const [enrollments, setEnrollments] = useState([])
  const [grades, setGrades] = useState([])
  const [attendance, setAttendance] = useState(null) // null = no disponible
  const [loading, setLoading] = useState(true)
  const [error, setError] = useState('')

  const user = getUser()

  useEffect(() => {
    async function load() {
      try {
        const [studentsData, programsData, groupsData, enrollmentsData, gradesData] = await Promise.all([
          getStudents(),
          getPrograms(),
          getGroups(),
          getEnrollments(),
          getGrades(),
        ])
        setStudents(studentsData)
        setPrograms(programsData)
        setGroups(groupsData)
        setEnrollments(enrollmentsData)
        setGrades(gradesData)
      } catch (err) {
        setError(getErrorMessage(err, 'No se pudo cargar el resumen.'))
      } finally {
        setLoading(false)
      }
    }
    load()

    // La asistencia es un dato secundario: si falla, el resto del dashboard sigue funcionando.
    getAttendanceSummary()
      .then(setAttendance)
      .catch(() => setAttendance(null))
  }, [])

  const stats = useMemo(() => {
    const countByStatus = { ACTIVE: 0, COMPLETED: 0, CANCELLED: 0 }
    enrollments.forEach((e) => {
      if (e.status in countByStatus) countByStatus[e.status] += 1
    })

    const average = grades.length ? grades.reduce((sum, g) => sum + Number(g.finalScore), 0) / grades.length : null
    const passed = grades.filter((g) => Number(g.finalScore) >= PASSING_SCORE).length

    const latest = [...enrollments]
      .sort((a, b) => String(b.enrolledAt).localeCompare(String(a.enrolledAt)))
      .slice(0, 5)
      .map((e) => {
        const student = students.find((s) => s.userID === e.studentId)
        const group = groups.find((g) => g.groupId === e.groupId)
        return {
          ...e,
          studentName: student ? `${student.firstName} ${student.lastName}` : '—',
          groupLabel: group ? group.code : '—',
        }
      })

    const attendedTotal = (attendance ?? []).reduce((sum, row) => sum + row.present + row.late, 0)
    const classesTotal = (attendance ?? []).reduce((sum, row) => sum + row.total, 0)

    return {
      attendedTotal,
      classesTotal,
      countByStatus,
      activeStudents: students.filter((s) => s.status).length,
      average,
      passed,
      latest,
    }
  }, [students, groups, enrollments, grades, attendance])

  if (loading) return <p className="text-ink/70">Cargando resumen...</p>
  if (error) return <p className="text-sm text-rose">{error}</p>

  return (
    <div>
      <PageTitle
        title="Dashboard"
        subtitle={`${user ? `Hola, ${user.username}. ` : ''}Este es el estado actual del sistema.`}
      />

      <div className="grid gap-4 sm:grid-cols-2 xl:grid-cols-5">
        <StatTile
          to="/estudiantes"
          label="Estudiantes"
          value={students.length}
          hint={`${stats.activeStudents} ${stats.activeStudents === 1 ? 'activo' : 'activos'}`}
          icon={UserRound}
          tone="straw"
        />
        <StatTile
          to="/matriculas"
          label="Matrículas activas"
          value={stats.countByStatus.ACTIVE}
          hint={`de ${enrollments.length} en total`}
          icon={ClipboardCheck}
          tone="plum"
        />
        <StatTile
          to="/grupos"
          label="Grupos"
          value={groups.length}
          hint={`${programs.length} ${programs.length === 1 ? 'programa' : 'programas'}`}
          icon={UsersRound}
          tone="harbor"
        />
        <StatTile
          to="/notas"
          label="Promedio de notas"
          value={stats.average === null ? '—' : stats.average.toFixed(1)}
          hint={
            grades.length === 0
              ? 'Sin notas registradas'
              : `${grades.length} ${grades.length === 1 ? 'nota' : 'notas'}, ${stats.passed} ${stats.passed === 1 ? 'aprobada' : 'aprobadas'}`
          }
          icon={Award}
          tone="pine"
        />
        <StatTile
          to="/asistencia"
          label="Asistencia"
          value={stats.classesTotal === 0 ? '—' : formatRate((stats.attendedTotal / stats.classesTotal) * 100)}
          hint={
            attendance === null
              ? 'No disponible'
              : stats.classesTotal === 0
                ? 'Sin registros'
                : `${stats.classesTotal} ${stats.classesTotal === 1 ? 'registro' : 'registros'}`
          }
          icon={CalendarCheck}
          tone="rose"
        />
      </div>

      <div className="mt-6 grid gap-6 lg:grid-cols-2">
        <Panel title="Estado de las matrículas">
          <ul className="divide-y divide-line">
            {Object.entries(statusInfo).map(([status, info]) => (
              <li key={status} className="flex items-center justify-between py-2.5 first:pt-0 last:pb-0">
                <span className={`rounded-full px-2.5 py-1 text-xs font-medium ${info.badge}`}>{info.plural}</span>
                <span className="font-display text-xl font-semibold tabular-nums">{stats.countByStatus[status]}</span>
              </li>
            ))}
          </ul>
        </Panel>

        <Panel title="Últimas matrículas">
          {stats.latest.length === 0 ? (
            <p className="text-sm text-ink/70">Aún no hay matrículas.</p>
          ) : (
            <ul className="divide-y divide-line">
              {stats.latest.map((e) => (
                <li key={e.enrollmentId} className="flex items-center justify-between gap-3 py-2.5 text-sm first:pt-0 last:pb-0">
                  <div className="min-w-0">
                    <p className="truncate font-medium">{e.studentName}</p>
                    <p className="text-ink/70">
                      {e.groupLabel} · {formatDate(e.enrolledAt)}
                    </p>
                  </div>
                  <span
                    className={`shrink-0 rounded-full px-2.5 py-1 text-xs font-medium ${statusInfo[e.status]?.badge ?? 'bg-mist'}`}
                  >
                    {statusInfo[e.status]?.single ?? e.status}
                  </span>
                </li>
              ))}
            </ul>
          )}
        </Panel>
      </div>
    </div>
  )
}

export default Dashboard
