package org.joyfmi.dreams

import android.app.Application
import org.joyfmi.dreams.database.AppDatabase

class DreamApplication : Application() {
    val database: AppDatabase by lazy { AppDatabase.getDatabase(this) }
}