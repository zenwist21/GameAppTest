package com.test.movieApp.feature_movie.presentation.splash

import android.content.Intent
import android.os.Bundle
import androidx.lifecycle.lifecycleScope
import com.test.gamesapp.R
import com.test.gamesapp.databinding.ActivitySplashBinding
import com.test.movieApp.core.base.BaseActivity
import com.test.movieApp.feature_movie.presentation.main.MainMenuMovieActivity
import kotlinx.coroutines.delay


class Splash : BaseActivity() {
    private val binding: ActivitySplashBinding by activityBinding(R.layout.activity_splash)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initController()
        setContentView(binding.root)
    }
    override fun initController() {
        lifecycleScope.launchWhenStarted {
            delay(2000)
            Intent(this@Splash, MainMenuMovieActivity::class.java).also {
                startActivity(it)
                this@Splash.finish()
            }
        }
    }

    override fun initListener() {

    }


    override fun observe() {

    }

    override fun onDestroy() {
        super.onDestroy()
        binding.unbind()
    }
}