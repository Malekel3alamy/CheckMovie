package com.example.movies.ui

import android.content.Context
import android.database.Cursor
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import androidx.room.Query
import com.example.movies.models.Movie
import com.example.movies.models.MovieResponse
import com.example.movies.models.details.DetailsResponse
import com.example.movies.paging.NowPlayingMoviesPagingSource
import com.example.movies.paging.PopularMoviesPagingSource
import com.example.movies.paging.SearchMoviesPagingSource
import com.example.movies.paging.TopRatedMoviesPagingSource
import com.example.movies.paging.UpComingMoviesPagingSource
import com.example.movies.repo.MoviesRepo
import com.example.movies.utils.Resources
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import retrofit2.Response
import javax.inject.Inject

@HiltViewModel
class MoviesViewModel @Inject constructor( private val moviesRepo: MoviesRepo) : ViewModel() {

    val popularMovies = Pager(PagingConfig(1)){
        PopularMoviesPagingSource(moviesRepo)
    }.flow.cachedIn(viewModelScope)

    val nowPlayingMovies = Pager(PagingConfig(1)){
        NowPlayingMoviesPagingSource(moviesRepo)
    }.flow.cachedIn(viewModelScope)

    val topRatedMovies = Pager(PagingConfig(1)){
        TopRatedMoviesPagingSource(moviesRepo)
    }.flow.cachedIn(viewModelScope)

    val upcomingMovies = Pager(PagingConfig(1)){
        UpComingMoviesPagingSource(moviesRepo)
    }.flow.cachedIn(viewModelScope)

    var searchMovies :  Flow<PagingData<Movie>>? = null

fun searchMovie(keyword: String){
     searchMovies = Pager(PagingConfig(1)){
        SearchMoviesPagingSource(keyword,moviesRepo)
    }.flow.cachedIn(viewModelScope)
}


    var newSearchQuery : String? = null
    var oldSearchQuery:String? = null

    var  searchMoviePage = 1

    private var movieResponse : MovieResponse? = null
    private var searchMovieResponse : MovieResponse? = null
     var detailsResponse =  MutableLiveData<DetailsResponse>()


    var roomMovies : MutableLiveData<List<Movie>>? = null

    // get Movie Details

    fun getMovieDetails(movie_id:Int) = viewModelScope.launch {

       detailsResponse.value =  moviesRepo.getDetails(movie_id)
    }

    suspend fun  upsertMovies(movies : Movie):Boolean {
        var  result = false
        viewModelScope.async {

             moviesRepo.upsert(movies)
            result = true

        }.await()
        return result
    }
    // Get All Movies
    suspend fun getAllMoviesFromRoom() {
        roomMovies = MutableLiveData()
        val movies= moviesRepo.getAllData()
        if (movies!= null){
            roomMovies?.postValue(movies)
        }
    }



    fun cursorToMovieList(cursor: Cursor): List<Movie> {
        val movies = mutableListOf<Movie>()
        while (cursor.moveToNext()) {
            val id = cursor.getInt(cursor.getColumnIndexOrThrow("id"))
            val title = cursor.getString(cursor.getColumnIndexOrThrow("title"))
            val backdrop_path = cursor.getString(cursor.getColumnIndexOrThrow("backdrop_path"))
            val genre_ids = cursor.getInt(cursor.getColumnIndexOrThrow("genre_ids"))
            val original_language = cursor.getString(cursor.getColumnIndexOrThrow("original_language"))
            val original_title = cursor.getString(cursor.getColumnIndexOrThrow("original_title"))
            val overview = cursor.getString(cursor.getColumnIndexOrThrow("overview"))
            val popularity = cursor.getDouble(cursor.getColumnIndexOrThrow("popularity"))
            val poster_path = cursor.getString(cursor.getColumnIndexOrThrow("poster_path"))
            val release_date = cursor.getString(cursor.getColumnIndexOrThrow("release_date"))

            val vote_average = cursor.getDouble(cursor.getColumnIndexOrThrow("vote_average"))
            val vote_count = cursor.getInt(cursor.getColumnIndexOrThrow("vote_count"))

            // ... extract other properties
            movies.add(Movie(null,backdrop_path,
                listOf(genre_ids),id,original_language,original_title,overview,popularity,poster_path,release_date,title,null,vote_average,vote_count))}
        cursor.close()
        return movies
    }




