package com.line.assignment.simpleimageloader.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.line.assignment.simpleimageloader.ui.ViewModelFactory
import com.line.assignment.simpleimageloader.ui.component.images.ImagesViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

/**
 * Since dagger
 * support multibinding you are free to bind as may ViewModels as you want.
 */
@Suppress("unused")
@Module
abstract class ViewModelModule {
    @Binds
    internal abstract fun bindViewModelFactory(viewModelFactory: ViewModelFactory): ViewModelProvider.Factory


    @Binds
    @IntoMap
    @ViewModelKey(ImagesViewModel::class)
    internal abstract fun bindImagesViewModel(viewModel: ImagesViewModel): ViewModel
}
