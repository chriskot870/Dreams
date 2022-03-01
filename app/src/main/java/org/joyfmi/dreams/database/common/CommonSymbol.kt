package org.joyfmi.dreams.database.common

import androidx.annotation.NonNull
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

/*
 * Define the data Symbol class that matches the Symbol Table of the database
 */
@Entity(indices = [Index(value = ["Name"], unique = true)])
data class CommonSymbol (
    /*
     * The definitions and types here should match the database Schema for the Symbol table
     */
    @PrimaryKey(autoGenerate = true) @ColumnInfo(name = "Id") val id: Int,
    @NonNull @ColumnInfo(name = "Name") val name: String,
    @NonNull @ColumnInfo(name = "CategoryId") val categoryId: Int,
    @NonNull @ColumnInfo(name = "Local") val local: Int
    )
