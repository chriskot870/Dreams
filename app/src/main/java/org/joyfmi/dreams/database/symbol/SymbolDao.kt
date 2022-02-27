package org.joyfmi.dreams.database.symbol

import androidx.room.Dao
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

/*
 * This provides the routines to get data from the Symbol Table of the database
 */
@Dao
interface SymbolDao {

    @Query("SELECT * FROM Symbol WHERE categoryId = :cat ORDER By name ASC")
    fun getSymbolsByCategoryId(cat: Int): Flow<List<Symbol>>

    @Query("SELECT * FROM Symbol ORDER By name ASC")
    fun getAllSymbols(): Flow<List<Symbol>>
}