package com.wintam.data

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name="wintam_prefs")

class TokenManager(private val context: Context){
    companion object{
        val TOKEN_KEY= stringPreferencesKey("jwt_token")
        val USERNAME_KEY= stringPreferencesKey("username")
        val ROLE_KEY= stringPreferencesKey("role")
    }

    val token: Flow<String?> = context.dataStore.data.map { it[TOKEN_KEY] }
    val username: Flow<String?> = context.dataStore.data.map { it[USERNAME_KEY]}
    val role: Flow<String?> = context.dataStore.data.map { it[ROLE_KEY] }

    suspend fun saveAuthData(token: String, username: String, role:String){
        context.dataStore.edit { prefs ->
            prefs[TOKEN_KEY]= token
            prefs[USERNAME_KEY]= username
            prefs[ROLE_KEY]= role
        }
    }

    suspend fun clearAuthData() {
        context.dataStore.edit { it.clear() }
    }
}