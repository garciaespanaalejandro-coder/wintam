package com.wintam.utils

import okio.IOException
import org.json.JSONObject
import retrofit2.HttpException

private val traducciones = mapOf(
    "Bad credentials" to "Email o contraseña incorrectos",
    "El usuario no existe. " to "Email o contraseña incorrectos", // mismo mensaje por seguridad
    "User is disabled" to "Tu cuenta ha sido deshabilitada",
    "must be a well-formed email address" to "El email no tiene un formato válido",
    "must not be blank" to "Este campo es obligatorio",
    "must not be null" to "Este campo es obligatorio",
    "could not execute statement" to "Error al guardar los datos"
)

suspend fun <T> safeApiCall(
    errorMessages: Map<Int, String> = emptyMap(),
    call: suspend () -> T
): Result<T> {
    return try {
        Result.success(call())
    } catch (e: HttpException) {
        val backendMessage = try {
            val json = e.response()?.errorBody()?.string()
            JSONObject(json ?: "").getString("message")
        } catch (ex: Exception) { null }

        val mensajeTraducido = backendMessage?.let { msg ->
            traducciones[msg] ?: traducciones.entries.firstOrNull { msg.contains(it.key) }?.value
        }

        val mensaje = mensajeTraducido
            ?: errorMessages[e.code()]
            ?: when (e.code()) {
                400 -> "Solicitud incorrecta"
                401 -> "No autenticado"
                403 -> "Sin permiso"
                404 -> "No encontrado"
                409 -> "Conflicto con datos existentes"
                else -> "Error inesperado"
            }
        Result.failure(Exception(mensaje))
    } catch (e: IOException) {
        Result.failure(Exception("Sin conexión"))
    }
}