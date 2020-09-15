
package com.line.assignment.simpleimageloader.di

import com.line.assignment.simpleimageloader.data.DataRepository
import com.line.assignment.simpleimageloader.data.DataRepositorySource
import dagger.Binds
import dagger.Module
import javax.inject.Singleton

// Tells Dagger this is a Dagger module
@Module
abstract class DataModule {
    @Binds
    @Singleton
    abstract fun provideDataRepository(dataRepository: DataRepository): DataRepositorySource
}
