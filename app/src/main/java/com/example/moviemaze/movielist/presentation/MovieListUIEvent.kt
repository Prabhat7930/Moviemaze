package com.example.moviemaze.movielist.presentation

sealed interface MovieListUIEvent {
    data class Paginate(val category: String) : MovieListUIEvent
    object Navigate : MovieListUIEvent

}