package org.joyfmi.dreams.repository

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.launchIn
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import org.joyfmi.dreams.DreamApplication
import org.joyfmi.dreams.database.common.CommonDatabase
import org.joyfmi.dreams.database.local.LocalDatabase
import org.joyfmi.dreams.viewmodels.CategoryViewModel


class DreamRepository(val application: DreamApplication) {

    private var commonDatabase: CommonDatabase = CommonDatabase.getDatabase(application)

    private var localDatabase: LocalDatabase? = LocalDatabase.getDatabase(application)
    private val dreamApp: DreamApplication = application
    private val repositoryScope:CoroutineScope = CoroutineScope(Dispatchers.IO)
    /*
     * This sets the mode. The values are the following:
     * 0: Only want Common Database info
     * 1: Only want Local Database info
     * 2: Want both Common and Local info
     */
    private var mode: Int = 2

    /*
     * We only want one instance of the repository so create a companion object to make sure only
     * one instance gets made
     */
    companion object {
        @Volatile
        private var INSTANCE: DreamRepository? = null

        fun getRepository(application: DreamApplication): DreamRepository {
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

    suspend fun getAllCategories(): Flow<List<CategoryIdentity>> = flow {
        commonDatabase.commonCategoryDao().getAllCategories().collect() {
            val categoryIdentityList: MutableList<CategoryIdentity> = mutableListOf()
            /*
             * For each element use the CommonCategory information to create a Category item
             * Remember we are getting a List of Category items for each it and so we need to
             * look at each Category in it
             */
            it.forEach {
                /*
                 * Create a Category with the values from the CommonCategory
                 * Add the new Category to the List of Category's to be returned
                 */
                    categoryIdentityList.add(CategoryIdentity(it.id, it.name, it.local))
            }
            /*
             * We want to return a List of Categoroy not a MutableList.
             * Since this is the last operation of collect it will be this value that is returned???
             */
            emit(categoryIdentityList.toList())
        }
    }



    fun getMeaningsBySymbolIdentity(symbolIdentity: SymbolIdentity): Flow<List<Meaning>>  = flow {
        /*
         * For the Common Datbase we use the symbolIdentity's id
         */
        commonDatabase.commonMeaningDao().getMeaningsBySymbolId(symbolIdentity.id).collect() {
            val meaningList: MutableList<Meaning> = mutableListOf()
            /*
             * For each element use the CommonCategory information to create a Category item
             * Remember we are getting a List of Category items for each it and so we need to
             * look at each Category in it
             */
            it.forEach {
                /*
                 * Create a Meaning with the values from the CommonMeaning
                 * Add the new Category to the List of Category's to be returned
                 */
                meaningList.add(
                    Meaning(
                        /*
                         * The Meaning doesn't have a name. Just an id and whether it is local.
                         */
                        MeaningIdentity(it.id, it.local),
                        it.reference,
                        it.contents,
                        /*
                         * The common meanings were gotten from the symbolIdentity input parameter.
                         * So we know that is the symbolIdentity that belongs with the meaning.
                         */
                        symbolIdentity
                    )
                )
            }
            /*
             * We want to return a List of Categoroy not a MutableList.
             * Since this is the last operation of collect it will be this value that is returned???
             */
            emit(meaningList.toList())
        }
    }

    suspend fun symbolIdentitiesByCategoryIdentity(categoryIdentity: CategoryIdentity): Flow<List<SymbolIdentity>> = flow {
        commonDatabase.commonSymbolDao().getSymbolNamesByCategoryId(categoryIdentity.id).collect() {
            val symbolList: MutableList<SymbolIdentity> = mutableListOf()
            /*
             * For each element use the CommonCategory information to create a Category item
             * Remember we are getting a List of Category items for each it and so we need to
             * look at each Category in it
             */
            it.forEach {
                /*
                 * Create a Symbol with the values from the CommonSymbol
                 * Add the new Symbol to the List of Symbols to be returned
                 */
                symbolList.add(SymbolIdentity(it.id, it.name, it.local))
            }
            /*
             * We want to return a List of Categoroy not a MutableList.
             * Since this is the last operation of collect it will be this value that is returned???
             */
            emit(symbolList.toList())
        }
    }
}

class CommonCategoryFactory(
    private val repository: DreamRepository
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(CategoryViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return CategoryViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}