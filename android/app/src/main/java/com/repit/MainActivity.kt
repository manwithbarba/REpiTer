package com.repit.v2

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.ViewGroup
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.ScrollView
import android.widget.Spinner
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import android.text.Editable
import android.text.TextWatcher
import android.widget.TextView
import com.repit.v2.data.model.FormField
import com.repit.v2.ui.DynamicWidgetFactory
import com.repit.v2.ui.MainViewModel
import com.repit.v2.ui.Institution
import com.repit.v2.ui.InstitutionAdapter
import com.repit.v2.utils.InterviewerManager

class MainActivity : AppCompatActivity() {

    private val viewModel: MainViewModel by viewModels()
    private val formContainer: LinearLayout by lazy { LinearLayout(this).apply { orientation = LinearLayout.VERTICAL } }
    private val interviewerManager by lazy { InterviewerManager(this) }
    private var selectedInstitution: String? = null
    
    // Store reference to fields to extract data later
    private val fieldViews = mutableMapOf<String, FormField>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        // Root Layout
        val rootLayout = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            layoutParams = ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT
            )
            setPadding(32, 32, 32, 32)
        }

        // ScrollView for the form
        val scrollView = ScrollView(this).apply {
            layoutParams = LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                0,
                1f
            )
        }
        scrollView.addView(formContainer)
        rootLayout.addView(scrollView)

        // Save Button
        val saveButton = Button(this).apply {
            text = getString(R.string.guardar_encuesta)
            setOnClickListener { attemptSave() }
        }
        rootLayout.addView(saveButton)

        // Export Button
        val exportButton = Button(this).apply {
            text = getString(R.string.exportar_datos)
            setOnClickListener { viewModel.exportData() }
        }
        rootLayout.addView(exportButton)

        setContentView(rootLayout)

        // Observe Config
        viewModel.formConfig.observe(this) { config ->
            config?.let { renderForm(it.fields) }
        }

        // Observe Status
        viewModel.saveStatus.observe(this) { status ->
            Toast.makeText(this, status, Toast.LENGTH_SHORT).show()
        }

        checkPermissions()

        val formFile = intent.getStringExtra("FORM_FILE")
        val formName = intent.getStringExtra("FORM_NAME")
        val actionExport = intent.getBooleanExtra("ACTION_EXPORT", false)

        if (actionExport) {
            viewModel.exportData()
            finish()
            return
        }

        if (formFile != null) {
            title = formName ?: "Encuesta"
            viewModel.loadSelectedConfig(formFile)
        }
    }

    private fun renderForm(fields: List<FormField>) {
        formContainer.removeAllViews()
        fieldViews.clear()
        
        fields.forEach { field ->
            val view = factory.createWidget(field, formContainer)
            if (field.type == "institution") {
                view?.setOnClickListener { showInstitutionDialog(view as TextView) }
            }
            fieldViews[field.id] = field
        }
    }

    private fun attemptSave() {
        val dataMap = mutableMapOf<String, Any>()
        var isValid = true

        for ((id, field) in fieldViews) {
            val view = formContainer.findViewWithTag<android.view.View>(id) ?: continue
            
            var value: Any? = null
            
            when (field.type) {
                "text", "number" -> {
                    val text = (view as EditText).text.toString()
                    if (field.required && text.isBlank()) {
                        view.error = getString(R.string.error_campo_requerido)
                        isValid = false
                    } else if (field.id == "dni" && !text.matches(Regex("^[0-9]{7,8}$"))) {
                        view.error = getString(R.string.error_dni_invalido)
                        isValid = false
                    }
                    value = if (text.isBlank()) null else text
                }
                "boolean" -> {
                    val selected = (view as Spinner).selectedItem.toString()
                    value = when (selected) {
                        "Sí" -> true
                        "No" -> false
                        else -> null
                    }
                    if (field.required && value == null) {
                        isValid = false
                        Toast.makeText(this, "Campo requerido: ${field.label}", Toast.LENGTH_SHORT).show()
                    }
                }
                "multiselect" -> {
                    val selected = (view as Spinner).selectedItem.toString()
                    value = if (selected == "Seleccione...") null else selected
                    if (field.required && value == null) {
                        isValid = false
                        Toast.makeText(this, "Debe seleccionar una opción: ${field.label}", Toast.LENGTH_SHORT).show()
                    }
                }
                "institution" -> {
                    value = (view as TextView).text.toString()
                    if (field.required && (value == "Toque para seleccionar efector..." || value == "")) {
                        isValid = false
                        Toast.makeText(this, "Debe seleccionar un efector", Toast.LENGTH_SHORT).show()
                    }
                    if (value == "Toque para seleccionar efector...") value = null
                }
            }
            
            if (value != null) {
                dataMap[id] = value
            }
        }

        if (isValid) {
            val interviewer = interviewerManager.getInterviewer()
            viewModel.saveSurveyResponse(
                dataMap,
                interviewer?.name,
                interviewer?.surname,
                interviewer?.email,
                selectedInstitution
            )
        } else {
            Toast.makeText(this, "Por favor complete los campos requeridos", Toast.LENGTH_SHORT).show()
        }
    }

    private fun showInstitutionDialog(targetView: TextView) {
        val dialogView = layoutInflater.inflate(R.layout.dialog_institution_selector, null)
        val etSearch = dialogView.findViewById<com.google.android.material.textfield.TextInputEditText>(R.id.etSearchInstitution)
        val rv = dialogView.findViewById<RecyclerView>(R.id.rvInstitutions)

        // Mock data - will be replaced by actual REFES integration
        val allInstitutions = listOf(
            Institution("Hospital Regional Mar del Plata", "Buenos Aires", "General Pueyrredón", "Hospital"),
            Institution("CAPS Centro de Salud N1", "Buenos Aires", "General Pueyrredón", "CAPS"),
            Institution("Hospital Interzonal General de Agudos (HIGA)", "Buenos Aires", "General Pueyrredón", "Hospital"),
            Institution("CAPS Batán", "Buenos Aires", "General Pueyrredón", "CAPS"),
            Institution("CAPS La Peregrina", "Buenos Aires", "General Pueyrredón", "CAPS")
        )

        val adapter = InstitutionAdapter(allInstitutions) { selected ->
            targetView.text = selected.name
            selectedInstitution = "${selected.name} (${selected.municipality})"
            targetView.setTextColor(ContextCompat.getColor(this, R.color.black))
            // Dismiss dialog - we need the dialog reference
        }

        rv.layoutManager = LinearLayoutManager(this)
        rv.adapter = adapter

        val dialog = AlertDialog.Builder(this)
            .setView(dialogView)
            .setCancelable(true)
            .create()

        etSearch.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                val query = s.toString().lowercase()
                val filtered = allInstitutions.filter { 
                    it.name.lowercase().contains(query) || 
                    it.municipality.lowercase().contains(query)
                }
                adapter.updateList(filtered)
            }
            override fun afterTextChanged(s: Editable?) {}
        })

        // Override adapter click to dismiss dialog
        val finalAdapter = InstitutionAdapter(allInstitutions) { selected ->
            targetView.text = selected.name
            selectedInstitution = "${selected.name} (${selected.municipality})"
            targetView.setTextColor(ContextCompat.getColor(this, android.R.color.black))
            dialog.dismiss()
        }
        rv.adapter = finalAdapter

        dialog.show()
    }

    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        // Handle logic if needed
    }

    private fun checkPermissions() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            requestPermissionLauncher.launch(arrayOf(
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ))
        }
    }
}
