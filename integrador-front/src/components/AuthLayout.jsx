import { Link } from 'react-router-dom'
import { Award, ClipboardCheck } from 'lucide-react'

// Marco compartido de las pantallas de login y registro: panel de color a la izquierda
// y el contenido (formulario) a la derecha.
function AuthLayout({ children }) {
  return (
    <div className="min-h-screen lg:grid lg:grid-cols-[1fr_1.1fr]">
      <aside className="relative hidden flex-col justify-between overflow-hidden bg-deep p-12 text-paper lg:flex">
        {/* Formas decorativas */}
        <div aria-hidden="true" className="pointer-events-none absolute -bottom-28 -right-24 h-96 w-96 rounded-full bg-harbor/30" />
        <div aria-hidden="true" className="pointer-events-none absolute bottom-24 right-28 h-44 w-44 rounded-full bg-straw/30" />
        <div aria-hidden="true" className="pointer-events-none absolute -left-24 -top-24 h-64 w-64 rounded-full bg-plum/30" />
        <div aria-hidden="true" className="pointer-events-none absolute right-16 top-16 h-24 w-24 rounded-full bg-tint/50" />

        <Link to="/" className="relative font-display text-xl font-semibold">
          Gestión académica
        </Link>

        <div className="relative">
          <p className="max-w-md text-balance font-display text-4xl font-semibold leading-tight">
            Todo el registro académico, ordenado y a la mano
          </p>
          <p className="mt-4 max-w-sm leading-relaxed text-paper/80">
            Administra programas, periodos, grupos, estudiantes y matrículas desde un solo lugar.
          </p>

          <div className="mt-8 flex flex-wrap gap-3">
            <span className="flex items-center gap-2 rounded-full bg-white/10 px-3.5 py-2 text-sm">
              <ClipboardCheck size={16} className="text-pine-light" aria-hidden="true" />
              Matrículas al día
            </span>
            <span className="flex items-center gap-2 rounded-full bg-white/10 px-3.5 py-2 text-sm">
              <Award size={16} className="text-straw-light" aria-hidden="true" />
              Notas por materia
            </span>
          </div>
        </div>

        <p className="relative text-sm text-paper/70">Proyecto integrador CESDE 2026</p>
      </aside>

      <main className="flex items-center justify-center px-6 py-12">
        <div className="w-full max-w-sm">
          <Link to="/" className="font-display text-xl font-semibold lg:hidden">
            Gestión académica
          </Link>
          {children}
        </div>
      </main>
    </div>
  )
}

export default AuthLayout
