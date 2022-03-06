package org.joyfmi.dreams.database.common

import androidx.room.Dao
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface CommonCategoryDao {

    @Query("SELECT * FROM Category")
    fun getAllCategories(): Flow<List<CommonCategory>>
}