package org.joyfmi.dreams.database.local

import androidx.annotation.NonNull
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class LocalMeaning(
    @PrimaryKey(autoGenerate = true) @ColumnInfo(name = "Id") val id: Int,
    @NonNull @ColumnInfo(name = "Category") val category: String,
    @NonNull @ColumnInfo(name = "Name") val name: String,
    @NonNull @ColumnInfo(name = "Reference") val reference: String,
    @NonNull @ColumnInfo(name = "Meaning") val meaning: String,
    @NonNull @ColumnInfo(name = "Local") val local: Int
)
