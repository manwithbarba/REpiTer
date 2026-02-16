package com.repit.v2.ui

import android.content.Context
import android.text.InputType
import android.view.View
import android.widget.ArrayAdapter
import android.widget.CheckBox
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.Spinner
import android.widget.TextView
import androidx.core.content.ContextCompat
import android.graphics.Typeface
import com.repit.v2.data.model.FormField
import com.repit.v2.R

class DynamicWidgetFactory(private val context: Context) {

    fun createWidget(field: FormField, container: LinearLayout): View? {
        // Handle Header/Separator type
        if (field.type == "header") {
            val header = TextView(context).apply {
                text = field.label
                textSize = 18f
                setTypeface(null, Typeface.BOLD)
                setTextColor(ContextCompat.getColor(context, R.color.purple_700)) // Use a primary color
                layoutParams = LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
                ).apply {
                    setMargins(0, 32, 0, 8)
                }
            }
            container.addView(header)
            return null // Headers are not input fields
        }

        // Label
        val label = TextView(context).apply {
            text = if (field.required) "${field.label} *" else field.label
            textSize = 16f
            layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            ).apply {
                setMargins(0, 16, 0, 4)
            }
        }
        container.addView(label)

        // Input Widget
        val view: View? = when (field.type) {
            "text", "number" -> createEditText(field)
            "boolean" -> createBooleanSpinner(field) // Changed from CheckBox to Spinner per user request
            "multiselect" -> createSpinner(field)
            "gps" -> createGpsPlaceholder(field)
            else -> null
        }

        if (view != null) {
            view.tag = field.id
            view.layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            ).apply {
                setMargins(0, 0, 0, 16)
            }
            container.addView(view)
        }
        return view
    }

    private fun createEditText(field: FormField): EditText {
        return EditText(context).apply {
            // No hint here to avoid redundancy with the TextView label above
            inputType = if (field.type == "number") {
                InputType.TYPE_CLASS_NUMBER or InputType.TYPE_NUMBER_FLAG_DECIMAL
            } else {
                InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_FLAG_CAP_SENTENCES
            }
            // Ensure visibility and clickability
            minHeight = 48 // Accessibility standard
            background = ContextCompat.getDrawable(context, android.R.drawable.edit_text)
        }
    }

    private fun createBooleanSpinner(field: FormField): Spinner {
        return Spinner(context).apply {
            val options = listOf("Seleccione...", "Sí", "No")
            adapter = ArrayAdapter(context, android.R.layout.simple_spinner_dropdown_item, options)
            background = ContextCompat.getDrawable(context, android.R.drawable.btn_default)
        }
    }

    private fun createSpinner(field: FormField): Spinner {
        return Spinner(context).apply {
            val rawOptions = field.options ?: emptyList()
            val options = listOf("Seleccione...") + rawOptions
            adapter = ArrayAdapter(context, android.R.layout.simple_spinner_dropdown_item, options)
            background = ContextCompat.getDrawable(context, android.R.drawable.btn_default)
        }
    }


    private fun createGpsPlaceholder(field: FormField): TextView {
        return TextView(context).apply {
            text = context.getString(R.string.obs_gps)
            textSize = 12f
            isEnabled = false
            layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            ).apply {
                setMargins(0, 4, 0, 8)
            }
        }
    }
}
