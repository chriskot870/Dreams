package org.joyfmi.dreams.database.local

import androidx.room.*

@Dao
interface LocalMeaningDao {

    /*
     * We will want to get the list of all the different categories.
     * There can be multiple occurrences of a Category so we use to
     * DISTINCT to get just one of each different Category. Also,
     * all we want is the Category list
     */
    @Query("SELECT DISTINCT Category FROM LocalMeaning")
    suspend fun getAllCategories(): List<String>

    /*
     * We will want to get the list of all the different Symbols.
     * There can be multiple occurrences of a Symbols so we use to
     * DISTINCT to get just one of each different Symbols. Also,
     * all we want is the Symbol list
     */
    @Query("SELECT DISTINCT Symbol FROM LocalMeaning")
    suspend fun getAllSymbols(): List<String>

    /*
     * Get all symbols by categoryName
     */
    @Query("SELECT DISTINCT Symbol FROM LocalMeaning WHERE Category = :name")
    suspend fun getAllSymbolsByCategoryName(name: String): List<String>

    /*
     * We want to make sure that all of the entries with the same symbol have the same category.
     * This routine returns all the distinct categories given a symbol.
     * It is up to higher level routines to make sure that all entries with the same symbol have
     * the same category.
     * We expect this to return 1 value. If there are more than 1 then something went terribly wrong
     */
   // @Query("SELECT DISTINCT Category FROM LocalMeaning WHERE Symbol = :name")
   // suspend fun getCategoriesBySymbol(name: String): List<String>

    /*
     *
     */
    @Query("SELECT * From LocalMeaning WHERE Symbol = :name")
    suspend fun getMeaningsBySymbolName(name: String): List<LocalMeaning>

    /*
     * Below are the routines that modify the database
     */
    /*
     * This routine inserts a symbol into the Table
     * The OnConflictStrategy.IGNORE strategy ignores a new item if it's primary key is already
     * in the database.
     */
    //@Insert(onConflict = OnConflictStrategy.IGNORE)
    //suspend fun localInsert(symbol: LocalSymbol)

    /*
     * This routine updates an existing symbol entry
     */
    //@Update
    //suspend fun localUpdate(symbol: LocalSymbol)

    /*
     * This routine deletes an existing symbol entry
     */
    //@Delete
    //suspend fun localDelete(symbol: LocalSymbol)

}