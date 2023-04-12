package com.exercise.digio.di

import com.exercise.digio.data.RepositoryImp
import com.exercise.digio.data.network.retrofit.RetrofitInstance
import com.exercise.digio.domain.Repository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
class RepositoryModule {

    @Provides
    @Singleton
    fun providesRepository(
        network: RetrofitInstance
    ): Repository {
        return RepositoryImp(
            network = network
        )
    }

}