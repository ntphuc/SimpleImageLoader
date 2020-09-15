package com.line.assignment.simpleimageloader.data.remote.service

import com.line.assignment.simpleimageloader.data.dto.movies.ImageItem
import retrofit2.Response
import retrofit2.http.GET

/**
 * Created by PhucNguyen
 */

interface MoviesService {
    @GET("movies.json")
    suspend fun fetchMovies(): Response<List<ImageItem>>
}
