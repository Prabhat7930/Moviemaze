package com.example.moviemaze.movielist.data.respository

import coil.network.HttpException
import com.example.moviemaze.movielist.data.local.movie.MovieDatabase
import com.example.moviemaze.movielist.data.mapper.toMovie
import com.example.moviemaze.movielist.data.mapper.toMovieEntity
import com.example.moviemaze.movielist.data.remote.MovieApi
import com.example.moviemaze.movielist.domain.model.Movie
import com.example.moviemaze.movielist.domain.repository.MovieListRepository
import com.example.moviemaze.movielist.util.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import okio.IOException
import javax.inject.Inject

class MovieListRepositoryImpl @Inject constructor(
    private val movieApi : MovieApi,
    private val movieDatabase: MovieDatabase
) : MovieListRepository{
    override suspend fun getMovieList(
        forceFetchFromRemote: Boolean,
        category: String,
        page: Int
    ): Flow<Resource<List<Movie>>> {
        return flow {
            emit(Resource.Loading(isLoading = true))

            val localMovieList = movieDatabase.movieDao.getMovieByCategory(category)

            val shouldLoadLocalMovie = localMovieList.isNotEmpty() && !forceFetchFromRemote

            if (shouldLoadLocalMovie) {
                emit(Resource.Success(
                    data = localMovieList.map { movieEntity ->
                        movieEntity.toMovie(category)
                    }
                ))

                emit(Resource.Loading(isLoading = true))
                return@flow
            }

            val movieListFromApi = try {
                movieApi.getMoviesList(category, page)
            } catch (e : IOException) {
                e.printStackTrace()
                emit(Resource.Error(message = "Error loading movies"))
                return@flow
            } catch (e : HttpException) {
                e.printStackTrace()
                emit(Resource.Error(message = "Error loading movies"))
                return@flow
            } catch (e : Exception) {
                e.printStackTrace()
                emit(Resource.Error(message = "Error loading movies"))
                return@flow
            }

            val movieEntities = movieListFromApi.results.let {
                it.map { movieDto ->
                    movieDto.toMovieEntity(category)
                }
            }

            movieDatabase.movieDao.upsertMovieList(movieEntities)
            emit(Resource.Success(
                movieEntities.map { it.toMovie(category) }
            ))
            emit(Resource.Loading(false))
        }
    }

    override suspend fun getMovieById(id: Int): Flow<Resource<Movie>> {
        return flow {
            emit(Resource.Loading(isLoading = true))

            val movieEntity = movieDatabase.movieDao.getMovieById(id)

            if (movieEntity != null) {
                emit(Resource.Success(
                    movieEntity.toMovie(movieEntity.category)
                ))
                emit(Resource.Loading(isLoading = false))
                return@flow
            }

            emit(Resource.Error(
                "No such movie exist"
            ))

        }
    }
}