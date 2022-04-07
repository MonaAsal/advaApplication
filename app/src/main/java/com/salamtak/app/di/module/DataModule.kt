package com.salamtak.app.di.module

import com.salamtak.app.data.DataRepository
import com.salamtak.app.data.DataSource
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

// Tells Dagger this is a Dagger module
@InstallIn(SingletonComponent::class)
@Module
abstract class DataModule {
    @Binds
    abstract fun provideDataRepository(dataRepository: DataRepository): DataSource

//    @Binds
//    abstract fun provideUseCase(sharedUseCase: SharedUseCase): SharedUseCase
}
