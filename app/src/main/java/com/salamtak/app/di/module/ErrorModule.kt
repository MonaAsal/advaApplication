package com.salamtak.app.di.module

import com.salamtak.app.data.error.mapper.ErrorMapper
import com.salamtak.app.data.error.mapper.ErrorMapperInterface
import com.salamtak.app.usecase.errors.ErrorFactory
import com.salamtak.app.usecase.errors.ErrorManager
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

// with @Module we Telling Dagger that, this is a Dagger module
@InstallIn(SingletonComponent::class)
@Module
abstract class ErrorModule {
    @Binds
    abstract fun provideErrorFactoryImpl(errorManager: ErrorManager): ErrorFactory

    @Binds
    abstract fun provideErrorMapper(errorMapper: ErrorMapper): ErrorMapperInterface
}
