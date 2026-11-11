import { httpError } from '../config/errors'

// Usuarios del modo demo: viven solo en este navegador (localStorage). No es seguridad real.
const KEY = 'gestion-academica.demo-users'

export const demoAdmin = { username: 'admin', password: 'admin123', email: 'admin@demo.local' }

export const DEMO_ROLES = [
  { roleId: 1, name: 'ADMIN' },
  { roleId: 2, name: 'PROFESOR' },
]

function read() {
  try {
    return JSON.parse(localStorage.getItem(KEY)) ?? []
  } catch {
    return []
  }
}

function write(users) {
  try {
    localStorage.setItem(KEY, JSON.stringify(users))
  } catch {
    // Sin almacenamiento no se pueden guardar los usuarios de prueba.
  }
}

// Ni siquiera en el modo demo se guardan las contrasenas en texto plano.
async function sha256(text) {
  const buffer = await crypto.subtle.digest('SHA-256', new TextEncoder().encode(text))
  return [...new Uint8Array(buffer)].map((byte) => byte.toString(16).padStart(2, '0')).join('')
}

const same = (a, b) => a.toLowerCase() === b.toLowerCase()

export async function registerDemoUser({ username, email, password }) {
  const users = read()

  if (same(username, demoAdmin.username) || users.some((u) => same(u.username, username))) {
    throw httpError(409, `Ya existe un usuario con el username: ${username}`)
  }
  if (same(email, demoAdmin.email) || users.some((u) => same(u.email, email))) {
    throw httpError(409, `Ya existe un usuario registrado con el email: ${email}`)
  }

  const nextId = Math.max(0, ...users.map((u) => u.userId)) + 1
  write([
    ...users,
    {
      userId: nextId,
      username,
      email,
      passwordHash: await sha256(password),
      status: 'INACTIVE', // pendiente de aprobacion
      roles: ['PROFESOR'],
      createdAt: new Date().toISOString(),
    },
  ])
}

export async function findDemoUserByCredentials(username, password) {
  const passwordHash = await sha256(password)
  const user = read().find((u) => same(u.username, username) && u.passwordHash === passwordHash)
  return user ?? null
}

export function listDemoUsers() {
  return read().map(({ passwordHash, ...user }) => user)
}

export function updateDemoUser(userId, patch) {
  write(read().map((user) => (user.userId === userId ? { ...user, ...patch } : user)))
}

export function deleteDemoUser(userId) {
  write(read().filter((user) => user.userId !== userId))
}
