import { useEffect, useState } from 'react'
import { Link } from 'react-router-dom'
import {
  Award,
  BookOpen,
  CalendarCheck,
  CalendarRange,
  ClipboardCheck,
  GraduationCap,
  LayoutDashboard,
  Presentation,
  UserRound,
  UsersRound,
} from 'lucide-react'
import api from '../config/api'
import { isAuthenticated } from '../config/auth'
import { tones } from '../config/navigation'
import dashboardPreview from '../assets/dashboard-preview.webp'

const days = ['Lun', 'Mar', 'Mié', 'Jue', 'Vie', 'Sáb']
const times = ['7:00', '9:00', '11:00', '14:00']

// day: 0 = lunes ... 5 = sabado. time: fila del horario. span: cuantas filas ocupa.
const blocks = [
  { day: 0, time: 0, label: 'Lógica', short: 'Lógica', tone: 'border-pine/30 bg-pine/15' },
  { day: 2, time: 0, label: 'Bases de datos', short: 'Datos', tone: 'border-harbor/40 bg-harbor/25' },
  { day: 1, time: 1, label: 'Inglés', short: 'Inglés', tone: 'border-straw/60 bg-straw/35' },
  { day: 3, time: 1, label: 'Frontend', short: 'Front', tone: 'border-pine/30 bg-pine/15' },
  { day: 4, time: 2, label: 'Backend', short: 'Back', tone: 'border-plum/40 bg-plum/20' },
  { day: 0, time: 3, label: 'Tutoría', short: 'Tutoría', tone: 'border-straw/60 bg-straw/35' },
  { day: 5, time: 0, span: 3, label: 'Proyecto integrador', short: 'Proy.', tone: 'border-pine-dark bg-pine text-white' },
]

const highlights = [
  {
    icon: LayoutDashboard,
    tone: 'pine',
    title: 'Un resumen claro',
    text: 'Estudiantes, matrículas activas, grupos y promedio de notas apenas entras.',
  },
  {
    icon: ClipboardCheck,
    tone: 'plum',
    title: 'Matrículas al día',
    text: 'Matricula, completa o cancela, y mira cuáles son las más recientes.',
  },
  {
    icon: Award,
    tone: 'straw',
    title: 'Notas por materia',
    text: 'Registra la nota final de cada estudiante en el grupo donde está matriculado.',
  },
]

const modules = [
  { name: 'Dashboard', to: '/dashboard', icon: LayoutDashboard, tone: 'pine', text: 'Un resumen del sistema: matrículas, grupos, promedio de notas y lo más reciente.' },
  { name: 'Estudiantes', to: '/estudiantes', icon: UserRound, tone: 'straw', text: 'Registra a cada estudiante con su documento y su fecha de nacimiento.' },
  { name: 'Programas', to: '/programas', icon: GraduationCap, tone: 'harbor', text: 'Los programas académicos que ofrece la institución.' },
  { name: 'Periodos', to: '/periodos', icon: CalendarRange, tone: 'harbor', text: 'Los ciclos académicos, con su fecha de inicio y de cierre.' },
  { name: 'Grupos', to: '/grupos', icon: UsersRound, tone: 'harbor', text: 'Un grupo une un programa, un periodo y una jornada.' },
  { name: 'Matrículas', to: '/matriculas', icon: ClipboardCheck, tone: 'plum', text: 'Inscribe estudiantes en un grupo, complétalas o cancélalas.' },
  { name: 'Materias y profesores', to: '/materias', icon: Presentation, tone: 'straw', text: 'Las materias de cada programa y los docentes que las dictan en cada grupo.' },
  { name: 'Notas', to: '/notas', icon: Award, tone: 'plum', text: 'Registra la nota final de cada estudiante en cada materia.' },
  { name: 'Asistencia', to: '/asistencia', icon: CalendarCheck, tone: 'plum', text: 'Marca quién asistió a cada clase y consulta el porcentaje de cada estudiante.' },
]

const steps = [
  { title: 'Crea un programa', text: 'Es la base: todo grupo pertenece a uno.', tone: 'harbor' },
  { title: 'Define un periodo', text: 'Con sus fechas de inicio y de cierre.', tone: 'harbor' },
  { title: 'Abre un grupo', text: 'Elige programa, periodo y jornada.', tone: 'harbor' },
  { title: 'Registra estudiantes', text: 'Con su código y su documento.', tone: 'straw' },
  { title: 'Matricula', text: 'Une al estudiante con su grupo.', tone: 'plum' },
]

