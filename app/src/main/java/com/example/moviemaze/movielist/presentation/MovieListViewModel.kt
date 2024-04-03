package com.example.moviemaze.movielist.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.moviemaze.movielist.domain.repository.MovieListRepository
import com.example.moviemaze.movielist.util.Category
import com.example.moviemaze.movielist.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MovieListViewModel @Inject constructor(
    private val movieListRepository: MovieListRepository
) : ViewModel() {
    private var _movieListState = MutableStateFlow(MovieListState())
    val movieListState = _movieListState.asStateFlow()

    init {
        getPopularMovieList(false)
        getUpcomingMovieList(false)
        getTopRatedMovieList(false)
    }


    fun onEvent(event : MovieListUIEvent) {
        when(event) {
            is MovieListUIEvent.Navigate -> {
                when (event.category) {
                    Category.POPULAR -> _movieListState.update {
                        it.copy(
                            currentScreen = 1
                        )
                    }
                    Category.UPCOMING -> {
                        _movieListState.update {
                            it.copy(
                                currentScreen = 2
                            )
                        }
                    }
                    Category.TOP_RATED -> {
                        _movieListState.update {
                            it.copy(
                                currentScreen = 3
                            )
                        }
                    }
                }
            }
            is MovieListUIEvent.Paginate -> {
                when(event.category) {
                    Category.POPULAR -> {
                        getPopularMovieList(true)
                    }
                    Category.UPCOMING -> {
                        getUpcomingMovieList(true)
                    }
                    Category.TOP_RATED -> {
                        getTopRatedMovieList(true)
                    }
                }
            }
        }
    }

    private fun getPopularMovieList(forceFetchFromRemote : Boolean) {
        viewModelScope.launch {
            _movieListState.update {
                it.copy(isLoading = true)
            }

            movieListRepository.getMovieList(
                forceFetchFromRemote,
                Category.POPULAR,
                movieListState.value.popularMovieListPage
            ).collectLatest { result ->
                when(result) {
                    is Resource.Error -> {
                        _movieListState.update {
                            it.copy(isLoading = false)
                        }
                    }
                    is Resource.Success -> {
                        result.data?.let {popularList ->
                            _movieListState.update {
                                it.copy(
                                    popularMovieList = movieListState.value.popularMovieList
                                            + popularList,
                                    popularMovieListPage = movieListState.value.popularMovieListPage++
                                )
                            }
                        }
                    }
                    is Resource.Loading -> {
                        _movieListState.update {
                            it.copy(
                                isLoading = result.isLoading,
                                popularMovieListPage = movieListState.value.popularMovieListPage++
                            )
                        }
                    }
                }
            }
        }
    }

    private fun getUpcomingMovieList(forceFetchFromRemote : Boolean) {
        viewModelScope.launch {
            _movieListState.update {
                it.copy(isLoading = true)
            }

            movieListRepository.getMovieList(
                forceFetchFromRemote,
                Category.UPCOMING,
                movieListState.value.upcomingMovieListPage
            ).collectLatest { result ->
                when(result) {
                    is Resource.Error -> {
                        _movieListState.update {
                            it.copy(isLoading = false)
                        }
                    }
                    is Resource.Success -> {
                        result.data?.let { upcomingList ->
                            _movieListState.update {
                                it.copy(
                                    upcomingMovieList = movieListState.value.upcomingMovieList + upcomingList,
                                    upcomingMovieListPage = movieListState.value.upcomingMovieListPage++
                                )
                            }
                        }
                    }
                    is Resource.Loading -> {
                        _movieListState.update {
                            it.copy(
                                isLoading = result.isLoading,
                                upcomingMovieListPage = movieListState.value.upcomingMovieListPage++
                            )
                        }
                    }
                }
            }
        }
    }

    private fun getTopRatedMovieList(forceFetchFromRemote : Boolean) {
        viewModelScope.launch {
            _movieListState.update {
                it.copy(isLoading = true)
            }

            movieListRepository.getMovieList(
                forceFetchFromRemote,
                Category.TOP_RATED,
                movieListState.value.topRatedMovieListPage
            ).collectLatest { result ->
                when(result) {
                    is Resource.Error -> {
                        _movieListState.update {
                            it.copy(isLoading = false)
                        }
                    }
                    is Resource.Success -> {
                        result.data?.let { topRatedList ->
                            _movieListState.update {
                                it.copy(
                                    topRatedMovieList = movieListState.value.topRatedMovieList + topRatedList,
                                    topRatedMovieListPage = movieListState.value.topRatedMovieListPage++
                                )
                            }
                        }
                    }
                    is Resource.Loading -> {
                        _movieListState.update {
                            it.copy(
                                isLoading = result.isLoading,
                                topRatedMovieListPage = movieListState.value.topRatedMovieListPage++
                            )
                        }
                    }
                }
            }
        }
    }
}