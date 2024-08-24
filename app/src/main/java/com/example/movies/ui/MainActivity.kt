package com.example.movies.ui

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import androidx.work.BackoffPolicy
import androidx.work.Constraints
import androidx.work.NetworkType
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import com.example.movies.R
import com.example.movies.databinding.ActivityMainBinding
import com.example.movies.repo.MoviesRepo
import com.example.movies.room.MoviesDatabase
import com.example.movies.ui.fragments.MoviesFragment
import com.example.movies.worker.UpdateWorker
import dagger.hilt.android.AndroidEntryPoint
import java.time.Duration
import java.util.concurrent.TimeUnit

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
     val moviesViewModel: MoviesViewModel by viewModels()
     lateinit var navController: NavController


    @RequiresApi(Build.VERSION_CODES.O)
    fun initWorker(){
        val consttraints = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.CONNECTED)
            .build()
        val periodicWorkRequest = PeriodicWorkRequestBuilder<UpdateWorker>(
            4, TimeUnit.HOURS)
            .setBackoffCriteria(
                backoffPolicy = BackoffPolicy.LINEAR,
                duration = Duration.ofSeconds(15)
            ).setConstraints(consttraints)
            .build()

        val workManager = WorkManager.getInstance(applicationContext)
        workManager.enqueue(periodicWorkRequest)
    }
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        showToolbarAndNavigationView()

        initWorker()

        // NavController
        val navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        navController = navHostFragment.navController
        binding.bottomNavigationView.setupWithNavController(navController)

        binding.searchIcon.setOnClickListener {
            val intent = Intent(this, SearchActvity::class.java)
            startActivity(intent)
        }

    }
 fun hideToolbarAndNavigationView(){
     binding.bottomNavigationView.visibility = View.GONE
     binding.toolBar.visibility = View.GONE
 }

    fun showToolbarAndNavigationView(){
        binding.bottomNavigationView.visibility = View.VISIBLE
        binding.toolBar.visibility = View.VISIBLE
    }



}