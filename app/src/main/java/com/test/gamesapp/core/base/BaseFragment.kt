package com.test.gamesapp.core.base

import androidx.fragment.app.Fragment
import com.test.gamesapp.feature_movie.presentation.component.adapter.AdapterGenre
import javax.inject.Inject

abstract class BaseFragment : Fragment() {
    @Inject
    lateinit var adapterGenre: AdapterGenre
    abstract fun initController()
    abstract fun observer()
    abstract fun initListener()
    abstract fun setupAdapter()

}