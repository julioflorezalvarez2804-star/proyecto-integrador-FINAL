const options = [
  { value: 'PRESENT', label: 'Presente', checked: 'peer-checked:bg-pine peer-checked:text-white' },
  { value: 'ABSENT', label: 'Ausente', checked: 'peer-checked:bg-rose peer-checked:text-white' },
  { value: 'LATE', label: 'Tarde', checked: 'peer-checked:bg-straw peer-checked:text-ink' },
  { value: 'EXCUSED', label: 'Excusado', checked: 'peer-checked:bg-harbor-dark peer-checked:text-white' },
]

// Cuatro opciones de las que se elige una. Usa radios reales (se pueden usar con el teclado).
function AttendanceStatusPicker({ name, value, onChange, label }) {
  return (
    <div role="radiogroup" aria-label={label} className="inline-flex overflow-hidden rounded-lg border border-line bg-white">
      {options.map((option, index) => (
        <label key={option.value} className={`cursor-pointer ${index > 0 ? 'border-l border-line' : ''}`}>
          <input
            type="radio"
            name={name}
            value={option.value}
            checked={value === option.value}
            onChange={() => onChange(option.value)}
            className="peer sr-only"
          />
          <span
            className={`block px-3 py-1.5 text-sm text-ink/80 transition-colors peer-checked:font-medium peer-focus-visible:outline-2 peer-focus-visible:-outline-offset-2 peer-focus-visible:outline-pine ${option.checked}`}
          >
            {option.label}
          </span>
        </label>
      ))}
    </div>
  )
}

export default AttendanceStatusPicker
