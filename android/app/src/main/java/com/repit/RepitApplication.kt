package com.repit

import android.app.Application
import com.repit.data.local.AppDatabase

class RepitApplication : Application() {
    val database by lazy { AppDatabase.getDatabase(this) }
}
