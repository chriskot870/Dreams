package org.joyfmi.dreams.database.category

import androidx.room.Dao
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface CategoryDao {

    @Query("SELECT * FROM Category")
    fun getAllCategories(): Flow<List<Category>>
}