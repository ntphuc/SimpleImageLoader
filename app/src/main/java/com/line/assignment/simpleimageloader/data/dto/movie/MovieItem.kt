package com.line.assignment.simpleimageloader.data.dto.movie


import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class MovieItem(
    val title: String = "",
    val image: List<String> = listOf(),
) : Parcelable
