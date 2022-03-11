package org.joyfmi.dreams.database.common

import androidx.room.Dao
import androidx.room.Query

@Dao
interface CommonCategoryDao {

    @Query("SELECT * FROM Category")
    suspend fun getAllCategories(): List<CommonCategory>
}