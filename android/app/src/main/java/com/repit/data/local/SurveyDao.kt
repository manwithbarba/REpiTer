package com.repit.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface SurveyDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSurvey(survey: SurveyResponse)

    @Query("SELECT * FROM surveys ORDER BY timestamp DESC")
    fun getAllSurveys(): Flow<List<SurveyResponse>>

    @Query("SELECT * FROM surveys")
    suspend fun getAllSurveysList(): List<SurveyResponse>
}
