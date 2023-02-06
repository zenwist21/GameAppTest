package com.test.gamesapp.core.base

import androidx.fragment.app.Fragment

abstract class BaseFragment : Fragment() {

    abstract fun initController()
    abstract fun observer()
    abstract fun initListener()
    abstract fun setupAdapter()

}