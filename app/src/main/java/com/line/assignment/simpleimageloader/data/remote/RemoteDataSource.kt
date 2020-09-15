package com.line.assignment.simpleimageloader.data.remote

import com.line.assignment.simpleimageloader.data.Resource
import com.line.assignment.simpleimageloader.data.dto.movies.Movies

/**
 * Created by PhucNguyen
 */

internal interface RemoteDataSource {
    suspend fun fetchMovies(): Resource<Movies>
}
