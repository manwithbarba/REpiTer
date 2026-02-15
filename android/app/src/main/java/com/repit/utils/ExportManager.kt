package com.repit.utils

import android.content.ContentValues
import android.content.Context
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import com.google.gson.Gson
import com.repit.data.local.SurveyResponse
import java.io.File
import java.io.FileOutputStream
import java.io.OutputStream
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class ExportManager(private val context: Context) {

    fun exportData(surveys: List<SurveyResponse>): String {
        val timestamp = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Date())
        val csvResult = exportToCsv(surveys, "repit_datos_$timestamp.csv")
        val jsonResult = exportToJson(surveys, "repit_datos_$timestamp.json")
        
        return "Exportación finalizada: \n$csvResult\n$jsonResult"
    }

    private fun exportToCsv(surveys: List<SurveyResponse>, fileName: String): String {
        if (surveys.isEmpty()) return "CSV: Sin datos para exportar"

        val sb = StringBuilder()
        // Header
        sb.append("ID,Fecha_Hora,Latitud,Longitud,DatosJSON\n")
        
        for (survey in surveys) {
            sb.append("${survey.id},")
            sb.append("${survey.timestamp},")
            sb.append("${survey.latitude ?: ""},")
            sb.append("${survey.longitude ?: ""},")
            // Escape double quotes in JSON for CSV
            val escapedJson = survey.dataJson.replace("\"", "\"\"")
            sb.append("\"$escapedJson\"\n")
        }

        return saveFile(fileName, sb.toString(), "text/csv")
    }

    private fun exportToJson(surveys: List<SurveyResponse>, fileName: String): String {
        val fhirBundle = mapSurveysToFhirBundle(surveys)
        val jsonString = Gson().newBuilder().setPrettyPrinting().create().toJson(fhirBundle)
        return saveFile(fileName, jsonString, "application/json")
    }

    private fun mapSurveysToFhirBundle(surveys: List<SurveyResponse>): Map<String, Any> {
        val entries = surveys.map { survey ->
            val dataMap = try {
                Gson().fromJson(survey.dataJson, Map::class.java) as Map<String, Any>
            } catch (e: Exception) {
                emptyMap<String, Any>()
            }

            val dni = dataMap["dni"]?.toString() ?: "unknown"
            
            val questionnaireResponse = mutableMapOf<String, Any>(
                "resourceType" to "QuestionnaireResponse",
                "status" to "completed",
                "authored" to SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.getDefault()).format(Date(survey.timestamp)),
                "subject" to mapOf(
                    "identifier" to mapOf(
                        "system" to "http://renaper.gob.ar/dni",
                        "value" to dni
                    )
                ),
                "item" to dataMap.map { (key, value) ->
                    mapOf(
                        "linkId" to key,
                        "answer" to listOf(
                            mapOf(
                                when (value) {
                                    is Boolean -> "valueBoolean" to value
                                    is Number -> "valueDecimal" to value
                                    else -> "valueString" to value.toString()
                                }
                            )
                        )
                    )
                }
            )

            mapOf(
                "fullUrl" to "urn:uuid:${java.util.UUID.randomUUID()}",
                "resource" to questionnaireResponse
            )
        }

        return mapOf(
            "resourceType" to "Bundle",
            "type" to "collection",
            "timestamp" to SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.getDefault()).format(Date()),
            "entry" to entries
        )
    }

    private fun saveFile(fileName: String, content: String, mimeType: String): String {
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                val values = ContentValues().apply {
                    put(MediaStore.MediaColumns.DISPLAY_NAME, fileName)
                    put(MediaStore.MediaColumns.MIME_TYPE, mimeType)
                    put(MediaStore.MediaColumns.RELATIVE_PATH, Environment.DIRECTORY_DOWNLOADS)
                }

                val resolver = context.contentResolver
                val uri = resolver.insert(MediaStore.Downloads.EXTERNAL_CONTENT_URI, values)
                    ?: return "Error creando archivo $fileName"

                resolver.openOutputStream(uri)?.use { outputStream ->
                    outputStream.write(content.toByteArray())
                }
            } else {
                // Legacy storage for older Android versions (Simplified)
                val downloadsDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
                val file = File(downloadsDir, fileName)
                FileOutputStream(file).use { it.write(content.toByteArray()) }
            }
            return "Guardado: $fileName"
        } catch (e: Exception) {
            e.printStackTrace()
            return "Error guardando $fileName: ${e.message}"
        }
    }
}
