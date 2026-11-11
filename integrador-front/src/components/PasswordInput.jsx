import { useState } from 'react'
import { inputClass } from './Field'

function PasswordInput({ id, value, onChange, autoComplete }) {
  const [visible, setVisible] = useState(false)

  return (
    <div className="relative mt-1">
      <input
        id={id}
        name={id}
        type={visible ? 'text' : 'password'}
        autoComplete={autoComplete}
        value={value}
        onChange={(e) => onChange(e.target.value)}
        className={`${inputClass} pr-20`}
      />
      <button
        type="button"
        onClick={() => setVisible((current) => !current)}
        aria-pressed={visible}
        aria-label={visible ? 'Ocultar contraseña' : 'Mostrar contraseña'}
        className="absolute inset-y-0 right-0 px-3 text-sm font-medium text-pine hover:underline"
      >
        {visible ? 'Ocultar' : 'Mostrar'}
      </button>
    </div>
  )
}

export default PasswordInput
