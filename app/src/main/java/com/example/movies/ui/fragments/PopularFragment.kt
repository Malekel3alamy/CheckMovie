package com.example.movies.ui.fragments

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.widget.AbsListView
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.cardview.widget.CardView
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.movies.R
import com.example.movies.adapter.MovieRecyclerAdapter
import com.example.movies.adapter.NowPlayingMoviesAdapter
import com.example.movies.databinding.FragmentPopularBinding
import com.example.movies.ui.MainActivity
import com.example.movies.ui.MoviesViewModel
import com.example.movies.utils.Constants
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class PopularFragment : Fragment(R.layout.fragment_popular) {
     val moviesViewModel: MoviesViewModel by viewModels()
    lateinit var binding: FragmentPopularBinding
    lateinit var moviesAdapter : MovieRecyclerAdapter

    lateinit var retryButton : Button
    lateinit var errorText : TextView
    lateinit var itemError: CardView


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentPopularBinding.bind(view)
        (activity as MainActivity).showToolbarAndNavigationView()
        setUpRecycler()

        moviesAdapter.onMovieClick={ movie ->

            val bundle = Bundle().apply {
                if (movie.id != null)
                    putInt("id", movie.id!!)
            }
            findNavController().navigate(R.id.action_popularFragment_to_moviesFragment,bundle)
        }


        itemError = view.findViewById(R.id.itemMoviesError)
        val inflater =
            requireContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val ite_view_error = inflater.inflate(R.layout.item_error, null)

        retryButton = ite_view_error.findViewById(R.id.retryButton)
        errorText = ite_view_error.findViewById(R.id.errorText)








        if (moviesViewModel.internetConnection((activity as MainActivity).applicationContext)){
        lifecycleScope.launch {
            moviesViewModel.popularMovies.collectLatest {
                      moviesAdapter.submitData(it)
            }
        }
    }else{

            moviesViewModel.getAllArticlesFromRoom().observe(viewLifecycleOwner, Observer {

                moviesAdapter.differ.submitList(it.toList())
            })
        }

        retryButton.setOnClickListener {


        }
    }

    private fun setUpRecycler(){
        moviesAdapter = MovieRecyclerAdapter()

        binding.recyclerPopulars.apply {
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
        itemError.visibility = View.INVISIBLE
        isError = false
    }
    private fun showErrorMessage(message: String){
        itemError.visibility = View.VISIBLE
        isError = true
        errorText.text = message
    }


}