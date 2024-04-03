package com.example.moviemaze.movielist.presentation

import com.example.moviemaze.movielist.domain.model.Movie

data class MovieListState(
    val isLoading : Boolean = false,

    var popularMovieListPage : Int = 1,
    var upcomingMovieListPage : Int = 1,
    var topRatedMovieListPage : Int = 1,

    val currentScreen : Int = 1,

    val popularMovieList : List<Movie> = emptyList(),
    val upcomingMovieList : List<Movie> = emptyList(),
    val topRatedMovieList : List<Movie> = emptyList()

)
