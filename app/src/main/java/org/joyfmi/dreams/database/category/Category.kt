package org.joyfmi.dreams.database.category

import androidx.annotation.NonNull
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Category (
    @PrimaryKey @ColumnInfo(name = "Id") val id: Int,
    @NonNull @ColumnInfo(name = "Name") val name: String,
    @NonNull @ColumnInfo(name = "Local") val local: Int
    )
