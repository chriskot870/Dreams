package org.joyfmi.dreams.database.category

import androidx.annotation.NonNull
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

/*
 * Define the data Category class that matches the Category Table of the database
 */
@Entity
data class Category (
    /*
     * The definitions and types here should match the database Schema for the Category table
     */
    @PrimaryKey(autoGenerate = true) @ColumnInfo(name = "Id") val id: Int,
    @NonNull @ColumnInfo(name = "Name") val name: String,
    @NonNull @ColumnInfo(name = "Local") val local: Int
    )
