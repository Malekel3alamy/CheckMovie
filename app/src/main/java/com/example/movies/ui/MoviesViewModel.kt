package com.example.movies.ui

import android.content.Context
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


    var roomMovies : LiveData<ArrayList<Movie>>? = null

    // get Movie Details

    fun getMovieDetails(movie_id:Int) = viewModelScope.launch {

       detailsResponse.value =  moviesRepo.getDetails(movie_id)
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

//  Upsert Articles
fun  upsertMovies(moviesList : List<Movie>) = viewModelScope.launch {

    moviesRepo.upsert(moviesList)

}
// Get All Articles
    fun getAllArticlesFromRoom() = moviesRepo.getAllData()


    // delete data inside database
    fun deleteAll()=viewModelScope.launch {
        moviesRepo.deleteAll()
    }


fun  updateMoviesDataAndApi() = viewModelScope.launch {

    deleteAll()
    upsertMovies(roomMovies?.value!!.toList() )

}



}