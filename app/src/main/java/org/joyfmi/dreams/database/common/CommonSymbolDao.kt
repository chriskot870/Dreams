package org.joyfmi.dreams.database.common

import androidx.room.Dao
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

/*
 * This provides the routines to get data from the Symbol Table of the database
 */
@Dao
interface CommonSymbolDao {

    @Query("SELECT * FROM Symbol WHERE categoryId = :cat ORDER By name ASC")
    fun getSymbolNamesByCategoryId(cat: Int): Flow<List<CommonSymbol>>

    @Query("SELECT * FROM Symbol ORDER By name ASC")
    fun getAllSymbols(): Flow<List<CommonSymbol>>
}