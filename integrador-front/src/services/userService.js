import api from '../config/api'
import { AUTH_MODE } from '../config/auth'
import { DEMO_ROLES, deleteDemoUser, demoAdmin, listDemoUsers, updateDemoUser } from './demoStore'

// Devuelve los usuarios con el nombre de sus roles, y la lista de roles disponibles.
export async function getUsersWithRoles() {
  if (AUTH_MODE === 'demo') {
    const builtIn = {
      userId: 0,
      username: demoAdmin.username,
      email: demoAdmin.email,
      status: 'ACTIVE',
      roles: ['ADMIN'],
      createdAt: null,
    }
    return { users: [builtIn, ...listDemoUsers()], roles: DEMO_ROLES }
  }

  const [usersResponse, rolesResponse] = await Promise.all([api.get('/api/users'), api.get('/api/roles')])
  const roleNames = new Map(rolesResponse.data.map((role) => [role.roleId, role.name]))

  // El backend entrega los roles de un usuario en otro endpoint, asi que se consulta uno por uno.
  const users = await Promise.all(
    usersResponse.data.map(async (user) => {
      const { data } = await api.get(`/api/user-roles/user/${user.userId}`)
      return { ...user, roles: data.map((userRole) => roleNames.get(userRole.roleId)).filter(Boolean) }
    }),
  )
  return { users, roles: rolesResponse.data }
}

export async function updateUserStatus(user, status) {
  if (AUTH_MODE === 'demo') {
    updateDemoUser(user.userId, { status })
    return
  }
  await api.put(`/api/users/${user.userId}`, { username: user.username, email: user.email, status })
}

// Deja al usuario exactamente con los roles elegidos: asigna los nuevos y quita los que sobran.
export async function setUserRoles(user, selectedNames, allRoles) {
  if (AUTH_MODE === 'demo') {
    updateDemoUser(user.userId, { roles: selectedNames })
    return
  }

  const roleIdOf = (name) => allRoles.find((role) => role.name === name)?.roleId
  const toAdd = selectedNames.filter((name) => !user.roles.includes(name))
  const toRemove = user.roles.filter((name) => !selectedNames.includes(name))

  for (const name of toAdd) {
    await api.post('/api/user-roles', null, { params: { userId: user.userId, roleId: roleIdOf(name) } })
  }
  for (const name of toRemove) {
    await api.delete('/api/user-roles', { params: { userId: user.userId, roleId: roleIdOf(name) } })
  }
}

export async function deleteUser(user) {
  if (AUTH_MODE === 'demo') {
    deleteDemoUser(user.userId)
    return
  }
  await api.delete(`/api/users/${user.userId}`)
}
