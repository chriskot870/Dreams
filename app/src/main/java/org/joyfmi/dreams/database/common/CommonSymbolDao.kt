package org.joyfmi.dreams.database.common

import androidx.room.Dao
import androidx.room.Query
/*
 * This provides the routines to get data from the Symbol Table of the database
 */
@Dao
interface CommonSymbolDao {

    @Query("SELECT * FROM Symbol WHERE categoryId = :cat ORDER By name ASC")
    suspend fun getSymbolNamesByCategoryId(cat: Int): List<CommonSymbol>

    @Query("SELECT * FROM Symbol ORDER By name ASC")
    suspend fun getAllSymbols(): List<CommonSymbol>
}