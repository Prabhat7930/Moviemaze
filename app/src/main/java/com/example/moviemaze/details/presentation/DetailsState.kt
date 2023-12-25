package com.example.moviemaze.details.presentation

import com.example.moviemaze.movielist.domain.model.Movie

data class DetailsState(
    val isLoading : Boolean = false,
    val movie : Movie? = null
)
