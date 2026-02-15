package com.repit.v2

import android.app.Application
import com.repit.v2.data.local.AppDatabase

class RepitApplication : Application() {
    val database by lazy { AppDatabase.getDatabase(this) }
}
