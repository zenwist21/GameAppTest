package com.test.movieApp.feature_movie.presentation.main

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.test.gamesapp.R
import com.test.gamesapp.databinding.ActivityMainMenuMovieBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainMenuMovieActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainMenuMovieBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainMenuMovieBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setBottomNav()
    }

    private fun setBottomNav() {
        val navView: BottomNavigationView = binding.navView
        val navController = findNavController(R.id.navigation)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        navView.setupWithNavController(navController)
        navView.itemIconTintList = null
    }
}