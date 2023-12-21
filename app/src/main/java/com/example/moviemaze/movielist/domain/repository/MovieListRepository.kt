package com.example.moviemaze.movielist.domain.repository

import com.example.moviemaze.movielist.domain.model.Movie
import com.example.moviemaze.movielist.util.Resource
import kotlinx.coroutines.flow.Flow

interface MovieListRepository {
    suspend fun getMovieList(
        forceFetchFromRemote : Boolean,
        category : String,
        page : Int
    ): Flow<Resource<List<Movie>>>

    suspend fun getMovieById(id : Int) : Flow<Resource<Movie>>
}