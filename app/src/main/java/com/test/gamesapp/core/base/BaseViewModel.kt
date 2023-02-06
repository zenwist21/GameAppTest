package com.test.gamesapp.core.base

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.test.gamesapp.utils.Constant

open class BaseViewModel : ViewModel() {
    var currentLayout = MutableLiveData<String>()
    fun setCurrentLayoutStage(responseCode: Int) {
        // 0 -> Loading, 200 -> main, 400, 422 -> badRequest, 401 -> unauthenticated, 500 -> Error, 998 -> No Data, 999 -> Time Out
        when(responseCode) {
            0 -> currentLayout.value = Constant.loading
            200 -> currentLayout.value = Constant.main
            400, 422 -> currentLayout.value = Constant.badRequest
            401 -> currentLayout.value = Constant.unAuthenticated
            500 -> currentLayout.value = Constant.error
            998 -> currentLayout.value = Constant.no_data
            999 -> currentLayout.value = Constant.timeOut
        }
    }
}