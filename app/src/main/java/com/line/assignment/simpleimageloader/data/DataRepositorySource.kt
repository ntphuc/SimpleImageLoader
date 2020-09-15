package com.line.assignment.simpleimageloader.data

import com.line.assignment.simpleimageloader.data.dto.movies.Movies
import com.line.assignment.simpleimageloader.data.dto.movie.MovieItem
import kotlinx.coroutines.flow.Flow

/**
 * Created by PhucNguyen
 */

interface DataRepositorySource {
    suspend fun loadMovieItem(): Flow<Resource<MovieItem>>
    suspend fun fetchMovies(): Flow<Resource<Movies>>
}
