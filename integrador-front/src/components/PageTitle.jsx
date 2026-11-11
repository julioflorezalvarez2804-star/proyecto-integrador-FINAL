import { useLocation } from 'react-router-dom'
import { findNavItem, tones } from '../config/navigation'

// El icono y el color del titulo salen de la opcion del menu que corresponde a la ruta.
function PageTitle({ title, subtitle, action }) {
  const { pathname } = useLocation()
  const found = findNavItem(pathname)
  const Icon = found?.item.icon

  return (
    <div className="mb-6 flex flex-wrap items-end justify-between gap-4">
      <div className="flex items-center gap-4">
        {Icon && (
          <span
            className={`flex h-12 w-12 shrink-0 items-center justify-center rounded-xl ${tones[found.section.tone].chip}`}
          >
            <Icon size={24} aria-hidden="true" />
          </span>
        )}
        <div>
          <h1 className="font-display text-3xl font-semibold tracking-tight">{title}</h1>
          {subtitle && <p className="mt-1 text-ink/70">{subtitle}</p>}
        </div>
      </div>
      {action}
    </div>
  )
}

export default PageTitle
