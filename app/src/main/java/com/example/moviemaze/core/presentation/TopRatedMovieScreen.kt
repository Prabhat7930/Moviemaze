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
fun TopRatedMovieScreen(
    navController : NavHostController,
    movieListState : MovieListState,
    onEvent : (MovieListUIEvent) -> Unit
) {
    if (movieListState.topRatedMovieList.isEmpty()) {
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
            items(movieListState.topRatedMovieList.size) {index ->
                MovieItem(
                    movie = movieListState.topRatedMovieList[index],
                    navHostController = navController
                )

                Spacer(modifier = Modifier.height(16.dp))

                if (index >= movieListState.topRatedMovieList.size - 1 && !movieListState.isLoading)  {
                    onEvent(MovieListUIEvent.Paginate(Category.TOP_RATED))
                }
            }
        }
    }
}