package com.example.fittrack.di

import com.example.fittrack.data.api.FitnessApiService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Provides
    @Singleton
    fun provideFitnessApiService(): FitnessApiService {
        return Retrofit.Builder()
            .baseUrl("https://api.fittrackapp.com/v1/") // Use a working API or mock
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(FitnessApiService::class.java)
    }
}
