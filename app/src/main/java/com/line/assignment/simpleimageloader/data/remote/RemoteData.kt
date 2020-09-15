package com.line.assignment.simpleimageloader.data.remote

import com.line.assignment.simpleimageloader.data.Resource
import com.line.assignment.simpleimageloader.data.dto.movies.Movies
import com.line.assignment.simpleimageloader.data.dto.movies.ImageItem
import com.line.assignment.simpleimageloader.data.error.NETWORK_ERROR
import com.line.assignment.simpleimageloader.data.error.NO_INTERNET_CONNECTION
import com.line.assignment.simpleimageloader.data.remote.service.MoviesService
import com.line.assignment.simpleimageloader.utils.NetworkConnectivity
import retrofit2.Response
import java.io.IOException
import javax.inject.Inject


/**
 * Created by PhucNguyen
 */

class RemoteData @Inject
constructor(private val serviceGenerator: ServiceGenerator, private val networkConnectivity: NetworkConnectivity) : RemoteDataSource {
    override suspend fun fetchMovies(): Resource<Movies> {
        val recipesService = serviceGenerator.createService(MoviesService::class.java)
        return when (val response = processCall(recipesService::fetchMovies)) {
            is List<*> -> {
                Resource.Success(data = Movies(response as ArrayList<ImageItem>))
            }
            else -> {
                Resource.DataError(errorCode = response as Int)
            }
        }
    }

    private suspend fun processCall(responseCall: suspend () -> Response<*>): Any? {
        if (!networkConnectivity.isConnected()) {
            return NO_INTERNET_CONNECTION
        }
        return try {
            val response = responseCall.invoke()
            val responseCode = response.code()
            if (response.isSuccessful) {
                response.body()
            } else {
                responseCode
            }
        } catch (e: IOException) {
            NETWORK_ERROR
        }
    }
}
