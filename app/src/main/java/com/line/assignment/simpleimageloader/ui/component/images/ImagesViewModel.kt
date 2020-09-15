package com.line.assignment.simpleimageloader.ui.component.images

import androidx.annotation.VisibleForTesting
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.line.assignment.simpleimageloader.data.DataRepositorySource
import com.line.assignment.simpleimageloader.data.dto.movie.MovieItem
import com.line.assignment.simpleimageloader.ui.base.BaseViewModel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * Created by PhucNguyen
 */

open class ImagesViewModel @Inject constructor(private val dataRepository: DataRepositorySource) :
    BaseViewModel() {

    val movieItem = MutableLiveData<MovieItem>()
    val movieData: LiveData<MovieItem> get() = movieItem


    fun getMovieItem() {
        viewModelScope.launch {
            dataRepository.loadMovieItem().collect {
                movieItem.value = it.data!!
            }

        }
    }

    fun getListImages(): List<String>? {
        return movieItem.value?.image
    }
}
