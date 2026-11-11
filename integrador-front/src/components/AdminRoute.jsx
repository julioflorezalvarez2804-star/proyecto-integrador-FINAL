import { Navigate, Outlet } from 'react-router-dom'
import { hasRole } from '../config/auth'

// Solo deja pasar a quien tenga el rol ADMIN. Es una proteccion de la pantalla;
// la proteccion real de los datos la debe hacer el backend.
function AdminRoute() {
  return hasRole('ADMIN') ? <Outlet /> : <Navigate to="/dashboard" replace />
}

export default AdminRoute
