package com.repit.v2.utils

import android.content.Context
import android.content.SharedPreferences

class InterviewerManager(context: Context) {
    private val prefs: SharedPreferences = context.getSharedPreferences("interviewer_prefs", Context.MODE_PRIVATE)

    data class SessionData(
        val realizationDate: String,
        val name: String,
        val surname: String,
        val email: String,
        val province: String,
        val municipality: String,
        val institution: String,
        val institutionSector: String
    )

    fun saveSession(data: SessionData) {
        prefs.edit().apply {
            putString("realization_date", data.realizationDate)
            putString("name", data.name)
            putString("surname", data.surname)
            putString("email", data.email)
            putString("province", data.province)
            putString("municipality", data.municipality)
            putString("institution", data.institution)
            putString("institution_sector", data.institutionSector)
            putBoolean("is_setup_complete", true)
            apply()
        }
    }

    fun getSession(): SessionData? {
        val date = prefs.getString("realization_date", null) ?: return null
        val name = prefs.getString("name", null) ?: return null
        val surname = prefs.getString("surname", null) ?: return null
        val email = prefs.getString("email", null) ?: return null
        val province = prefs.getString("province", null) ?: ""
        val municipality = prefs.getString("municipality", null) ?: ""
        val institution = prefs.getString("institution", null) ?: ""
        val sector = prefs.getString("institution_sector", null) ?: ""
        
        return SessionData(date, name, surname, email, province, municipality, institution, sector)
    }

    fun isSetupComplete(): Boolean {
        return prefs.getBoolean("is_setup_complete", false)
    }

    fun clear() {
        prefs.edit().clear().apply()
    }
}
