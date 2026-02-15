package com.repit.data

import com.repit.data.local.SurveyDao
import com.repit.data.local.SurveyResponse
import kotlinx.coroutines.flow.Flow

class FormRepository(private val surveyDao: SurveyDao) {

    // Insert a new survey response
    suspend fun saveSurvey(survey: SurveyResponse) {
        surveyDao.insertSurvey(survey)
    }

    // Get all surveys as a Flow for UI updates
    val allSurveys: Flow<List<SurveyResponse>> = surveyDao.getAllSurveys()

    // Get all surveys as a List for Exporting
    suspend fun getAllSurveysList(): List<SurveyResponse> {
        return surveyDao.getAllSurveysList()
    }
}
