package com.example.moviemaze.movielist.util

sealed class Screen(val rout : String){
    data object Home : Screen("main")
    data object PopularMovieList : Screen("popularMovie")
    data object UpcomingMovieList : Screen("upcomingMovie")
    data object TopRatedMovieList : Screen("topRatedMovie")
    data object Details : Screen("details")
}