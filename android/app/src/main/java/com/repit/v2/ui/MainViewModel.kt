package com.repit.v2.ui

import android.app.Application
import android.location.Location
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.repit.v2.RepitApplication
import com.repit.v2.data.FormRepository
import com.repit.v2.data.local.SurveyResponse
import com.repit.v2.data.model.FormConfig
import com.repit.v2.utils.FormParser
import com.repit.v2.utils.LocationHelper
import com.google.gson.Gson
import kotlinx.coroutines.launch

class MainViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: FormRepository
    private val locationHelper: LocationHelper

    private val _formConfig = MutableLiveData<FormConfig?>()
    val formConfig: LiveData<FormConfig?> = _formConfig

    private val _saveStatus = MutableLiveData<String>()
    val saveStatus: LiveData<String> = _saveStatus

    init {
        val surveyDao = (application as RepitApplication).database.surveyDao()
        repository = FormRepository(surveyDao)
        locationHelper = LocationHelper(application)
    }

    fun loadSelectedConfig(fileName: String) {
        // Load specific config from assets
        val parser = FormParser(getApplication())
        _formConfig.value = parser.parseConfig(fileName)
    }

    fun saveSurveyResponse(
        dataMap: Map<String, Any>,
        interviewerName: String?,
        interviewerSurname: String?,
        interviewerEmail: String?,
        institution: String?,
        realizationDate: String?,
        province: String?,
        municipality: String?
    ) {
        viewModelScope.launch {
            _saveStatus.value = "Obteniendo ubicación..."
            
            // 1. Capture Location (Blind)
            val location: Location? = locationHelper.getCurrentLocation()
            
            // 2. Serialize Data
            val dataJson = Gson().toJson(dataMap)
            
            // 3. Create Entity
            val survey = SurveyResponse(
                timestamp = System.currentTimeMillis(),
                latitude = location?.latitude,
                longitude = location?.longitude,
                dataJson = dataJson,
                interviewerName = interviewerName,
                interviewerSurname = interviewerSurname,
                interviewerEmail = interviewerEmail,
                institution = institution,
                realizationDate = realizationDate,
                province = province,
                municipality = municipality
            )
            
            // 4. Save to DB
            repository.saveSurvey(survey)
            
            _saveStatus.value = "Guardado exitosamente (Lat: ${location?.latitude ?: "N/A"})"
        }
    }

    fun exportData() {
        viewModelScope.launch {
            _saveStatus.value = "Exportando datos..."
            val surveys = repository.getAllSurveysList()
            val exportManager = com.repit.v2.utils.ExportManager(getApplication())
            val result = exportManager.exportData(surveys)
            _saveStatus.value = result
        }
    }
}
