package com.example.moviemaze.di

import com.example.moviemaze.movielist.data.respository.MovieListRepositoryImpl
import com.example.moviemaze.movielist.domain.repository.MovieListRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun bindMovieRepository(
        movieListRepositoryImpl : MovieListRepositoryImpl
    ) : MovieListRepository
}