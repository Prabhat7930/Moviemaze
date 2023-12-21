package com.example.moviemaze.movielist.data.respository

import android.util.Log
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

            val localMovieList = movieDatabase.movieDao.getMovieListByCategory(category)

            val shouldLoadLocalMovie = localMovieList.isNotEmpty() && !forceFetchFromRemote

            if (shouldLoadLocalMovie) {
                Log.d("haha", "10")
                emit(Resource.Success(
                    data = localMovieList.map { movieEntity ->
                        movieEntity.toMovie(category)
                    }
                ))
                Log.d("haha", "11")
                emit(Resource.Loading(isLoading = true))
                return@flow
            }

            Log.d("haha", "12")
            val movieListFromApi = try {
                movieApi.getMoviesList(category, page)
            } catch (e : IOException) {
                e.printStackTrace()
                emit(Resource.Error(message = "Error loading movies"))
                Log.d("haha", "13")
                return@flow
            } catch (e : HttpException) {
                e.printStackTrace()
                emit(Resource.Error(message = "Error loading movies"))
                Log.d("haha", "14")
                return@flow
            } catch (e : Exception) {
                e.printStackTrace()
                emit(Resource.Error(message = "Error loading movies"))
                Log.d("haha", "15")
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
            Log.d("haha", "16")

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