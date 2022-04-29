package org.joyfmi.dreams.repository

import android.graphics.Color
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.withContext
import org.joyfmi.dreams.DreamApplication
import org.joyfmi.dreams.database.common.CommonDatabase
import org.joyfmi.dreams.database.common.CommonSymbol
import org.joyfmi.dreams.database.local.LocalDatabase
import org.joyfmi.dreams.database.local.LocalMeaning
/*
 * Values to represent the Common or the Local Database
 */
const val DB_COMMON = 0
const val DB_LOCAL = 1
/*
 * Define which DB should be used
 */
const val DB_COMMON_ONLY = 0
const val DB_LOCAL_ONLY = 1
const val DB_COMMON_AND_LOCAL = 2
/*
 * It's optional to sort the Local ones first or the Common first
 * or mix them.
 * The mode variable determines:
 */
const val DB_SORT_MIXED = 0
const val DB_SORT_COMMON_FIRST = 1
const val DB_SORT_LOCAL_FIRST = 2

class DreamRepository(application: DreamApplication) {

    private var commonDatabase: CommonDatabase = CommonDatabase.getDatabase(application)
    private var localDatabase: LocalDatabase = LocalDatabase.getDatabase(application)
    //private val dreamApp: DreamApplication = application
    //private val repositoryScope: CoroutineScope = CoroutineScope(Dispatchers.IO)

