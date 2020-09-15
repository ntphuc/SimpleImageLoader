package com.line.assignment.simpleimageloader.utils

import android.content.Context
import com.google.gson.Gson
import com.line.assignment.simpleimageloader.R
import com.line.assignment.simpleimageloader.data.dto.movie.MovieItem

/**
 * Created by PhucNguyen
 */

class ParserJsonLocal constructor(val context: Context) {
    private var movieItem: MovieItem = MovieItem()

    init {

        val gson = Gson()
        try {
            val jsonData = getJson(context, R.raw.movie_item)
            movieItem = gson.fromJson(jsonData, MovieItem::class.java)
        } catch (e: Exception) {
        }

    }

    fun loadMovieItem(): MovieItem {
        return movieItem
    }

    private fun getJson(context: Context, id: Int): String {
        val text = context.resources.openRawResource(id)
                .bufferedReader().use { it.readText() }
        return text
    }

}
