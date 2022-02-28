package org.joyfmi.dreams.repository

import android.app.Application
import kotlinx.coroutines.flow.Flow
import org.joyfmi.dreams.database.common.CommonDatabase
import org.joyfmi.dreams.database.local.LocalDatabase


class DreamRepository(application: Application) {

    private var commonDatabase: CommonDatabase? = CommonDatabase.getDatabase(application)
    private var localDatabase: LocalDatabase? = LocalDatabase.getDatabase(application)

    /*
     * We only want one instance of the repository so create a companion object to make sure only
     * one instance gets made
     */
    companion object {
        @Volatile
        private var INSTANCE: DreamRepository? = null

        fun getRepository(application: Application): DreamRepository {
            return INSTANCE ?: synchronized(this) {
                val instance = DreamRepository(application)
                INSTANCE = instance

                instance
            }
        }
    }
    /*
     * Here we provide the functions to be accessible from the ViewModel to pass data between the
     * ViewModel and the Databases.
     */

    fun getAllCategories(): Flow<List<Category>>? {
        val dummy: Flow<List<Category>>? = null
        return dummy
    }

    fun getMeaningsBySymbolName(symbol: String): Flow<List<Meaning>>? {
        val dummy: Flow<List<Meaning>>? = null
        return dummy
    }

    fun symbolsByCategoryId(categoryId: Int): Flow<List<Symbol>>? {
        val dummy: Flow<List<Symbol>>? = null
        return dummy
    }
}