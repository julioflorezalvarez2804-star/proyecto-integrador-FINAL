import api from '../config/api'
import { AUTH_MODE, clearSession, saveSession } from '../config/auth'
import { httpError } from '../config/errors'
import { demoAdmin, findDemoUserByCredentials, registerDemoUser } from './demoStore'

// Usuario administrador de prueba del modo demo. Solo existe en el navegador.
export const demoCredentials = { username: demoAdmin.username, password: demoAdmin.password }
const DEMO_SESSION_SECONDS = 8 * 60 * 60

const delay = (ms) => new Promise((resolve) => setTimeout(resolve, ms))

export async function login(username, password) {
  if (AUTH_MODE === 'demo') {
    await delay(400)

    let account = null
    if (username === demoAdmin.username && password === demoAdmin.password) {
      account = { username: demoAdmin.username, email: demoAdmin.email, status: 'ACTIVE', roles: ['ADMIN'] }
    } else {
      account = await findDemoUserByCredentials(username, password)
    }

    if (!account) throw httpError(401, 'Usuario o contrasena incorrectos')
    if (account.status !== 'ACTIVE') throw httpError(403, 'El usuario esta inactivo')

    saveSession({
      token: 'demo-token',
      user: { username: account.username, email: account.email },
      roles: account.roles,
      expiresInSeconds: DEMO_SESSION_SECONDS,
    })
    return
  }

  // Modo api: el backend devuelve { token, user, roles, expiresInSeconds }.
  const { data } = await api.post('/api/auth/login', { username, password })
  saveSession({
    token: data.token,
    user: data.user,
    roles: data.roles,
    expiresInSeconds: data.expiresInSeconds,
  })
}

// La cuenta nueva queda INACTIVE con rol PROFESOR hasta que un administrador la active.
export async function register({ username, email, password }) {
  if (AUTH_MODE === 'demo') {
    await delay(400)
    await registerDemoUser({ username, email, password })
    return
  }
  await api.post('/api/auth/register', { username, email, password })
}

export function logout() {
  clearSession()
}
