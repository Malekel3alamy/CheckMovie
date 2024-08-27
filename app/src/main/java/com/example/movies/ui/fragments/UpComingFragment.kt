package com.example.movies.ui.fragments

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.movies.R
import com.example.movies.adapter.MovieRecyclerAdapter
import com.example.movies.databinding.FragmentUpComingBinding
import com.example.movies.ui.MainActivity
import com.example.movies.ui.MoviesViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class UpComingFragment : Fragment(R.layout.fragment_up_coming) {
 /*   val moviesViewModel: MoviesViewModel by viewModels()
    lateinit var binding: FragmentUpComingBinding
    lateinit var moviesAdapter : MovieRecyclerAdapter

    lateinit var retryButton : Button
    lateinit var errorText : TextView
    lateinit var itemError: CardView


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentUpComingBinding.bind(view)
        (activity as MainActivity).showToolbarAndNavigationView()
        setUpRecycler()


        itemError = view.findViewById(R.id.itemMoviesError)
        val inflater =
            requireContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val ite_view_error = inflater.inflate(R.layout.item_error, null)

        retryButton = ite_view_error.findViewById(R.id.retryButton)
        errorText = ite_view_error.findViewById(R.id.errorText)



       moviesAdapter.onMovieClick={ movie ->

           val bundle = Bundle().apply {
               if (movie.id != null)
                   putParcelable("movie", movie)
           }
           findNavController().navigate(R.id.action_upComingFragment_to_moviesFragment,bundle)
       }




        if (moviesViewModel.internetConnection((activity as MainActivity).applicationContext)){
            lifecycleScope.launch {
                moviesViewModel.upcomingMovies.collectLatest {
                       moviesAdapter.submitData(it)
                }
            }

        }else{

       /*     moviesViewModel.getAllMoviesFromRoom().observe(viewLifecycleOwner, Observer {

                moviesAdapter.differ.submitList(it.toList())
            })*/
        }

        retryButton.setOnClickListener {


        }
    }

    private fun setUpRecycler(){
        moviesAdapter = MovieRecyclerAdapter()

        binding.recyclerUpcoming.apply {
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

*/
}