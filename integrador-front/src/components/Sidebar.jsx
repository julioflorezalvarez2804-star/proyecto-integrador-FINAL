import { Link, NavLink, useNavigate } from 'react-router-dom'
import { getRoles, getUser, hasRole } from '../config/auth'
import { adminSection, sections, tones } from '../config/navigation'
import { logout } from '../services/authService'

function Sidebar() {
  const navigate = useNavigate()
  const user = getUser()
  const roles = getRoles()
  // "Administracion" solo aparece para los administradores.
  const visibleSections = hasRole('ADMIN') ? [...sections, adminSection] : sections

  function handleLogout() {
    logout()
    navigate('/login', { replace: true })
  }

  return (
    <aside className="flex flex-col bg-deep text-paper md:sticky md:top-0 md:h-screen md:w-64 md:shrink-0">
      <div className="flex items-start justify-between px-5 py-5 md:block md:py-6">
        <div>
          <Link to="/" className="font-display text-lg font-semibold">
            Gestión académica
          </Link>
          <p className="mt-1 text-xs text-paper/70">Proyecto integrador</p>
        </div>
        <button onClick={handleLogout} className="text-sm text-paper/80 hover:text-white md:hidden">
          Cerrar sesión
        </button>
      </div>

      <nav className="flex gap-1 overflow-x-auto px-3 pb-3 md:block md:flex-1 md:overflow-y-auto md:pb-4">
        {visibleSections.map((section) => {
          const tone = tones[section.tone]
          return (
            <div key={section.title} className="flex gap-1 md:mb-3 md:block md:space-y-0.5">
              <p className={`hidden px-3 pb-1 text-xs font-medium md:block ${tone.heading}`}>{section.title}</p>
              {section.items.map((item) => (
                <NavLink
                  key={item.to}
                  to={item.to}
                  className={({ isActive }) =>
                    `flex items-center gap-3 whitespace-nowrap rounded-md px-3 py-1.5 text-sm transition-colors ${
                      isActive ? 'bg-white/15 font-medium text-white' : 'text-paper/80 hover:bg-white/10'
                    }`
                  }
                >
                  <item.icon size={17} className={`shrink-0 ${tone.icon}`} aria-hidden="true" />
                  {item.label}
                </NavLink>
              ))}
            </div>
          )
        })}
      </nav>

      <div className="hidden border-t border-white/10 p-4 md:block">
        {user && (
          <div className="mb-3">
            <p className="truncate text-sm font-medium">{user.username}</p>
            {roles.length > 0 && <p className="truncate text-xs text-paper/70">{roles.join(', ')}</p>}
          </div>
        )}
        <button
          onClick={handleLogout}
          className="w-full rounded-md border border-white/25 px-3 py-2 text-sm text-paper/90 transition-colors hover:bg-white/10"
        >
          Cerrar sesión
        </button>
      </div>
    </aside>
  )
}

export default Sidebar
