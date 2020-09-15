package com.line.assignment.simpleimageloader.di

import com.line.assignment.simpleimageloader.App
import dagger.Component
import dagger.android.AndroidInjectionModule
import javax.inject.Singleton

@Singleton
@Component(
        modules = [
            AndroidInjectionModule::class,
            AppModule::class,
            DataModule::class,
            ErrorModule::class,
            ActivityModuleBuilder::class,
            ViewModelModule::class
        ]
)
interface AppComponent {
    @Component.Builder
    interface Builder {
        fun build(): AppComponent
    }

    fun inject(app: App)
}
