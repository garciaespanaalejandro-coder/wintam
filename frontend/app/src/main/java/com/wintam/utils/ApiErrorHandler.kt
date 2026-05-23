package com.wintam.utils

import okio.IOException
import org.json.JSONObject
import retrofit2.HttpException

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

        val mensaje = backendMessage
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