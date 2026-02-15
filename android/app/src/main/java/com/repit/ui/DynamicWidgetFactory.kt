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
import com.repit.v2.data.model.FormField
import com.repit.v2.R

class DynamicWidgetFactory(private val context: Context) {

    fun createWidget(field: FormField, container: LinearLayout): View? {
        // Label
        val label = TextView(context).apply {
            text = if (field.required) "${field.label} *" else field.label
            textSize = 16f
            layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            ).apply {
                setMargins(0, 16, 0, 8)
            }
        }
        container.addView(label)

        // Input Widget
        val view: View? = when (field.type) {
            "text", "number" -> createEditText(field)
            "boolean" -> createCheckBox(field)
            "multiselect" -> createSpinner(field)
            "gps" -> createGpsPlaceholder(field) // GPS is background, but we might show a label
            else -> null
        }

        if (view != null) {
            // Assign the ID from config as the View's tag to easily retrieve it later
            view.tag = field.id
            container.addView(view)
        }
        return view
    }

    private fun createEditText(field: FormField): EditText {
        return EditText(context).apply {
            hint = field.label
            inputType = if (field.type == "number") {
                InputType.TYPE_CLASS_NUMBER
            } else {
                InputType.TYPE_CLASS_TEXT
            }
        }
    }

    private fun createCheckBox(field: FormField): CheckBox {
        return CheckBox(context).apply {
            text = "Sí"
        }
    }

    private fun createSpinner(field: FormField): Spinner {
        return Spinner(context).apply {
            val options = field.options ?: emptyList()
            adapter = ArrayAdapter(context, android.R.layout.simple_spinner_dropdown_item, options)
        }
    }

    private fun createGpsPlaceholder(field: FormField): TextView {
        return TextView(context).apply {
            text = context.getString(R.string.obs_gps)
            isEnabled = false
        }
    }
}
