package com.repit.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.ColumnInfo

@Entity(tableName = "surveys")
data class SurveyResponse(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    
    @ColumnInfo(name = "timestamp") val timestamp: Long,
    
    @ColumnInfo(name = "latitude") val latitude: Double?,
    
    @ColumnInfo(name = "longitude") val longitude: Double?,
    
    @ColumnInfo(name = "data_json") val dataJson: String // Stores the full form response as specific implementations might change
)
