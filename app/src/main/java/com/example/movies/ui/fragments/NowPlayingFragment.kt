package com.example.movies.ui.fragments

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.movies.R
import com.example.movies.adapter.NowPlayingMoviesAdapter
import com.example.movies.adapter.TopRatedMoviesAdapter
import com.example.movies.adapter.UpComingMoviesAdapter
import com.example.movies.databinding.FragmentNowPlayingBinding
import com.example.movies.ui.MainActivity
import com.example.movies.ui.MoviesViewModel
import com.google.android.material.carousel.CarouselLayoutManager
import com.google.android.material.carousel.CarouselStrategy
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class NowPlayingFragment : Fragment(R.layout.fragment_now_playing) {
     val moviesViewModel: MoviesViewModel by viewModels()
    lateinit var binding: FragmentNowPlayingBinding
    private lateinit var nowPlayingMoviesAdapter : NowPlayingMoviesAdapter
    private lateinit var topRatedMoviesAdapter: TopRatedMoviesAdapter
    private lateinit var upComingMoviesAdapter: UpComingMoviesAdapter
    lateinit var ite_view_error : View

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentNowPlayingBinding.bind(view)

        (activity as MainActivity).showToolbarAndNavigationView()
        setUpRecycler()

        handleClickOnNowPlayingAdapter()
        handleClickOnTopRatedAdapter()
        handleClickOnUpComingAdapter()


        ite_view_error = view.findViewById<View>(R.id.itemMoviesError)


        checkInternetThenObserveMovies()


        binding.itemMoviesError.retryButton.setOnClickListener {

            checkInternetThenObserveMovies()
            ite_view_error.visibility = View.GONE
        }

    }

    private fun setUpRecycler(){
        nowPlayingMoviesAdapter = NowPlayingMoviesAdapter()
        topRatedMoviesAdapter = TopRatedMoviesAdapter()
        upComingMoviesAdapter = UpComingMoviesAdapter()

        binding.recyclerNowPlaying.apply {
            adapter = nowPlayingMoviesAdapter
            //layoutManager = LinearLayoutManager(activity,LinearLayoutManager.HORIZONTAL,false)

        }

        binding.recyclerUpcoming.apply {
            adapter  = upComingMoviesAdapter
            layoutManager = LinearLayoutManager(activity,LinearLayoutManager.HORIZONTAL,false)
        }
        binding.recyclerTopRated.apply {
            adapter  = topRatedMoviesAdapter
            layoutManager = LinearLayoutManager(activity,LinearLayoutManager.HORIZONTAL,false)
        }
    }

    private fun handleClickOnNowPlayingAdapter(){
        nowPlayingMoviesAdapter.onMovieClick= { movie ->

            val bundle = Bundle().apply {
                if (movie.id != null)
                    putParcelable("movie",movie)
            }
            findNavController().navigate(R.id.action_nowPlayingFragment_to_moviesFragment,bundle)
        }
    }
private fun checkInternetThenObserveMovies(){
    if (moviesViewModel.internetConnection(requireContext())){
       showProgressBar()
        lifecycleScope.launch {

            moviesViewModel.topRatedMovies.collectLatest { topRatedMovies ->
                topRatedMoviesAdapter.submitData(topRatedMovies)
            }

        }
        lifecycleScope.launch {
            moviesViewModel.upcomingMovies.collectLatest { upComingMovies ->
                upComingMoviesAdapter.submitData(upComingMovies)
            }

        }
        lifecycleScope.launch {
            moviesViewModel.nowPlayingMovies.collectLatest{nowPlayingMovies ->
                nowPlayingMoviesAdapter.submitData(nowPlayingMovies)
                // moviesViewModel.upsertMovies(it.results.toList())

            }

        }
        lifecycleScope.launch(Dispatchers.Main) {
            hideProgressBar()
        }

    }else{
        lifecycleScope.launch(Dispatchers.Main) {
            ite_view_error.visibility = View.VISIBLE

        }

    }
}
    private fun handleClickOnTopRatedAdapter(){
        topRatedMoviesAdapter.onMovieClick= { movie ->

            val bundle = Bundle().apply {
                if (movie.id != null)
                    putParcelable("movie",movie)
            }
            findNavController().navigate(R.id.action_nowPlayingFragment_to_moviesFragment,bundle)
        }
    }

    private fun handleClickOnUpComingAdapter(){
        upComingMoviesAdapter.onMovieClick= { movie ->

            val bundle = Bundle().apply {
                if (movie.id != null)
                    putParcelable("movie",movie)
            }
            findNavController().navigate(R.id.action_nowPlayingFragment_to_moviesFragment,bundle)
        }
    }




    var isError = false
    var isLoading = false


    private fun hideProgressBar(){
        binding.paginationProgressBar.visibility = View.GONE
        isLoading = false
    }

    private fun showProgressBar(){

        binding.paginationProgressBar.visibility = View.VISIBLE
        isLoading = true
    }
    private fun hideErrorMessage(){
        binding.itemMoviesError.errorText.visibility = View.INVISIBLE
        binding.itemMoviesError.retryButton.visibility=View.INVISIBLE
        isError = false
    }
    private fun showErrorMessage(message: String){
         binding.itemMoviesError.errorText.visibility=View.VISIBLE
        binding.itemMoviesError.retryButton.visibility=View.VISIBLE
        isError = true
        binding.itemMoviesError.errorText.text = message
    }


}