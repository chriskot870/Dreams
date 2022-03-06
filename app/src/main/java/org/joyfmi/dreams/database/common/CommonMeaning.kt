package org.joyfmi.dreams.database.common

import androidx.annotation.NonNull
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

/*
 * Define the data Meaning class that matches the Meaning Table of the database
 */
@Entity(tableName = "Meaning")
data class CommonMeaning (
    @PrimaryKey(autoGenerate = true) @ColumnInfo(name = "Id") val id: Int,
    @NonNull @ColumnInfo(name = "Contents") val contents: String,
    @ColumnInfo(name = "Reference") val reference: String?,
    @NonNull @ColumnInfo(name = "SymbolId") val symbolId: Int,
    @NonNull @ColumnInfo(name = "Local") val local: Int
    )
