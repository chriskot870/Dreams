package org.joyfmi.dreams.repository

data class Symbol(
    val identity: SymbolIdentity,
    val category: CategoryIdentity,
    val meanings: List<Meaning>,
)
