package org.joyfmi.dreams.repository

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

/*
 * We want to pass SymbolIdentity as arguments to fragments so make it Parcelable
 */
@Parcelize
data class SymbolIdentity(
    val id: Int,
    val name: String,
    val local: Int
): Parcelable
