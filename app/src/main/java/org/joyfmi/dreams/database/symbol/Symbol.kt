package org.joyfmi.dreams.database.symbol

import androidx.annotation.NonNull
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Symbol (
        @PrimaryKey @ColumnInfo(name = "Id") val id: Int,
        @NonNull @ColumnInfo(name = "Name") val name: String,
        @NonNull @ColumnInfo(name = "CategoryId") val categoryId: Int,
        @NonNull @ColumnInfo(name = "Local") val local: Int
    )
