package org.joyfmi.dreams.database.local

import androidx.room.*
import kotlinx.coroutines.flow.Flow


@Dao
interface LocalSymbolDao {
    /*
     * This gets a specific local symbol
     */
    @Query("SELECT * FROM LocalSymbol WHERE Id = :id")
    fun getSymbolById(id: Int): Flow<LocalSymbol>
    /*
     * It is possible for their to be multiple Meanings for a symbol so get all
     * the entries with the given symbol
     */
    @Query("SELECT * FROM LocalSymbol WHERE Name = :name")
    fun getAllSymbolsByName(name: String): Flow<List<LocalSymbol>>
    /*
     * We will want to get the list of all the different categories.
     * There can be multiple occurrences of a Category so we use to
     * DISTINCT to get just one of each different Category. Also,
     * all we want is the Category list
     */
    @Query("SELECT DISTINCT Category FROM LocalSymbol")
    fun getAllCategories(): Flow<List<String>>

    /*
     * We will want to get the list of all the different Symbols.
     * There can be multiple occurrences of a Symbols so we use to
     * DISTINCT to get just one of each different Symbols. Also,
     * all we want is the Symbol list
     */

    @Query("SELECT DISTINCT Name FROM LocalSymbol")
    fun getAllSymbols(): Flow<List<String>>

    /*
     * We want to make sure that all of the entries with the same symbol have the same category.
     * This routine returns all the distinct categories given a symbol.
     * It is up to higher level routines to make sure that all entries with the same symbol have
     * the same category.
     * We expect this to return 1 value. If there are more than 1 then something went terribly wrong
     */
    @Query("SELECT DISTINCT Category FROM LocalSymbol WHERE Name = :name")
    fun getCategoriesForSymbol(name: String): Flow<List<String>>

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