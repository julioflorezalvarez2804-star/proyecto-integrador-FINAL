// Error con el mismo formato que devuelve axios, para reutilizar getErrorMessage
// en el modo demo (sin servidor).
export function httpError(status, message) {
  const error = new Error(message)
  error.response = { status, data: { message } }
  return error
}
