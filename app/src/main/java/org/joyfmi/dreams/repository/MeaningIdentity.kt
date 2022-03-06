package org.joyfmi.dreams.repository

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

/*
 * We want to pass MeaningIdentity as arguments to fragments so make it Parcelable
 */
@Parcelize
data class MeaningIdentity(
    val id: Int,
    val local: Int
): Parcelable
