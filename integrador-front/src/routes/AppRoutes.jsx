import { Navigate, Route, Routes } from 'react-router-dom'
import MainLayout from '../layouts/MainLayout'
import ProtectedRoute from '../components/ProtectedRoute'
import AdminRoute from '../components/AdminRoute'
import Landing from '../pages/Landing'
import Login from '../pages/Login'
import Register from '../pages/Register'
import Dashboard from '../pages/Dashboard'
import Students from '../pages/Students'
import Teachers from '../pages/Teachers'
import Programs from '../pages/Programs'
import Periods from '../pages/Periods'
import Groups from '../pages/Groups'
import Subjects from '../pages/Subjects'
import GroupSubjects from '../pages/GroupSubjects'
import Enrollments from '../pages/Enrollments'
import Grades from '../pages/Grades'
import Attendance from '../pages/Attendance'
import Users from '../pages/Users'

function AppRoutes() {
  return (
    <Routes>
      <Route path="/" element={<Landing />} />
      <Route path="/login" element={<Login />} />
      <Route path="/registro" element={<Register />} />

      <Route element={<ProtectedRoute />}>
        <Route element={<MainLayout />}>
          <Route path="/dashboard" element={<Dashboard />} />
          <Route path="/programas" element={<Programs />} />
          <Route path="/periodos" element={<Periods />} />
          <Route path="/grupos" element={<Groups />} />
          <Route path="/materias" element={<Subjects />} />
          <Route path="/materias-por-grupo" element={<GroupSubjects />} />
          <Route path="/estudiantes" element={<Students />} />
          <Route path="/profesores" element={<Teachers />} />
          <Route path="/matriculas" element={<Enrollments />} />
          <Route path="/notas" element={<Grades />} />
          <Route path="/asistencia" element={<Attendance />} />

          <Route element={<AdminRoute />}>
            <Route path="/usuarios" element={<Users />} />
          </Route>
        </Route>
      </Route>

      <Route path="*" element={<Navigate to="/" replace />} />
    </Routes>
  )
}

export default AppRoutes
