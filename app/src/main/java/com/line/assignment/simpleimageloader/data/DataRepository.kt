package com.line.assignment.simpleimageloader.data

import com.line.assignment.simpleimageloader.data.dto.movie.MovieItem
import com.line.assignment.simpleimageloader.data.dto.movies.Movies
import com.line.assignment.simpleimageloader.data.local.LocalData
import com.line.assignment.simpleimageloader.data.remote.RemoteData
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject
import kotlin.coroutines.CoroutineContext


/**
 * Created by PhucNguyen
 */

class DataRepository @Inject constructor(private val remoteRepository: RemoteData, private val localRepository: LocalData, private val ioDispatcher: CoroutineContext) : DataRepositorySource {
    override suspend fun loadMovieItem(): Flow<Resource<MovieItem>> {
        return flow {
            emit(localRepository.loadMovieItem())
        }.flowOn(ioDispatcher)
    }

    override suspend fun fetchMovies(): Flow<Resource<Movies>> {
        return flow {
            emit(remoteRepository.fetchMovies())
        }.flowOn(ioDispatcher)
    }


}
