package com.repit.v2.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.ColumnInfo

@Entity(tableName = "surveys")
data class SurveyResponse(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    
    @ColumnInfo(name = "timestamp") val timestamp: Long,
    
    @ColumnInfo(name = "latitude") val latitude: Double?,
    
    @ColumnInfo(name = "longitude") val longitude: Double?,
    
    @ColumnInfo(name = "data_json") val dataJson: String,

    @ColumnInfo(name = "interviewer_name") val interviewerName: String?,
    @ColumnInfo(name = "interviewer_surname") val interviewerSurname: String?,
    @ColumnInfo(name = "interviewer_email") val interviewerEmail: String?,
    @ColumnInfo(name = "institution") val institution: String?
)
