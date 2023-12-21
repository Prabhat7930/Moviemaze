package com.example.moviemaze.movielist.domain.repository

import com.example.moviemaze.movielist.domain.model.Movie
import kotlinx.coroutines.flow.Flow

interface MovieListRepository {
    suspend fun getMovieList(
        forceFetchFromRemote : Boolean,
        category : String,
        page : Int
    ): Flow<Result<List<Movie>>>
}