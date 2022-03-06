package org.joyfmi.dreams.repository

data class Meaning(
    val identity: MeaningIdentity,
    val reference: String?,
    val contents: String,
    val symbol: SymbolIdentity
)
