package com.repit.v2

import android.app.DatePickerDialog
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.textfield.TextInputEditText
import com.google.gson.Gson
import com.repit.v2.ui.Institution
import com.repit.v2.ui.InstitutionAdapter
import com.repit.v2.utils.InterviewerManager
import java.text.SimpleDateFormat
import java.util.*

class SetupActivity : AppCompatActivity() {

    private lateinit var interviewerManager: InterviewerManager
    private var selectedInstitution: String = ""
    private var refesData: RefesData? = null

    data class RefesData(val provinces: List<Province>)
    data class Province(val name: String, val departments: List<Department>)
    data class Department(val name: String, val establishments: List<Establishment>)
    data class Establishment(val n: String, val s: String) // n = name, s = sector

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_setup)
        
        interviewerManager = InterviewerManager(this)
        loadRefesData()

        val etDate = findViewById<TextInputEditText>(R.id.etDate)
        val etName = findViewById<TextInputEditText>(R.id.etName)
        val etSurname = findViewById<TextInputEditText>(R.id.etSurname)
        val etEmail = findViewById<TextInputEditText>(R.id.etEmail)
        val spinnerProvince = findViewById<Spinner>(R.id.spinnerProvince)
        val spinnerMunicipality = findViewById<Spinner>(R.id.spinnerMunicipality)
        val spinnerSector = findViewById<Spinner>(R.id.spinnerSector)
        val tvInstitution = findViewById<TextView>(R.id.tvInstitutionSelector)
        val btnSave = findViewById<Button>(R.id.btnSave)

        // Sector Spinner Setup
        val sectors = listOf("Todos los Sectores", "Público/Estatal", "Privado", "Privado/Mutual", "Otros")
        spinnerSector.adapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, sectors)

        // Date Picker Logic
        val calendar = Calendar.getInstance()
        val dateFormatter = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
        etDate.setText(dateFormatter.format(calendar.time))

        etDate.setOnClickListener {
            DatePickerDialog(this, { _, year, month, day ->
                calendar.set(year, month, day)
                etDate.setText(dateFormatter.format(calendar.time))
            }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH)).show()
        }

        // Setup REFES Hierarchy
        val provincesList = refesData?.provinces?.map { it.name } ?: emptyList()
        val provinceAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, listOf("Seleccione Provincia...") + provincesList)
        spinnerProvince.adapter = provinceAdapter

        spinnerProvince.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                if (position <= 0) {
                    spinnerMunicipality.adapter = null
                    tvInstitution.text = "Tocar para seleccionar establecimiento..."
                    return
                }
                val selectedProv = refesData?.provinces?.get(position - 1)
                val depts = selectedProv?.departments?.map { it.name } ?: emptyList()
                spinnerMunicipality.adapter = ArrayAdapter(this@SetupActivity, android.R.layout.simple_spinner_dropdown_item, listOf("Seleccione Municipio...") + depts)
            }
            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }

        spinnerMunicipality.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                tvInstitution.text = "Tocar para seleccionar establecimiento..."
                selectedInstitution = ""
            }
            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }

        spinnerSector.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                // Changing sector invalidates selected institution to be safe
                tvInstitution.text = "Tocar para seleccionar establecimiento..."
                selectedInstitution = ""
            }
            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }

        tvInstitution.setOnClickListener {
            val provPos = spinnerProvince.selectedItemPosition
            val muniPos = spinnerMunicipality.selectedItemPosition
            val sector = spinnerSector.selectedItem.toString()

            if (provPos <= 0 || muniPos <= 0) {
                Toast.makeText(this, "Debe seleccionar Provincia y Municipio primero", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val establishments = refesData?.provinces?.get(provPos - 1)?.departments?.get(muniPos - 1)?.establishments ?: emptyList()
            
            // Filter by sector
            val filtered = if (sector == "Todos los Sectores") {
                establishments
            } else {
                establishments.filter { it.s == sector }
            }

            if (filtered.isEmpty()) {
                Toast.makeText(this, "No hay establecimientos en el sector '$sector' para este municipio", Toast.LENGTH_LONG).show()
                return@setOnClickListener
            }

            showInstitutionSearch(filtered, tvInstitution)
        }

        // Pre-fill from previous session
        interviewerManager.getSession()?.let { session ->
            etName.setText(session.name)
            etSurname.setText(session.surname)
            etEmail.setText(session.email)
            etDate.setText(session.realizationDate)
            
            val provinceNames = refesData?.provinces?.map { it.name } ?: emptyList()
            val pIndex = provinceNames.indexOf(session.province)
            if (pIndex >= 0) {
                spinnerProvince.setSelection(pIndex + 1)
                
                // Force population of municipality spinner to allow pre-selection
                val selectedProv = refesData?.provinces?.get(pIndex)
                val depts = selectedProv?.departments?.map { it.name } ?: emptyList()
                spinnerMunicipality.adapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, listOf("Seleccione Municipio...") + depts)
                
                val mIndex = depts.indexOf(session.municipality)
                if (mIndex >= 0) {
                    spinnerMunicipality.setSelection(mIndex + 1)
                }

                // Sector pre-selection
                val sIndex = sectors.indexOf(session.institutionSector)
                if (sIndex >= 0) spinnerSector.setSelection(sIndex)
                
                // Pre-fill institution
                if (session.institution.isNotEmpty()) {
                    tvInstitution.text = session.institution
                    selectedInstitution = session.institution
                    tvInstitution.setTextColor(ContextCompat.getColor(this, android.R.color.black))
                }
            }
        }

        btnSave.setOnClickListener {
            val date = etDate.text.toString()
            val name = etName.text.toString().trim()
            val surname = etSurname.text.toString().trim()
            val email = etEmail.text.toString().trim()
            val province = spinnerProvince.selectedItem?.toString() ?: ""
            val municipality = spinnerMunicipality.selectedItem?.toString() ?: ""
            val sector = spinnerSector.selectedItem?.toString() ?: ""

            if (name.isEmpty() || surname.isEmpty() || email.isEmpty() || province == "Seleccione Provincia..." || municipality == "Seleccione Municipio..." || selectedInstitution.isEmpty()) {
                Toast.makeText(this, "Por favor complete todos los campos", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            interviewerManager.saveSession(
                InterviewerManager.SessionData(date, name, surname, email, province, municipality, selectedInstitution, sector)
            )
            
            startActivity(Intent(this, FormSelectorActivity::class.java))
            finish()
        }
    }

    private fun loadRefesData() {
        try {
            val jsonString = assets.open("refes_data.json").bufferedReader().use { it.readText() }
            refesData = Gson().fromJson(jsonString, RefesData::class.java)
        } catch (e: Exception) {
            e.printStackTrace()
            Toast.makeText(this, "Error cargando datos REFES", Toast.LENGTH_LONG).show()
        }
    }

    private fun showInstitutionSearch(list: List<Establishment>, target: TextView) {
        val dialogView = layoutInflater.inflate(R.layout.dialog_institution_selector, null)
        val etSearch = dialogView.findViewById<EditText>(R.id.etSearchInstitution)
        val rv = dialogView.findViewById<RecyclerView>(R.id.rvInstitutions)

        // Map Establishment objects to the UI Institution model
        val institutions = list.map { Institution(it.n, it.s, "", "Efector") }
        val adapter = InstitutionAdapter(institutions) { selected ->
            target.text = selected.name
            selectedInstitution = selected.name
            target.setTextColor(ContextCompat.getColor(this, android.R.color.black))
        }

        rv.layoutManager = LinearLayoutManager(this)
        rv.adapter = adapter

        val dialog = AlertDialog.Builder(this)
            .setView(dialogView)
            .create()

        etSearch.addTextChangedListener(object : android.text.TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                val filtered = institutions.filter { it.name.lowercase().contains(s.toString().lowercase()) }
                adapter.updateList(filtered)
            }
            override fun afterTextChanged(s: android.text.Editable?) {}
        })

        // Dismiss logic override
        val finalAdapter = InstitutionAdapter(institutions) { selected ->
            target.text = selected.name
            selectedInstitution = selected.name
            target.setTextColor(ContextCompat.getColor(this, android.R.color.black))
            dialog.dismiss()
        }
        rv.adapter = finalAdapter

        dialog.show()
    }
}
