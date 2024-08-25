package com.example.movies.di

import android.content.Context
import com.example.movies.room.MoviesDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RoomDatabaseModule {

    @Provides
    @Singleton
    fun provideRoomModule(@ApplicationContext context: Context) : MoviesDatabase {

        return MoviesDatabase.getInstance(context)

    }
}