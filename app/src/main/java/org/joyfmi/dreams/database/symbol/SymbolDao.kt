package org.joyfmi.dreams.database.symbol

import androidx.room.Dao
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface SymbolDao {

    @Query("SELECT * FROM Symbol WHERE categoryId = :cat ORDER By name ASC")
    fun getSymbolsByCategoryId(cat: Int): Flow<List<Symbol>>
}