    /*
     * We only want one instance of the repository so create a companion object to make sure only
     * one instance gets made
     */
    companion object {
        @Volatile
        private var INSTANCE: DreamRepository? = null

        /*
         * This sets the mode. The values are the following:
         * 0: Only want Common Database info
         * 1: Only want Local Database info
         * 2: Want both Common and Local info
         */
        var dbMode: Int = DB_COMMON_AND_LOCAL
        var dbSortMode: Int = DB_SORT_MIXED

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

    suspend fun getAllCategories(ioDispatcher: CoroutineDispatcher = Dispatchers.IO): List<CategoryIdentity> {
        /*
         * We need to go into the main thread in order to update the symbolAdapter List
         */
        return withContext(ioDispatcher) {
            val categoryIdentityList: MutableList<CategoryIdentity> = mutableListOf()
            /*
             * If we want the common list then go get it
             */
            if (dbMode != DB_LOCAL_ONLY) {
                val common = getCommonCategories()
                categoryIdentityList.addAll(common)
            }
            /*
             * If we want the local list then go get it
             */
            if (dbMode != DB_COMMON_ONLY) {
                val localIdentities = getAllLocalCategories()
                /*
                 * We only want to add local categories that don't have the
                 * same name as a common category that is already on the list
                 */
                outerLoop@ for (localIdentity in localIdentities.iterator()) {
                    /*
                     * Walk through all the local categories and check the name against all
                     * the common categories already on the list
                     */
                    for (commonIdentity in categoryIdentityList.iterator()) {
                        if (commonIdentity.name.equals(localIdentity.name, ignoreCase = true) ) {
                            /*
                             * There is already a common categoryIdentity on the list so
                             * don't add the local identity.
                             * Go to the next local identity
                             */
                            continue@outerLoop
                        }
                        /*
                         * If I made it to here the local name doesn't match any common names.
                         * So, add it to the list.
                         */
                    }
                    /*
                     * If I get here then there was no match so add the category to the list
                     */
                    categoryIdentityList.add(localIdentity)
                }
            }
            /*
             * Now the list has been created so sort it
             */
            categoryIdentityList.sortWith(CategoryIdentityComparator)
            /*
             * We want to pass back a List not a MutableList
             */
            categoryIdentityList.toList()
        }
    }

    private suspend fun getCommonCategories(): MutableList<CategoryIdentity> {

        val categoryIdentityList: MutableList<CategoryIdentity> = mutableListOf()
        val commons = commonDatabase.commonCategoryDao().getAllCategories()

        /*
         * For each element use the CommonCategory information to create a Category item
         * Remember we are getting a List of Category items for each it and so we need to
         * look at each Category in it
         */
        commons.forEach { common ->
            /*
             * Create a Category with the values from the CommonCategory
             * Add the new Category to the List of Category's to be returned
             */
            categoryIdentityList.add(CategoryIdentity(common.id, common.name, common.local))
        }
        /*
         * We want to return a List of Category not a MutableList.
         * We also want to source using the CategoryIdentityComparator
         */
        categoryIdentityList.sortWith(CategoryIdentityComparator)
        return (categoryIdentityList)
    }

    private suspend fun getAllLocalCategories(): MutableList<CategoryIdentity> {
        val categoryIdentityList: MutableList<CategoryIdentity> = mutableListOf()
        lateinit var names: List<String>
        try {
            names = localDatabase.localMeaningDao().getAllCategories()
        } catch( e: Exception) {
            val error = e.message
        }
        /*
         * For each element use the LocalCategory information to create a Category item
         */
        names.forEach { name ->
            /*
             * Create a Category Identity
             * Local Databases always have an id of 0 and a local value of 1
             */
            categoryIdentityList.add(CategoryIdentity(0, name, DB_LOCAL))
        }
        /*
         * We want to return a List of Category not a MutableList.
         * We also want to source using the CategoryIdentityComparator
         */
        categoryIdentityList.sortWith(CategoryIdentityComparator)
        return(categoryIdentityList)
    }

    suspend fun getAllSymbolNames(ioDispatcher: CoroutineDispatcher = Dispatchers.IO):Array<String> {
        /*
         * Run this in the Dispatchers.IO scope
         * This will return the symbolNames.toTypedArray()
         */
        return withContext(ioDispatcher) {
            val symbolNames: MutableList<String> = mutableListOf()
            val symbolList = symbolIdentities()
            symbolList.forEach { symbolIdentity ->
                symbolNames.add(symbolIdentity.toString())
            }
            symbolNames.toTypedArray()
        }
    }

    /*
     * Get the symbolIdentites.
     * If no parameter is specified assume you want all of them.
     * If a CategoryIdentity is provided use it to filter the symbols that have that as a category
     */
    suspend fun symbolIdentities(
        categoryIdentity: CategoryIdentity? = null,
        ioDispatcher: CoroutineDispatcher = Dispatchers.IO
        ): List<SymbolIdentity> {

        return withContext(ioDispatcher) {
            val symbolIdentityList: MutableList<SymbolIdentity> = mutableListOf()
            /*
             * If we want the common list then go get it
             */
            if (dbMode != DB_LOCAL_ONLY) {
                val common = commonSymbolIdentitiesByCategoryIdentity(categoryIdentity)
                /*
                 * All the common categories should go onto the final list
                 */
                symbolIdentityList.addAll(common)
            }
            /*
             * If we want the local list then go get it
             */
            var localIdentityList: MutableList<SymbolIdentity> = mutableListOf()
            if (dbMode != DB_COMMON_ONLY) {
                val localList: MutableList<SymbolIdentity> = mutableListOf()
                lateinit var locals: List<SymbolIdentity>
                try {
                    locals = localSymbolIdentitiesByCategoryIdentity(categoryIdentity)
                } catch(e: Exception) {
                    val msg = e.message
                }
                /*
                 * We only want to add local categories that don't have the
                 * same name as a common category that is already on the list
                 */
                symbolCheckLoop@for (local in locals.iterator()) {
                    /*
                     * Walk through all the local categories and check the name against all
                     * the common categories already on the list
                     */
                    for (symbolIdentity in symbolIdentityList.iterator()) {
                        if (local.name.equals(symbolIdentity.name, ignoreCase = true)) {
                            /*
                             * There is already a common categoryIdentity on the list so
                             * don't add the local identity.
                             * Go to the next local identity
                             */
                            continue@symbolCheckLoop
                        }
                    }
                    /*
                     * If I made it to here the local name doesn't match any common names.
                     * So, add it to the list.
                     */
                    localList.add(local)
                }
                /*
                 * ANy symbols just on the local list should be added to symbolIdentityList
                 */
                symbolIdentityList.addAll(localList)
            }

            /*
             * Now the list has been created so sort it
             */
            symbolIdentityList.sortWith(SymbolIdentityComparator)
            /*
             * We want to pass back a List not a MutableList
             */
            symbolIdentityList.toList()
        }
    }

    private suspend fun commonSymbolIdentitiesByCategoryIdentity(categoryIdentity: CategoryIdentity?): List<SymbolIdentity> {
        /*
         * The All option will have an id of 1 and local will be 0.
         * If that is the option then get all symbols.
         */
        val commons: List<CommonSymbol> =
            if (categoryIdentity == null || (categoryIdentity.id == 1 && categoryIdentity.local == DB_COMMON) ) {
                /*
                 * It is a request for All symbols so use getAllSymbols
                 */
                commonDatabase.commonSymbolDao().getAllSymbols()
            } else {
                commonDatabase.commonSymbolDao().getSymbolNamesByCategoryId(categoryIdentity.id)
            }

        val symbolList: MutableList<SymbolIdentity> = mutableListOf()
        /*
         * For each element use the CommonCategory information to create a Category item
         * Remember we are getting a List of Category items for each it and so we need to
         * look at each Category in it
         */
        commons.forEach {
            /*
             * Create a Symbol with the values from the CommonSymbol
             * Add the new Symbol to the List of Symbols to be returned
             */
            symbolList.add(SymbolIdentity(it.id, it.name, it.local))
        }
        symbolList.sortWith(SymbolIdentityComparator)
        return(symbolList.toList())
    }

    private suspend fun localSymbolIdentitiesByCategoryIdentity(categoryIdentity: CategoryIdentity?): List<SymbolIdentity> {
        /*
         * The All option will have an id of 1 and local will be 0.
         * If that is the option then get all symbols.
         */
        val locals: List<String> =
            if (categoryIdentity == null || (categoryIdentity.id == 1 && categoryIdentity.local == DB_COMMON)) {
            /*
             * It is a request for All symbols so use getAllSymbols
             */
            localDatabase.localMeaningDao().getAllSymbols()
        } else {
            localDatabase.localMeaningDao().getAllSymbolsByCategoryName(categoryIdentity.name)
        }
        /*
         * Now go and get the info from the database
         */
        val symbolList: MutableList<SymbolIdentity> = mutableListOf()
        /*
         * For each element use the CommonCategory information to create a Category item
         * Remember we are getting a List of Category items for each it and so we need to
         * look at each Category in it
         */
        locals.forEach { local ->
            /*
             * Create a SymbolIdentity
             * For local symbols id is always 0
             * For local symbols local is always 1
             */
            symbolList.add(SymbolIdentity(0, local, DB_LOCAL))
        }
        symbolList.sortWith(SymbolIdentityComparator)

        return(symbolList)
    }

    suspend fun meaningsBySymbolIdentity(
        symbolIdentity: SymbolIdentity,
        ioDispatcher: CoroutineDispatcher = Dispatchers.IO): List<Meaning> {

        return withContext(ioDispatcher) {
            val meaningList: MutableList<Meaning> = mutableListOf()
            /*
             * If we want the common list then go get it
             */
            if (dbMode != DB_LOCAL_ONLY) {
                val commons = commonMeaningsBySymbolIdentity(symbolIdentity)
                /*
                 * All the common categories should go onto the final list
                 */
                meaningList.addAll(commons)
            }
            /*
             * If we want the local list then go get it
             */
            if (dbMode != DB_COMMON_ONLY) {
                val locals = localMeaningsBySymbolIdentity(symbolIdentity)
                /*
                 * Add the meanings from local to the list
                 */
                for (local in locals.iterator()) {
                    meaningList.add(local)
                }
            }
            /*
             * We want to pass back a List not a MutableList
             */
            meaningList.toList()
        }
    }

    private suspend fun commonMeaningsBySymbolIdentity(symbolIdentity: SymbolIdentity): MutableList<Meaning> {
        val meaningList: MutableList<Meaning> = mutableListOf()
        /*
         * For the Common Database we use the symbolIdentity's id
         */
        val commons = commonDatabase.commonMeaningDao().getMeaningsBySymbolId(symbolIdentity.id)
        /*
         * For each element use the CommonCategory information to create a Category item
         * Remember we are getting a List of Category items for each it and so we need to
         * look at each Category in it
         */
         commons.forEach { common ->
             /*
              * Create a Meaning with the values from the CommonMeaning
              * Add the new Category to the List of Category's to be returned
              */
             meaningList.add(
                 Meaning(
                     /*
                      * The Meaning doesn't have a name. Just an id and whether it is local.
                      */
                     MeaningIdentity(common.id, common.local),
                     common.reference,
                     common.contents,
                     /*
                      * The common meanings were gotten from the symbolIdentity input parameter.
                      * So we know that is the symbolIdentity that belongs with the meaning.
                      */
                     symbolIdentity
                 )
             )
         }
        /*
         * We want to return a List of Category not a MutableList.
         * Since this is the last operation of collect it will be this value that is returned???
         */
        return(meaningList)
    }

    private suspend fun localMeaningsBySymbolIdentity(symbolIdentity: SymbolIdentity): MutableList<Meaning> {
        val meaningsList: MutableList<Meaning> = mutableListOf()
        val meanings = localDatabase.localMeaningDao().getMeaningsBySymbolName(symbolIdentity.name)
        /*
         * For each element use the CommonCategory information to create a Category item
         * Remember we are getting a List of Category items for each it and so we need to
         * look at each Category in it
         */
        meanings.forEach { meaning ->
            /*
             * Create a Category Identity
             * Local Databases always have an id of 0 and a local value of 1
             */
            meaningsList.add(Meaning(
                MeaningIdentity(meaning.id, meaning.local),
                meaning.reference,
                meaning.contents,
                symbolIdentity))
        }
        /*
         * We want to return a List of Category not a MutableList.
         * We also want to source using the CategoryIdentityComparator
         */
        return(meaningsList)
    }

    suspend fun localInsert(newRecord: LocalMeaning) {
        localDatabase.localMeaningDao().localInsert(newRecord)
    }

    suspend fun localUpdate(record: LocalMeaning) {
        localDatabase.localMeaningDao().localUpdate(record)
    }

    suspend fun localDelete(record: LocalMeaning) {
        localDatabase.localMeaningDao().localDelete(record)
    }

}

class SymbolIdentityComparator {
    companion object : Comparator<SymbolIdentity> {
        override fun compare(a: SymbolIdentity, b: SymbolIdentity): Int {
            /*
             * It's optional to sort the Local ones first or the Common first
             * or mix them.
             * The mode variable determines:
             * mode = 0 Mixed
             * mode = 1 Common First
             * mode = 2 Local First
             */
            when (DreamRepository.dbSortMode) {
                DB_SORT_COMMON_FIRST -> if (a.local != b.local) return a.local.compareTo(b.local)
                DB_SORT_LOCAL_FIRST -> if (a.local != b.local) return b.local.compareTo(a.local)
            }
            /*
             * Either Mode is mixed or both have same local value
             */
            val aIsNumeric = a.name.matches("-?\\d+(\\.\\d+)?".toRegex())
            val bIsNumeric = b.name.matches("-?\\d+(\\.\\d+)?".toRegex())
            /*
             * Any numeric value is less than any non-numeric value
             */
            if (aIsNumeric && !bIsNumeric) return 1
            if (!aIsNumeric && bIsNumeric) return -1
            if (aIsNumeric && bIsNumeric) return a.name.toDouble().compareTo(b.name.toDouble())
            /*
             * The last possibility is that neither can be converted to a number.
             * So, compare the items directly
             */
            return a.name.compareTo(b.name, ignoreCase = true)
        }
    }
}

class CategoryIdentityComparator {
    companion object : Comparator<CategoryIdentity> {
        override fun compare(a: CategoryIdentity, b: CategoryIdentity): Int {
            /*
             * It's optional to sort the Local ones first or the Common first
             * or mix them.
             * The mode variable determines:
             * mode = 0 Mixed
             * mode = 1 Common First
             * mode = 2 Local First
             */
            when (DreamRepository.dbSortMode) {
                DB_SORT_COMMON_FIRST -> if (a.local != b.local) return a.local.compareTo(b.local)
                DB_SORT_LOCAL_FIRST -> if (a.local != b.local) return b.local.compareTo(a.local)
            }
            /*
             * Either Mode is mixed or both have same local value
             */
            val aIsNumeric = a.name.matches("-?\\d+(\\.\\d+)?".toRegex())
            val bIsNumeric = b.name.matches("-?\\d+(\\.\\d+)?".toRegex())
            /*
             * Any numeric value is less than any non-numeric value
             */
            if (aIsNumeric && !bIsNumeric) return -1
            if (!aIsNumeric && bIsNumeric) return 1
            if (aIsNumeric && bIsNumeric) return a.name.toDouble().compareTo(b.name.toDouble())
            /*
             * The last possibility is that neither can be converted to a number.
             * So, compare the items directly
             */
            return a.name.compareTo(b.name, ignoreCase = true)
        }
    }
}

