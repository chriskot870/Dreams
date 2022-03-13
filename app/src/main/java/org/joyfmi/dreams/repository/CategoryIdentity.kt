package org.joyfmi.dreams.repository

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
/*
 * We want to pass CategoryIdentity as arguments to fragments so make it Parcelable
 */
@Parcelize
data class CategoryIdentity (
    val id: Int,
    val name: String,
    val local: Int
): Parcelable {
    override fun toString() = "$name"
}