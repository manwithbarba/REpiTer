package com.repit.v2.utils

import android.content.Context
import android.content.SharedPreferences

class InterviewerManager(context: Context) {
    private val prefs: SharedPreferences = context.getSharedPreferences("interviewer_prefs", Context.MODE_PRIVATE)

    data class InterviewerData(
        val name: String,
        val surname: String,
        val email: String
    )

    fun saveInterviewer(name: String, surname: String, email: String) {
        prefs.edit().apply {
            putString("name", name)
            putString("surname", surname)
            putString("email", email)
            putBoolean("is_setup_complete", true)
            apply()
        }
    }

    fun getInterviewer(): InterviewerData? {
        val name = prefs.getString("name", null) ?: return null
        val surname = prefs.getString("surname", null) ?: return null
        val email = prefs.getString("email", null) ?: return null
        return InterviewerData(name, surname, email)
    }

    fun isSetupComplete(): Boolean {
        return prefs.getBoolean("is_setup_complete", false)
    }

    fun clear() {
        prefs.edit().clear().apply()
    }
}
