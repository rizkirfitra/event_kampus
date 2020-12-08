package com.ikriz.eventkampus

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Events(
    val judul: String? = null,
    val deskripsi: String? = null,
    val logo: String? = null
) : Parcelable