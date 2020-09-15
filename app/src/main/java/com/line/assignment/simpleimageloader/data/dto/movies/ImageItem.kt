package com.line.assignment.simpleimageloader.data.dto.movies


import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class ImageItem(

        val name: String = "",
        val url: List<String> = listOf()

) : Parcelable
