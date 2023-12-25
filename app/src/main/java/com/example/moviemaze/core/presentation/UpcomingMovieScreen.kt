package com.example.moviemaze.core.presentation

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.moviemaze.core.presentation.components.MovieItem
import com.example.moviemaze.movielist.presentation.MovieListState
import com.example.moviemaze.movielist.presentation.MovieListUIEvent
import com.example.moviemaze.movielist.util.Category

@Composable
fun UpcomingMovieScreen(
    movieListState : MovieListState,
    navController : NavHostController,
    onEvent : (MovieListUIEvent) -> Unit
) {

    if (movieListState.upcomingMovieList.isEmpty()) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator()
        }
    }
    else {
        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(vertical = 8.dp, horizontal = 4.dp)
        ) {
            items(movieListState.upcomingMovieList.size) {index ->
                MovieItem(
                    movie = movieListState.upcomingMovieList[index],
                    navHostController = navController
                )

                Spacer(modifier = Modifier.height(16.dp))

                if (index >= movieListState.upcomingMovieList.size - 1 && !movieListState.isLoading)  {
                    onEvent(MovieListUIEvent.Paginate(Category.UPCOMING))
                }
            }
        }
    }

}