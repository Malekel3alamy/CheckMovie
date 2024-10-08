package com.example.movies.adapter

import android.annotation.SuppressLint
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.bumptech.glide.Glide
import com.example.movies.R
import com.example.movies.models.Movie

class BookmarksAdapter : RecyclerView.Adapter<BookmarksAdapter.MyViewHolder>() {
    class MyViewHolder (val view : View) : ViewHolder(view){

        val movieImage = view.findViewById<ImageView>(R.id.movie_imageBookmark)
        val title      = view.findViewById<TextView>(R.id.movie_titleBookmark)
        val date   = view.findViewById<TextView>(R.id.dateBookmark)
        val tvRating = view.findViewById<TextView>(R.id.movieRate)
        val views = view.findViewById<TextView>(R.id.movieViews)
        val genre = view.findViewById<TextView>(R.id.movieGenre)


    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.bookmarks_item,parent,false)

        return MyViewHolder(itemView)
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }


    @SuppressLint("DefaultLocale")
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {

        val movie = differ.currentList[position]
if (movie != null){
    holder.title.text = movie.title
    Log.d("MovieName",movie.title.toString())
    holder.date.text = movie.release_date
    val average = movie.vote_average?.toFloat()
    holder.tvRating.text = String.format("%1.1f",average)
    holder.views.text = movie.popularity.toString()
    Glide.with(holder.itemView).load("https://image.tmdb.org/t/p/w500/${movie.poster_path}").into(holder.movieImage)

    holder.itemView. setOnClickListener{
        onMovieClick?.invoke(movie)
    }
}

    }

    var onMovieClick : ((Movie) -> Unit)? = null




     private val differCallback = object : DiffUtil.ItemCallback<Movie>() {
         override fun areContentsTheSame(oldItem: Movie, newItem: Movie): Boolean {
             return oldItem.title == newItem.title
         }

         override fun areItemsTheSame(oldItem: Movie, newItem: Movie): Boolean {
             return oldItem == newItem
         }

     }




    val differ = AsyncListDiffer(this, differCallback)



}