function ServerStatus() {
  const [status, setStatus] = useState('checking')

  useEffect(() => {
    let active = true
    api
      .get('/api/programs', { timeout: 4000 })
      .then(() => active && setStatus('online'))
      // Si el servidor responde (aunque sea 401), esta conectado.
      .catch((err) => active && setStatus(err.response ? 'online' : 'offline'))
    return () => {
      active = false
    }
  }, [])

  const dot = { checking: 'bg-line', online: 'bg-pine', offline: 'bg-rose' }[status]
  const text = {
    checking: 'Comprobando el servidor...',
    online: 'Servidor conectado',
    offline: 'No se detecta el servidor en el puerto 8081',
  }[status]

  return (
    <p className="mt-8 flex items-center gap-2 text-sm text-ink/70" role="status">
      <span className={`h-2 w-2 rounded-full ${dot}`} aria-hidden="true" />
      {text}
    </p>
  )
}

function FloatingCard({ icon: Icon, tone, title, subtitle, className }) {
  return (
    <div
      className={`absolute hidden items-center gap-3 rounded-xl border border-line bg-white px-3.5 py-2.5 shadow-lg lg:flex ${className}`}
    >
      <span className={`flex h-9 w-9 items-center justify-center rounded-lg ${tones[tone].chip}`}>
        <Icon size={18} aria-hidden="true" />
      </span>
      <div>
        <p className="text-sm font-medium">{title}</p>
        <p className="text-xs text-ink/70">{subtitle}</p>
      </div>
    </div>
  )
}

function Timetable() {
  return (
    <div className="relative">
      <div aria-hidden="true" className="absolute -right-3 -top-3 h-full w-full rounded-3xl bg-tint/20" />
      <div
        role="img"
        aria-label="Ejemplo de horario semanal de un grupo sabatino"
        className="relative rounded-2xl border border-line bg-white p-4 shadow-[0_24px_48px_-28px_rgba(23,32,54,0.45)] sm:p-5"
      >
        <div className="mb-4 flex items-baseline justify-between">
          <p className="font-display text-lg font-semibold">Grupo G1A</p>
          <p className="text-sm text-ink/70">Jornada sabatina</p>
        </div>

        <div className="grid grid-cols-[2rem_repeat(6,minmax(0,1fr))] gap-1 text-xs sm:grid-cols-[2.5rem_repeat(6,minmax(0,1fr))] sm:gap-1.5">
          {days.map((day, i) => (
            <div key={day} style={{ gridColumn: i + 2, gridRow: 1 }} className="pb-1 text-center font-medium text-ink/70">
              {day}
            </div>
          ))}

          {times.map((time, row) => (
            <div key={time} style={{ gridColumn: 1, gridRow: row + 2 }} className="pt-1 text-[11px] text-ink/70">
              {time}
            </div>
          ))}

          {times.map((time, row) =>
            days.map((day, col) => (
              <div
                key={`${time}-${day}`}
                style={{ gridColumn: col + 2, gridRow: row + 2 }}
                className="min-h-14 rounded-md bg-mist/70"
              />
            )),
          )}

          {blocks.map((block, i) => (
            <div
              key={block.label}
              style={{
                gridColumn: block.day + 2,
                gridRow: `${block.time + 2} / span ${block.span ?? 1}`,
                animationDelay: `${i * 110}ms`,
              }}
              className={`block-in flex items-start rounded-md border px-1 py-1.5 text-[10px] font-medium leading-tight sm:px-1.5 sm:text-[11px] ${block.tone}`}
            >
              <span className="sm:hidden">{block.short}</span>
              <span className="hidden sm:inline">{block.label}</span>
            </div>
          ))}
        </div>
      </div>

      <FloatingCard
        icon={ClipboardCheck}
        tone="plum"
        title="Matrícula activa"
        subtitle="Camilo Rios · G1A"
        className="-left-10 -bottom-8"
      />
      <FloatingCard
        icon={Award}
        tone="straw"
        title="Nota final 4.5"
        subtitle="Lógica · Aprobada"
        className="-right-6 -top-10"
      />
    </div>
  )
}

