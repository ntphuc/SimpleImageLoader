package com.line.assignment.simpleimageloader.di

import com.line.assignment.simpleimageloader.ui.component.images.ImagesActivity
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Suppress("unused")
@Module
abstract class ActivityModuleBuilder {

    @ContributesAndroidInjector()
    abstract fun contributeImagesActivity(): ImagesActivity

}
