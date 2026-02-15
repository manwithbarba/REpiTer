package com.repit.v2.`data`.local

import androidx.room.EntityInsertAdapter
import androidx.room.RoomDatabase
import androidx.room.coroutines.createFlow
import androidx.room.util.getColumnIndexOrThrow
import androidx.room.util.performSuspending
import androidx.sqlite.SQLiteStatement
import javax.`annotation`.processing.Generated
import kotlin.Double
import kotlin.Int
import kotlin.Long
import kotlin.String
import kotlin.Suppress
import kotlin.Unit
import kotlin.collections.List
import kotlin.collections.MutableList
import kotlin.collections.mutableListOf
import kotlin.reflect.KClass
import kotlinx.coroutines.flow.Flow

@Generated(value = ["androidx.room.RoomProcessor"])
@Suppress(names = ["UNCHECKED_CAST", "DEPRECATION", "REDUNDANT_PROJECTION", "REMOVAL"])
public class SurveyDao_Impl(
  __db: RoomDatabase,
) : SurveyDao {
  private val __db: RoomDatabase

  private val __insertAdapterOfSurveyResponse: EntityInsertAdapter<SurveyResponse>
  init {
    this.__db = __db
    this.__insertAdapterOfSurveyResponse = object : EntityInsertAdapter<SurveyResponse>() {
      protected override fun createQuery(): String =
          "INSERT OR REPLACE INTO `surveys` (`id`,`timestamp`,`latitude`,`longitude`,`data_json`,`interviewer_name`,`interviewer_surname`,`interviewer_email`,`institution`) VALUES (nullif(?, 0),?,?,?,?,?,?,?,?)"

      protected override fun bind(statement: SQLiteStatement, entity: SurveyResponse) {
        statement.bindLong(1, entity.id)
        statement.bindLong(2, entity.timestamp)
        val _tmpLatitude: Double? = entity.latitude
        if (_tmpLatitude == null) {
          statement.bindNull(3)
        } else {
          statement.bindDouble(3, _tmpLatitude)
        }
        val _tmpLongitude: Double? = entity.longitude
        if (_tmpLongitude == null) {
          statement.bindNull(4)
        } else {
          statement.bindDouble(4, _tmpLongitude)
        }
        statement.bindText(5, entity.dataJson)
        val _tmpInterviewerName: String? = entity.interviewerName
        if (_tmpInterviewerName == null) {
          statement.bindNull(6)
        } else {
          statement.bindText(6, _tmpInterviewerName)
        }
        val _tmpInterviewerSurname: String? = entity.interviewerSurname
        if (_tmpInterviewerSurname == null) {
          statement.bindNull(7)
        } else {
          statement.bindText(7, _tmpInterviewerSurname)
        }
        val _tmpInterviewerEmail: String? = entity.interviewerEmail
        if (_tmpInterviewerEmail == null) {
          statement.bindNull(8)
        } else {
          statement.bindText(8, _tmpInterviewerEmail)
        }
        val _tmpInstitution: String? = entity.institution
        if (_tmpInstitution == null) {
          statement.bindNull(9)
        } else {
          statement.bindText(9, _tmpInstitution)
        }
      }
    }
  }

  public override suspend fun insertSurvey(survey: SurveyResponse): Unit = performSuspending(__db,
      false, true) { _connection ->
    __insertAdapterOfSurveyResponse.insert(_connection, survey)
  }

  public override fun getAllSurveys(): Flow<List<SurveyResponse>> {
    val _sql: String = "SELECT * FROM surveys ORDER BY timestamp DESC"
    return createFlow(__db, false, arrayOf("surveys")) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        val _cursorIndexOfId: Int = getColumnIndexOrThrow(_stmt, "id")
        val _cursorIndexOfTimestamp: Int = getColumnIndexOrThrow(_stmt, "timestamp")
        val _cursorIndexOfLatitude: Int = getColumnIndexOrThrow(_stmt, "latitude")
        val _cursorIndexOfLongitude: Int = getColumnIndexOrThrow(_stmt, "longitude")
        val _cursorIndexOfDataJson: Int = getColumnIndexOrThrow(_stmt, "data_json")
        val _cursorIndexOfInterviewerName: Int = getColumnIndexOrThrow(_stmt, "interviewer_name")
        val _cursorIndexOfInterviewerSurname: Int = getColumnIndexOrThrow(_stmt,
            "interviewer_surname")
        val _cursorIndexOfInterviewerEmail: Int = getColumnIndexOrThrow(_stmt, "interviewer_email")
        val _cursorIndexOfInstitution: Int = getColumnIndexOrThrow(_stmt, "institution")
        val _result: MutableList<SurveyResponse> = mutableListOf()
        while (_stmt.step()) {
          val _item: SurveyResponse
          val _tmpId: Long
          _tmpId = _stmt.getLong(_cursorIndexOfId)
          val _tmpTimestamp: Long
          _tmpTimestamp = _stmt.getLong(_cursorIndexOfTimestamp)
          val _tmpLatitude: Double?
          if (_stmt.isNull(_cursorIndexOfLatitude)) {
            _tmpLatitude = null
          } else {
            _tmpLatitude = _stmt.getDouble(_cursorIndexOfLatitude)
          }
          val _tmpLongitude: Double?
          if (_stmt.isNull(_cursorIndexOfLongitude)) {
            _tmpLongitude = null
          } else {
            _tmpLongitude = _stmt.getDouble(_cursorIndexOfLongitude)
          }
          val _tmpDataJson: String
          _tmpDataJson = _stmt.getText(_cursorIndexOfDataJson)
          val _tmpInterviewerName: String?
          if (_stmt.isNull(_cursorIndexOfInterviewerName)) {
            _tmpInterviewerName = null
          } else {
            _tmpInterviewerName = _stmt.getText(_cursorIndexOfInterviewerName)
          }
          val _tmpInterviewerSurname: String?
          if (_stmt.isNull(_cursorIndexOfInterviewerSurname)) {
            _tmpInterviewerSurname = null
          } else {
            _tmpInterviewerSurname = _stmt.getText(_cursorIndexOfInterviewerSurname)
          }
          val _tmpInterviewerEmail: String?
          if (_stmt.isNull(_cursorIndexOfInterviewerEmail)) {
            _tmpInterviewerEmail = null
          } else {
            _tmpInterviewerEmail = _stmt.getText(_cursorIndexOfInterviewerEmail)
          }
          val _tmpInstitution: String?
          if (_stmt.isNull(_cursorIndexOfInstitution)) {
            _tmpInstitution = null
          } else {
            _tmpInstitution = _stmt.getText(_cursorIndexOfInstitution)
          }
          _item =
              SurveyResponse(_tmpId,_tmpTimestamp,_tmpLatitude,_tmpLongitude,_tmpDataJson,_tmpInterviewerName,_tmpInterviewerSurname,_tmpInterviewerEmail,_tmpInstitution)
          _result.add(_item)
        }
        _result
      } finally {
        _stmt.close()
      }
    }
  }

  public override suspend fun getAllSurveysList(): List<SurveyResponse> {
    val _sql: String = "SELECT * FROM surveys"
    return performSuspending(__db, true, false) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        val _cursorIndexOfId: Int = getColumnIndexOrThrow(_stmt, "id")
        val _cursorIndexOfTimestamp: Int = getColumnIndexOrThrow(_stmt, "timestamp")
        val _cursorIndexOfLatitude: Int = getColumnIndexOrThrow(_stmt, "latitude")
        val _cursorIndexOfLongitude: Int = getColumnIndexOrThrow(_stmt, "longitude")
        val _cursorIndexOfDataJson: Int = getColumnIndexOrThrow(_stmt, "data_json")
        val _cursorIndexOfInterviewerName: Int = getColumnIndexOrThrow(_stmt, "interviewer_name")
        val _cursorIndexOfInterviewerSurname: Int = getColumnIndexOrThrow(_stmt,
            "interviewer_surname")
        val _cursorIndexOfInterviewerEmail: Int = getColumnIndexOrThrow(_stmt, "interviewer_email")
        val _cursorIndexOfInstitution: Int = getColumnIndexOrThrow(_stmt, "institution")
        val _result: MutableList<SurveyResponse> = mutableListOf()
        while (_stmt.step()) {
          val _item: SurveyResponse
          val _tmpId: Long
          _tmpId = _stmt.getLong(_cursorIndexOfId)
          val _tmpTimestamp: Long
          _tmpTimestamp = _stmt.getLong(_cursorIndexOfTimestamp)
          val _tmpLatitude: Double?
          if (_stmt.isNull(_cursorIndexOfLatitude)) {
            _tmpLatitude = null
          } else {
            _tmpLatitude = _stmt.getDouble(_cursorIndexOfLatitude)
          }
          val _tmpLongitude: Double?
          if (_stmt.isNull(_cursorIndexOfLongitude)) {
            _tmpLongitude = null
          } else {
            _tmpLongitude = _stmt.getDouble(_cursorIndexOfLongitude)
          }
          val _tmpDataJson: String
          _tmpDataJson = _stmt.getText(_cursorIndexOfDataJson)
          val _tmpInterviewerName: String?
          if (_stmt.isNull(_cursorIndexOfInterviewerName)) {
            _tmpInterviewerName = null
          } else {
            _tmpInterviewerName = _stmt.getText(_cursorIndexOfInterviewerName)
          }
          val _tmpInterviewerSurname: String?
          if (_stmt.isNull(_cursorIndexOfInterviewerSurname)) {
            _tmpInterviewerSurname = null
          } else {
            _tmpInterviewerSurname = _stmt.getText(_cursorIndexOfInterviewerSurname)
          }
          val _tmpInterviewerEmail: String?
          if (_stmt.isNull(_cursorIndexOfInterviewerEmail)) {
            _tmpInterviewerEmail = null
          } else {
            _tmpInterviewerEmail = _stmt.getText(_cursorIndexOfInterviewerEmail)
          }
          val _tmpInstitution: String?
          if (_stmt.isNull(_cursorIndexOfInstitution)) {
            _tmpInstitution = null
          } else {
            _tmpInstitution = _stmt.getText(_cursorIndexOfInstitution)
          }
          _item =
              SurveyResponse(_tmpId,_tmpTimestamp,_tmpLatitude,_tmpLongitude,_tmpDataJson,_tmpInterviewerName,_tmpInterviewerSurname,_tmpInterviewerEmail,_tmpInstitution)
          _result.add(_item)
        }
        _result
      } finally {
        _stmt.close()
      }
    }
  }

  public companion object {
    public fun getRequiredConverters(): List<KClass<*>> = emptyList()
  }
}
