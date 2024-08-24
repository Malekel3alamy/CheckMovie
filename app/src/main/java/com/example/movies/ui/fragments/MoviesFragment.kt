package com.example.movies.ui.fragments

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.example.movies.R
import com.example.movies.databinding.FragmentMoviesBinding
import com.example.movies.models.MovieResponse
import com.example.movies.ui.MainActivity
import com.example.movies.ui.MoviesViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class MoviesFragment : Fragment(R.layout.fragment_movies) {
     val moviesViewModel: MoviesViewModel by viewModels()
    lateinit var binding : FragmentMoviesBinding


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentMoviesBinding.bind(view)

        (activity as MainActivity).hideToolbarAndNavigationView()


        // Getting Movie Id
        if (arguments!= null){
    val id = arguments?.getInt("id")
    if (id != null){
        moviesViewModel.getMovieDetails(id)
    }else{
        Log.d("id","$id")
    }
}
        lifecycleScope.launch {
            moviesViewModel.detailsResponse.observe(viewLifecycleOwner, Observer {
                lifecycleScope.launch (Dispatchers.Main){
                    binding.detailsTitle.text = it?.title
                    binding.detailsViews.text = "Views : " + it?.popularity.toString()
                    binding.detailsDate.text =   it?.release_date.toString()
                    binding.detailsOverview.text=it?.overview
                    binding.detailsLanguage.text= " Language : " +it?.original_language.toString()
                    Log.d("Language",it?.original_language.toString())
                    Glide.with(view).load("https://image.tmdb.org/t/p/w500/${it?.poster_path}").into(binding.moviesMovieImage)

                }
            })
        }
        val details = moviesViewModel.detailsResponse



    }
}