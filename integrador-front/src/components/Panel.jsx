// Tarjeta con titulo, usada en el dashboard.
function Panel({ title, children }) {
  return (
    <section className="rounded-xl border border-line bg-white p-5">
      <h2 className="font-display text-lg font-semibold">{title}</h2>
      <div className="mt-4">{children}</div>
    </section>
  )
}

export default Panel
