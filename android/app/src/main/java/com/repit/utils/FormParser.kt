package com.repit.utils

import android.content.Context
import com.google.gson.Gson
import com.repit.data.model.FormConfig
import java.io.IOException

class FormParser(private val context: Context) {

    fun parseConfig(fileName: String = "config.json"): FormConfig? {
        val jsonString: String
        try {
            jsonString = context.assets.open(fileName).bufferedReader().use { it.readText() }
        } catch (ioException: IOException) {
            ioException.printStackTrace()
            return null
        }
        return Gson().fromJson(jsonString, FormConfig::class.java)
    }
}
