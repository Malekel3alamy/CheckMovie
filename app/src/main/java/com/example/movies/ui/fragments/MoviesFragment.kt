package com.example.movies.ui.fragments

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.View
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.example.movies.R
import com.example.movies.databinding.FragmentMoviesBinding
import com.example.movies.models.Movie
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

    var movie :Movie? = null

    private var i = false


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentMoviesBinding.bind(view)

        showPR()

        // Getting Movie Id
        if (arguments!= null){
            (activity as MainActivity).hideToolbarAndNavigationView()
     movie = arguments?.getParcelable<Movie>("movie")
    if (movie!!.id != null){
        moviesViewModel.getMovieDetails(movie!!.id!!)
    }else{
        Log.d("id","$id")
    }
}
        lifecycleScope.launch {
            moviesViewModel.detailsResponse.observe(viewLifecycleOwner, Observer {
                lifecycleScope.launch (Dispatchers.Main){
                    hidePR()
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
        if (movie != null){
            lifecycleScope.launch {
                if(moviesViewModel.getMovie(movie!!.id!!) ){
                    binding.bookmarkBlack.visibility =View.VISIBLE
                }
            }
        }


    binding.bookmark.setOnClickListener {
        if (binding.bookmarkBlack.visibility == View.GONE){
            lifecycleScope.launch {
                val result = moviesViewModel.upsertMovies(movie!!)
                if (result) {
                    lifecycleScope.launch(Dispatchers.Main) {
                        binding.bookmarkBlack.visibility = View.VISIBLE
                        Toast.makeText(requireContext(), " Movie Added To BookMarks ", Toast.LENGTH_SHORT).show()


                    }
                }
            }
        }else if(binding.bookmarkBlack.visibility == View.VISIBLE){
            lifecycleScope.launch(Dispatchers.Main) {
                binding.bookmarkBlack.visibility = View.GONE
                Toast.makeText(requireContext()," Movie Moved ",Toast.LENGTH_SHORT).show()

                moviesViewModel.deleteMovie(movie!!)
                savedInstanceState?.putBoolean("isMovieSaved", i)
                i = false
            }
        }
   /*     if (i == false) {

            lifecycleScope.launch {
                val result = moviesViewModel.upsertMovies(movie!!)
                if (result == true) {
                    lifecycleScope.launch(Dispatchers.Main) {
                        binding.bookmarkBlack.visibility = View.VISIBLE
                        Toast.makeText(requireContext(), " Movie Added To BookMarks ", Toast.LENGTH_SHORT).show()
                        i = true

                    }
                }
            }
        }else{
                    lifecycleScope.launch(Dispatchers.Main) {
                        binding.bookmarkBlack.visibility = View.GONE
                        Toast.makeText(requireContext()," Movie Moved ",Toast.LENGTH_SHORT).show()

                        moviesViewModel.deleteMovie(movie!!)
                        savedInstanceState?.putBoolean("isMovieSaved", i)
                        i = false
                    }
                }*/
    }
        binding.backButton.setOnClickListener {
            findNavController().navigateUp()
        }

    }

    override fun onStart() {
        super.onStart()

        if (i){
            binding.bookmarkBlack.visibility = View.VISIBLE
        }
    }

    fun showPR(){
        binding.moviePR.visibility = View.VISIBLE
        binding.movieFragment.visibility= View.INVISIBLE
    }
    fun hidePR(){
        binding.moviePR.visibility = View.GONE
        binding.movieFragment.visibility= View.VISIBLE
    }



}
