package com.test.gamesapp.utils

import android.annotation.SuppressLint
import android.content.Context
import android.view.View
import android.widget.ImageView
import androidx.core.app.ComponentActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.example.lawencontest.R
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import okhttp3.ResponseBody
import org.json.JSONObject
import java.io.*
import java.text.SimpleDateFormat
import java.util.*

fun View.showView() {
    visibility = View.VISIBLE
}

fun View.hideView() {
    visibility = View.GONE
}



fun convertErrorMessage(errorBody: ResponseBody?, getFrom: String = "status_message"): String {
    return JSONObject(errorBody!!.charStream().readText()).getString(getFrom)
}
fun ImageView.loadImage(context: Context, url: String){
    Glide.with(context)
        .load(url)
        .placeholder(R.drawable.ic_dummy_background)
        .error(R.color.colorSoftGrey)
        .diskCacheStrategy(DiskCacheStrategy.ALL)
        .thumbnail(
            Glide.with(context)
            .load(R.drawable.ic_dummy_background))
        .into(this)

}

fun <T> ComponentActivity.collectLatestLifecycleFlow(flow: Flow<T>, collect: suspend (T) -> Unit){
    lifecycleScope.launch {
        repeatOnLifecycle(Lifecycle.State.STARTED){
            flow.collectLatest(collect)
        }
    }
}

@SuppressLint("SimpleDateFormat")
fun convertDateFormat(
    current: String,
    oldFormat: String, newFormat: String
): String {
    return try {
        var dateFormat = SimpleDateFormat(oldFormat)
        val newDate = dateFormat.parse(current)
        dateFormat = SimpleDateFormat(newFormat)
        dateFormat.format(newDate ?:Date())
    } catch (e: Exception) {
        ""
    }

}
