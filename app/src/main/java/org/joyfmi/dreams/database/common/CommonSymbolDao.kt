package org.joyfmi.dreams.database.common

import androidx.room.Dao
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

/*
 * This provides the routines to get data from the Symbol Table of the database
 */
@Dao
interface CommonSymbolDao {

    @Query("SELECT * FROM CommonSymbol WHERE categoryId = :cat ORDER By name ASC")
    fun getSymbolsByCategoryId(cat: Int): Flow<List<CommonSymbol>>

    @Query("SELECT * FROM CommonSymbol ORDER By name ASC")
    fun getAllSymbols(): Flow<List<CommonSymbol>>
}