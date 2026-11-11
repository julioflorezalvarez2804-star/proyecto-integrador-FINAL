import { useState } from 'react'
import { Link, Navigate, useLocation, useNavigate } from 'react-router-dom'
import { AUTH_MODE, isAuthenticated } from '../config/auth'
import { getErrorMessage } from '../config/api'
import { demoCredentials, login } from '../services/authService'
import { inputClass } from '../components/Field'
import AuthLayout from '../components/AuthLayout'
import PasswordInput from '../components/PasswordInput'
import PrimaryButton from '../components/PrimaryButton'

function Login() {
  const navigate = useNavigate()
  const location = useLocation()

  const [username, setUsername] = useState('')
  const [password, setPassword] = useState('')
  const [error, setError] = useState('')
  const [loading, setLoading] = useState(false)

  const expired = new URLSearchParams(location.search).get('expired') === '1'
  const destination = location.state?.from ?? '/dashboard'

  // Si ya hay sesion no tiene sentido mostrar el login.
  if (isAuthenticated()) {
    return <Navigate to={destination} replace />
  }

  async function handleSubmit(e) {
    e.preventDefault()
    if (!username.trim() || !password) {
      setError('Escribe tu usuario y tu contraseña.')
      return
    }
    setError('')
    setLoading(true)
    try {
      await login(username.trim(), password)
      navigate(destination, { replace: true })
    } catch (err) {
      // 403: la cuenta existe pero un administrador todavia no la activa.
      if (err.response?.status === 403) {
        setError('Tu cuenta aún no está activa. Un administrador debe activarla antes de que puedas entrar.')
      } else {
        setError(getErrorMessage(err, 'No se pudo iniciar sesión.'))
      }
      setLoading(false)
    }
  }

  return (
    <AuthLayout>
      <h1 className="mt-10 font-display text-3xl font-semibold tracking-tight lg:mt-0">Iniciar sesión</h1>
      <p className="mt-2 text-ink/70">Ingresa con tu usuario y tu contraseña.</p>

      {expired && (
        <p className="mt-5 rounded-md bg-straw/20 px-3 py-2 text-sm text-ink/80" role="status">
          Tu sesión venció. Inicia sesión de nuevo para continuar.
        </p>
      )}

      {AUTH_MODE === 'demo' && (
        <p className="mt-5 rounded-md border border-straw/60 bg-straw/15 px-3 py-2 text-sm leading-relaxed text-ink/80">
          Modo demostración: usuario <strong>{demoCredentials.username}</strong>, contraseña{' '}
          <strong>{demoCredentials.password}</strong>. Se valida solo en este navegador, no en el servidor.
        </p>
      )}

      <form onSubmit={handleSubmit} className="mt-6 flex flex-col gap-4" noValidate>
        <div>
          <label htmlFor="username" className="text-sm font-medium text-ink/80">
            Usuario
          </label>
          <input
            id="username"
            name="username"
            autoComplete="username"
            autoFocus
            value={username}
            onChange={(e) => setUsername(e.target.value)}
            className={`${inputClass} mt-1`}
          />
        </div>

        <div>
          <label htmlFor="password" className="text-sm font-medium text-ink/80">
            Contraseña
          </label>
          <PasswordInput id="password" value={password} onChange={setPassword} autoComplete="current-password" />
        </div>

        {error && (
          <p className="text-sm text-rose" role="alert">
            {error}
          </p>
        )}

        <PrimaryButton type="submit" disabled={loading}>
          {loading ? 'Ingresando...' : 'Ingresar'}
        </PrimaryButton>
      </form>

      <p className="mt-8 text-sm text-ink/70">
        ¿No tienes cuenta?{' '}
        <Link to="/registro" className="font-medium text-pine hover:underline">
          Crear cuenta
        </Link>
      </p>
      <p className="mt-2 text-sm">
        <Link to="/" className="text-ink/70 hover:text-ink hover:underline">
          Volver al inicio
        </Link>
      </p>
    </AuthLayout>
  )
}

export default Login
