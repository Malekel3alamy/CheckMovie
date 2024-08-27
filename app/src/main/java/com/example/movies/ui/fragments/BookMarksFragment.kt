package com.example.movies.ui.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.movies.R
import com.example.movies.adapter.BookmarksAdapter
import com.example.movies.adapter.NowPlayingMoviesAdapter
import com.example.movies.databinding.FragmentBookMarksBinding
import com.example.movies.ui.MainActivity
import com.example.movies.ui.MoviesViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch


@AndroidEntryPoint
class BookMarksFragment : Fragment(R.layout.fragment_book_marks) {
        private lateinit var binding : FragmentBookMarksBinding
    private lateinit var bookmarksAdapter :BookmarksAdapter
    val moviesViewModel: MoviesViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentBookMarksBinding.bind(view)
        (activity as MainActivity).hideToolbarAndNavigationView()

        setUpRecycler()

        bookmarksAdapter.onMovieClick={ movie ->

            val bundle = Bundle().apply {
                if (movie.id != null)
                    putParcelable("movie",movie)
            }
            findNavController().navigate(R.id.action_bookMarksFragment_to_moviesFragment,bundle)
        }

        lifecycleScope.launch {
            moviesViewModel.getAllMoviesFromRoom()
            if(moviesViewModel.roomMovies != null){
                moviesViewModel.roomMovies?.observe(viewLifecycleOwner, Observer {

                    bookmarksAdapter.differ.submitList(it)
                })
            }

        }


    }
    private fun setUpRecycler(){
        bookmarksAdapter = BookmarksAdapter()

        binding.bookmarksRV.apply {
            adapter = bookmarksAdapter
            layoutManager = LinearLayoutManager(activity)

        }
    }

    override fun onStart() {
        super.onStart()
        bookmarksAdapter.notifyDataSetChanged()
    }
}