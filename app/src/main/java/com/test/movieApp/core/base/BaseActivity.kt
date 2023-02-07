package com.test.movieApp.core.base

import android.view.View
import androidx.annotation.LayoutRes
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.MutableLiveData
import com.test.movieApp.core.utils.Constant
import com.test.movieApp.core.utils.hideView
import com.test.movieApp.core.utils.showView

abstract class BaseActivity : AppCompatActivity() {

    protected inline fun <reified T : ViewDataBinding> activityBinding(@LayoutRes resId: Int): Lazy<T> =
        lazy { DataBindingUtil.setContentView(this, resId) }

    var usedView = MutableLiveData<MutableList<View?>>()

    private fun setSelectedLayout(position: Int) {
        // 0 = Loading, 1 = Main, 2 = No Data, 3 = Error, 4 = Time Out
        for (i in 0 until usedView.value?.size!!) {
            when (i) {
                position -> usedView.value?.get(i)?.showView()
                else -> usedView.value?.get(i)?.hideView()
            }
        }
    }

    fun setBaseLayout(currentLayout: String) {
        when (currentLayout) {
            Constant.loading -> setSelectedLayout(0)
            Constant.main -> setSelectedLayout(1)
            Constant.no_data -> setSelectedLayout(2)
            Constant.error -> setSelectedLayout(3)
            Constant.timeOut -> setSelectedLayout(4)
        }
    }

    abstract fun initListener()
    abstract fun initController()
    abstract fun observe()

}