function Landing() {
  // Con sesion se entra directo al sistema; sin sesion, al login.
  const entryPath = isAuthenticated() ? '/dashboard' : '/login'

  return (
    <div className="min-h-screen">
      <div className="relative overflow-hidden bg-tint/8">
        <GraduationCap
          aria-hidden="true"
          strokeWidth={1}
          className="pointer-events-none absolute -right-10 top-24 h-80 w-80 -rotate-12 text-tint/12"
        />
        <BookOpen
          aria-hidden="true"
          strokeWidth={1}
          className="pointer-events-none absolute -left-12 bottom-0 h-64 w-64 rotate-6 text-harbor/20"
        />

        <header className="relative mx-auto flex max-w-6xl items-center justify-between px-6 py-6">
          <Link to="/" className="font-display text-xl font-semibold tracking-tight">
            Gestión académica
          </Link>
          <nav className="flex items-center gap-6 text-sm">
            <a href="#vistazo" className="hidden text-ink/80 hover:text-ink sm:inline">
              Un vistazo
            </a>
            <a href="#modulos" className="hidden text-ink/80 hover:text-ink sm:inline">
              Módulos
            </a>
            <a href="#orden" className="hidden text-ink/80 hover:text-ink sm:inline">
              Cómo empezar
            </a>
            <Link
              to={entryPath}
              className="rounded-md bg-pine px-4 py-2 font-medium text-white transition-colors hover:bg-pine-dark"
            >
              Entrar al sistema
            </Link>
          </nav>
        </header>

        <section className="relative mx-auto grid max-w-6xl items-center gap-16 px-6 pb-28 pt-10 lg:grid-cols-[1fr_1.05fr] lg:pt-16">
          <div>
            <h1 className="text-balance font-display text-4xl font-semibold leading-[1.1] tracking-tight sm:text-5xl">
              Programas, grupos y matrículas en un solo lugar
            </h1>
            <p className="mt-5 max-w-md text-lg leading-relaxed text-ink/80">
              Administra la información académica de tu institución: registra estudiantes, abre grupos por periodo y
              matricula en pocos pasos.
            </p>
            <div className="mt-8 flex flex-wrap gap-3">
              <Link
                to={entryPath}
                className="rounded-md bg-pine px-5 py-3 text-sm font-medium text-white transition-colors hover:bg-pine-dark"
              >
                Entrar al sistema
              </Link>
              <a
                href="#orden"
                className="rounded-md border border-pine/30 bg-white px-5 py-3 text-sm font-medium transition-colors hover:bg-pine/10"
              >
                Ver cómo empezar
              </a>
            </div>
            <ServerStatus />
          </div>

          <Timetable />
        </section>
      </div>

      <main>
        <section id="vistazo" className="mx-auto grid max-w-6xl items-center gap-12 px-6 py-24 lg:grid-cols-[1fr_1.45fr]">
          <div>
            <h2 className="text-balance font-display text-3xl font-semibold tracking-tight">
              Todo el estado del sistema, de un vistazo
            </h2>
            <p className="mt-3 max-w-md leading-relaxed text-ink/80">
              Al entrar ves lo importante sin buscar: cuántos estudiantes hay, cómo van las matrículas y cómo van las
              notas.
            </p>
            <ul className="mt-8 space-y-5">
              {highlights.map((item) => (
                <li key={item.title} className="flex gap-4">
                  <span
                    className={`flex h-11 w-11 shrink-0 items-center justify-center rounded-xl ${tones[item.tone].chip}`}
                  >
                    <item.icon size={20} aria-hidden="true" />
                  </span>
                  <div>
                    <p className="font-display text-lg font-medium">{item.title}</p>
                    <p className="mt-0.5 text-sm leading-relaxed text-ink/70">{item.text}</p>
                  </div>
                </li>
              ))}
            </ul>
          </div>

          <figure className="overflow-hidden rounded-xl border border-line bg-white shadow-[0_28px_56px_-30px_rgba(27,43,82,0.55)]">
            <div className="flex items-center gap-2 border-b border-line bg-mist px-4 py-2.5">
              <span className="h-2.5 w-2.5 rounded-full bg-rose-light" aria-hidden="true" />
              <span className="h-2.5 w-2.5 rounded-full bg-straw-light" aria-hidden="true" />
              <span className="h-2.5 w-2.5 rounded-full bg-pine-light" aria-hidden="true" />
              <span className="ml-3 rounded-md bg-white px-3 py-0.5 text-xs text-ink/70">gestion-academica / dashboard</span>
            </div>
            <img
              src={dashboardPreview}
              alt="Vista del dashboard con estudiantes, matrículas activas, grupos, promedio de notas y las últimas matrículas"
              width="1280"
              height="800"
              loading="lazy"
              className="block w-full"
            />
          </figure>
        </section>

        <section id="modulos" className="border-y border-harbor/20 bg-harbor/10">
          <div className="mx-auto grid max-w-6xl gap-12 px-6 py-24 lg:grid-cols-[1fr_1.5fr]">
            <div>
              <h2 className="font-display text-3xl font-semibold tracking-tight">Lo que puedes gestionar</h2>
              <p className="mt-3 max-w-sm leading-relaxed text-ink/80">
                Cada módulo tiene su lista, su búsqueda y su formulario. Todo lo que guardes queda en la base de datos
                del proyecto.
              </p>
            </div>
            <ul className="divide-y divide-harbor/25 rounded-xl border border-harbor/25 bg-white">
              {modules.map((module) => (
                <li key={module.name}>
                  <Link
                    to={module.to}
                    className="group flex items-center gap-4 px-4 py-4 transition-colors first:rounded-t-xl last:rounded-b-xl hover:bg-harbor/10"
                  >
                    <span
                      className={`flex h-10 w-10 shrink-0 items-center justify-center rounded-lg ${tones[module.tone].chip}`}
                    >
                      <module.icon size={19} aria-hidden="true" />
                    </span>
                    <div className="min-w-0 flex-1">
                      <p className="font-display text-lg font-medium">{module.name}</p>
                      <p className="text-sm text-ink/70">{module.text}</p>
                    </div>
                    <span className="shrink-0 text-sm font-medium text-pine group-hover:underline">Abrir</span>
                  </Link>
                </li>
              ))}
            </ul>
          </div>
        </section>

        <section id="orden" className="mx-auto max-w-6xl px-6 py-24">
          <h2 className="font-display text-3xl font-semibold tracking-tight">Para empezar, sigue este orden</h2>
          <p className="mt-3 max-w-lg leading-relaxed text-ink/80">
            Unos datos dependen de otros: un grupo necesita programa y periodo, y una matrícula necesita estudiante y
            grupo. Para registrar notas y asistencia necesitas además una materia y un profesor asignados al grupo.
          </p>
          <ol className="mt-10 grid gap-8 sm:grid-cols-2 lg:grid-cols-5">
            {steps.map((step, i) => (
              <li key={step.title}>
                <span
                  className={`flex h-10 w-10 items-center justify-center rounded-full font-display text-lg font-semibold ${tones[step.tone].chip}`}
                >
                  {i + 1}
                </span>
                <p className="mt-4 font-display text-lg font-medium">{step.title}</p>
                <p className="mt-1 text-sm leading-relaxed text-ink/70">{step.text}</p>
              </li>
            ))}
          </ol>
        </section>

        <section className="relative overflow-hidden bg-deep text-paper">
          <div aria-hidden="true" className="pointer-events-none absolute -right-16 -top-20 h-72 w-72 rounded-full bg-harbor/30" />
          <div aria-hidden="true" className="pointer-events-none absolute -bottom-24 right-48 h-52 w-52 rounded-full bg-straw/25" />
          <div aria-hidden="true" className="pointer-events-none absolute -left-16 bottom-0 h-56 w-56 rounded-full bg-plum/30" />
          <div className="relative mx-auto flex max-w-6xl flex-col items-start justify-between gap-6 px-6 py-16 md:flex-row md:items-center">
            <div>
              <h2 className="max-w-md text-balance font-display text-3xl font-semibold tracking-tight">
                Empieza a organizar tu institución
              </h2>
              <p className="mt-2 text-paper/80">Crea tu primer programa y sigue el orden sugerido.</p>
            </div>
            <Link
              to={entryPath}
              className="rounded-md bg-straw px-6 py-3 text-sm font-medium text-ink transition-colors hover:bg-straw-light"
            >
              Entrar al sistema
            </Link>
          </div>
        </section>
      </main>

      <footer className="border-t border-line">
        <div className="mx-auto flex max-w-6xl flex-wrap items-center justify-between gap-2 px-6 py-6 text-sm text-ink/70">
          <p>Proyecto integrador CESDE 2026</p>
          <p>Gestión académica</p>
        </div>
      </footer>
    </div>
  )
}

export default Landing
