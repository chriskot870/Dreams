package org.joyfmi.dreams

import android.app.Application
import org.joyfmi.dreams.database.common.CommonDatabase
import org.joyfmi.dreams.database.local.LocalDatabase
import org.joyfmi.dreams.repository.DreamRepository

class DreamApplication : Application() {
    /*
     * I don't need these databases. I'll be using the repository instead
     */
    val commonDatabase: CommonDatabase by lazy { CommonDatabase.getDatabase(this) }
    val localDatabase: LocalDatabase by lazy { LocalDatabase.getDatabase(this)}

    val repository: DreamRepository by lazy { DreamRepository.getRepository(this)}
}