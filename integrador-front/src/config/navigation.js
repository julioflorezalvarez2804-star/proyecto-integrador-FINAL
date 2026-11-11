import {
  Award,
  BookOpen,
  CalendarCheck,
  CalendarRange,
  ClipboardCheck,
  GraduationCap,
  Layers,
  LayoutDashboard,
  Presentation,
  ShieldCheck,
  UserRound,
  UsersRound,
} from 'lucide-react'

// Cada seccion del menu tiene su color. Las clases van escritas completas (no armadas con
// texto) para que Tailwind las encuentre.
export const tones = {
  pine: {
    icon: 'text-pine-light',
    heading: 'text-pine-light',
    chip: 'bg-pine/15 text-pine-dark',
    tile: 'bg-pine/10 border-pine/25',
  },
  harbor: {
    icon: 'text-harbor-light',
    heading: 'text-harbor-light',
    chip: 'bg-harbor/20 text-harbor-dark',
    tile: 'bg-harbor/10 border-harbor/30',
  },
  straw: {
    icon: 'text-straw-light',
    heading: 'text-straw-light',
    chip: 'bg-straw/30 text-straw-dark',
    tile: 'bg-straw/15 border-straw/40',
  },
  plum: {
    icon: 'text-plum-light',
    heading: 'text-plum-light',
    chip: 'bg-plum/15 text-plum-dark',
    tile: 'bg-plum/10 border-plum/25',
  },
  rose: {
    icon: 'text-rose-light',
    heading: 'text-rose-light',
    chip: 'bg-rose/15 text-rose-dark',
    tile: 'bg-rose/10 border-rose/25',
  },
}

export const sections = [
  {
    title: 'Resumen',
    tone: 'pine',
    items: [{ to: '/dashboard', label: 'Dashboard', icon: LayoutDashboard }],
  },
  {
    title: 'Académico',
    tone: 'harbor',
    items: [
      { to: '/programas', label: 'Programas', icon: GraduationCap },
      { to: '/periodos', label: 'Periodos', icon: CalendarRange },
      { to: '/grupos', label: 'Grupos', icon: UsersRound },
      { to: '/materias', label: 'Materias', icon: BookOpen },
      { to: '/materias-por-grupo', label: 'Materias por grupo', icon: Layers },
    ],
  },
  {
    title: 'Personas',
    tone: 'straw',
    items: [
      { to: '/estudiantes', label: 'Estudiantes', icon: UserRound },
      { to: '/profesores', label: 'Profesores', icon: Presentation },
    ],
  },
  {
    title: 'Seguimiento',
    tone: 'plum',
    items: [
      { to: '/matriculas', label: 'Matrículas', icon: ClipboardCheck },
      { to: '/notas', label: 'Notas', icon: Award },
      { to: '/asistencia', label: 'Asistencia', icon: CalendarCheck },
    ],
  },
]

export const adminSection = {
  title: 'Administración',
  tone: 'rose',
  items: [{ to: '/usuarios', label: 'Usuarios', icon: ShieldCheck }],
}

// Busca a que seccion y opcion del menu corresponde una ruta (para el icono del titulo).
export function findNavItem(pathname) {
  for (const section of [...sections, adminSection]) {
    const item = section.items.find((candidate) => candidate.to === pathname)
    if (item) return { section, item }
  }
  return null
}
