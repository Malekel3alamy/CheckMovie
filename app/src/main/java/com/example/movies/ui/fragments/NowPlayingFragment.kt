package com.example.movies.ui.fragments

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.movies.R
import com.example.movies.adapter.NowPlayingMoviesAdapter
import com.example.movies.databinding.FragmentNowPlayingBinding
import com.example.movies.ui.MainActivity
import com.example.movies.ui.MoviesViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class NowPlayingFragment : Fragment(R.layout.fragment_now_playing) {
     val moviesViewModel: MoviesViewModel by viewModels()
    lateinit var binding: FragmentNowPlayingBinding
    lateinit var moviesAdapter : NowPlayingMoviesAdapter


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentNowPlayingBinding.bind(view)

        (activity as MainActivity).showToolbarAndNavigationView()
        setUpRecycler()
        moviesAdapter.onMovieClick= { movie ->

            val bundle = Bundle().apply {
                if (movie.id != null)
                    putParcelable("movie",movie)
            }
                findNavController().navigate(R.id.action_nowPlayingFragment_to_moviesFragment,bundle)
        }
        val inflater =
            requireContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val ite_view_error = inflater.inflate(R.layout.item_error, null)



        if (moviesViewModel.internetConnection((activity as MainActivity).applicationContext)){
            lifecycleScope.launch {
                moviesViewModel.nowPlayingMovies.collect{
                    moviesAdapter.submitData(it)
                   // moviesViewModel.upsertMovies(it.results.toList())
                }
            }

        }else{

         /*  moviesViewModel.roomMovies.observe(viewLifecycleOwner, Observer {

                moviesAdapter.submitData(it.toList())
            })*/
        }

        binding.itemMoviesError.retryButton.setOnClickListener {

          //  moviesViewModel.getNowPlayingMovies(pageNumber)
        }
    }

    private fun setUpRecycler(){
        moviesAdapter = NowPlayingMoviesAdapter()

        binding.recyclerNowPlaying.apply {
            adapter = moviesAdapter
            layoutManager = LinearLayoutManager(activity)

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