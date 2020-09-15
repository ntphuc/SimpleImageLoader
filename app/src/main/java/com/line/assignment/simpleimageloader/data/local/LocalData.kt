package com.line.assignment.simpleimageloader.data.local

import com.line.assignment.simpleimageloader.App.Companion.context
import com.line.assignment.simpleimageloader.data.Resource
import com.line.assignment.simpleimageloader.data.dto.movie.MovieItem
import com.line.assignment.simpleimageloader.utils.ParserJsonLocal
import javax.inject.Inject

/**
 * Created by PhucNguyen
 */

class LocalData @Inject constructor() {


    fun loadMovieItem(): Resource<MovieItem> {
         val loadJsonDataLocal = ParserJsonLocal(context)
         val movieItem = loadJsonDataLocal.loadMovieItem()
         return Resource.Success(movieItem)
    }
}