  /*  fun getPopularMovies(page_number : Int) = viewModelScope.launch {
         popularMovies.postValue(Resources.Loading())
        try {
            popularMovies.postValue(handleMovieResponse(moviesRepo.getPopularMovies(page_number)))

        }catch (e:Exception){
        }

    }*/






 /*   fun getUpComingMovies(page_number : Int) = viewModelScope.launch {
        upcomingMovies.postValue(Resources.Loading())

            upcomingMovies.postValue(handleMovieResponse(moviesRepo.getUpcomingMovies(page_number)))


    }*/

 /*   fun getNowPlayingMovies(page_number : Int) = viewModelScope.launch {
        nowPlayingMovies.postValue(Resources.Loading())
        try {
            nowPlayingMovies.postValue(handleMovieResponse(moviesRepo.getNowPlayingMovies(page_number)))

        }catch (e:Exception){
            Log.d("ViewModel","${e.message}")
        }

    }*/

 /*   fun getTopRatedMovies(page_number : Int) = viewModelScope.launch {
        topRatedMovies.postValue(Resources.Loading())
        try {
            topRatedMovies.postValue(handleMovieResponse(moviesRepo.getTopRatedMovies(page_number)))

        }catch (e:Exception){
            Log.d("ViewModel","${e.message}")
        }

    }*/

    // handle network Response

    private fun handleMovieResponse(
        response : Response<MovieResponse>
    ) : Resources<MovieResponse>{

        if(response.isSuccessful){
            response.body()?.let{ resultResponse ->


                if(movieResponse == null){
                    movieResponse = resultResponse
                    Log.d("HandleResponse1","Movies: ${resultResponse.results}")
                }else{
                   val oldMovies = movieResponse?.results
                    val newMovies = resultResponse.results
                    oldMovies?.addAll(newMovies)

                }
                return Resources.Success(movieResponse?:resultResponse)

            }
        }

        return Resources.Error(response.message())

    }

    fun internetConnection(context: Context) : Boolean {
        (context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager).apply{

            val internetStatus = getNetworkCapabilities(activeNetwork)?.run{
                when{
                    hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> true
                    hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
                    hasTransport(NetworkCapabilities.TRANSPORT_WIFI) ->     true
                    else -> false
                }
            }



            return internetStatus?:false

        }

    }


 /*   fun  search (keywords:String , page_number: Int) =viewModelScope.launch {
        searchMovies.postValue(Resources.Loading())

        searchMovies.postValue(handleSearchMovie(moviesRepo.search(keywords,page_number)))

    }*/

    private fun handleSearchMovie(
      response :  Response<MovieResponse>
    ) : Resources<MovieResponse>{
        if(response.isSuccessful){
            response.body()?.let { resultResponse ->
                if(searchMovieResponse == null || newSearchQuery != oldSearchQuery){
                    searchMovieResponse = resultResponse
                    oldSearchQuery = newSearchQuery

                }else{
                    searchMoviePage++
                    val oldArticles = searchMovieResponse?.results
                    val newArticles = resultResponse.results

                    oldArticles?.addAll(newArticles)
                }
                return Resources.Success(searchMovieResponse?:resultResponse)
            }
        }
        return Resources.Error(response.message())

    }






    // delete data inside database
    fun deleteAll()=viewModelScope.launch {
        moviesRepo.deleteAll()
    }

    fun deleteMovie(movies:Movie) =viewModelScope.launch {
        moviesRepo.deleteMovie(movies)
    }

    fun  updateMoviesDataAndApi() = viewModelScope.launch {

        deleteAll()
       // upsertMovies(roomMovies?.value!!.toList() )

    }

    suspend fun getMovie(id:Int) :Boolean {
        var movie :Movie?= null
        viewModelScope.async {
             movie = moviesRepo.getMovie(id)

        }.await()

        if (movie == null){
            return false
        }else{
            return true
        }
    }




}