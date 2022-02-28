package org.joyfmi.dreams.repository

data class Symbol(
    val id: Int,
    val name: String,
    val category: Category,
    val meanings: List<Meaning>
)
