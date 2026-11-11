import { useState } from 'react'
import { Link, Navigate } from 'react-router-dom'
import { isAuthenticated } from '../config/auth'
import { getErrorMessage } from '../config/api'
import { register } from '../services/authService'
import { inputClass } from '../components/Field'
import AuthLayout from '../components/AuthLayout'
import PasswordInput from '../components/PasswordInput'
import PrimaryButton from '../components/PrimaryButton'

const emailPattern = /^[^\s@]+@[^\s@]+\.[^\s@]+$/

function Register() {
  const [username, setUsername] = useState('')
  const [email, setEmail] = useState('')
  const [password, setPassword] = useState('')
  const [confirm, setConfirm] = useState('')
  const [error, setError] = useState('')
  const [loading, setLoading] = useState(false)
  const [done, setDone] = useState(false)

  if (isAuthenticated()) {
    return <Navigate to="/dashboard" replace />
  }

  // Mismas reglas que valida el backend, para avisar antes de enviar.
  function validate() {
    if (username.trim().length < 3 || username.trim().length > 50) {
      return 'El usuario debe tener entre 3 y 50 caracteres.'
    }
    if (!emailPattern.test(email.trim())) {
      return 'Escribe un correo válido.'
    }
    if (password.length < 8 || password.length > 72) {
      return 'La contraseña debe tener entre 8 y 72 caracteres.'
    }
    if (password !== confirm) {
      return 'Las contraseñas no coinciden.'
    }
    return ''
  }

  async function handleSubmit(e) {
    e.preventDefault()
    const problem = validate()
    if (problem) {
      setError(problem)
      return
    }
    setError('')
    setLoading(true)
    try {
      await register({ username: username.trim(), email: email.trim(), password })
      setDone(true)
    } catch (err) {
      setError(getErrorMessage(err, 'No se pudo crear la cuenta.'))
    } finally {
      setLoading(false)
    }
  }

  if (done) {
    return (
      <AuthLayout>
        <h1 className="mt-10 font-display text-3xl font-semibold tracking-tight lg:mt-0">Cuenta creada</h1>
        <p className="mt-3 leading-relaxed text-ink/70">
          Quedó registrada como profesor y está pendiente de aprobación. Un administrador debe activarla antes de que
          puedas iniciar sesión.
        </p>
        <Link
          to="/login"
          className="mt-8 inline-block rounded-md bg-pine px-5 py-3 text-sm font-medium text-white transition-colors hover:bg-pine-dark"
        >
          Ir a iniciar sesión
        </Link>
      </AuthLayout>
    )
  }

  return (
    <AuthLayout>
      <h1 className="mt-10 font-display text-3xl font-semibold tracking-tight lg:mt-0">Crear cuenta</h1>
      <p className="mt-2 leading-relaxed text-ink/70">
        Tu cuenta quedará pendiente hasta que un administrador la active.
      </p>

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
          <label htmlFor="email" className="text-sm font-medium text-ink/80">
            Correo
          </label>
          <input
            id="email"
            name="email"
            type="email"
            autoComplete="email"
            value={email}
            onChange={(e) => setEmail(e.target.value)}
            className={`${inputClass} mt-1`}
          />
        </div>

        <div>
          <label htmlFor="password" className="text-sm font-medium text-ink/80">
            Contraseña
          </label>
          <PasswordInput id="password" value={password} onChange={setPassword} autoComplete="new-password" />
          <p className="mt-1 text-xs text-ink/70">Mínimo 8 caracteres.</p>
        </div>

        <div>
          <label htmlFor="confirm" className="text-sm font-medium text-ink/80">
            Confirmar contraseña
          </label>
          <PasswordInput id="confirm" value={confirm} onChange={setConfirm} autoComplete="new-password" />
        </div>

        {error && (
          <p className="text-sm text-rose" role="alert">
            {error}
          </p>
        )}

        <PrimaryButton type="submit" disabled={loading}>
          {loading ? 'Creando cuenta...' : 'Crear cuenta'}
        </PrimaryButton>
      </form>

      <p className="mt-8 text-sm text-ink/70">
        ¿Ya tienes cuenta?{' '}
        <Link to="/login" className="font-medium text-pine hover:underline">
          Iniciar sesión
        </Link>
      </p>
    </AuthLayout>
  )
}

export default Register
