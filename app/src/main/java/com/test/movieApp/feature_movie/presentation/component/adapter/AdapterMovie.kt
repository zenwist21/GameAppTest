package com.test.movieApp.feature_movie.presentation.component.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.test.gamesapp.R
import com.test.gamesapp.databinding.ItemListErrorBinding
import com.test.gamesapp.databinding.ListItemLoadingNextBinding
import com.test.gamesapp.databinding.ListItemMovieBinding
import com.test.gamesapp.databinding.ListItemMovieSkeletonBinding
import com.test.movieApp.core.data.model.TmDbModel
import com.test.movieApp.core.utils.Constant.IMAGE_URL
import com.test.movieApp.core.utils.Constant.NO_DATA_EXIST
import com.test.movieApp.core.utils.Constant.RECYCLER_VIEW_ERROR
import com.test.movieApp.core.utils.Constant.RECYCLER_VIEW_LOADING
import com.test.movieApp.core.utils.Constant.RECYCLER_VIEW_LOADING_NEXT
import com.test.movieApp.core.utils.Constant.RECYCLER_VIEW_SUCCESS
import com.test.movieApp.core.utils.convertDateFormat
import com.test.movieApp.core.utils.loadImage

class AdapterMovie :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var onItemClick: ((Any?) -> Unit)? = null
    private var isLoading = false
    private var isNextLoading = false
    private var isError = false

    private val diffCallBack = object : DiffUtil.ItemCallback<TmDbModel>() {
        override fun areItemsTheSame(
            oldItem: TmDbModel,
            newItem: TmDbModel
        ): Boolean =
            oldItem == newItem

        override fun areContentsTheSame(
            oldItem: TmDbModel,
            newItem: TmDbModel
        ): Boolean =
            oldItem == newItem

        override fun getChangePayload(
            oldItem: TmDbModel,
            newItem: TmDbModel
        ): Any {
            return oldItem == newItem
        }

    }
    val differ = AsyncListDiffer(this, diffCallBack)

    inner class Loading(
        val parent: ViewGroup,
        binding: ListItemMovieSkeletonBinding
    ) : RecyclerView.ViewHolder(binding.root)

    inner class LoadingNextItem(
        val parent: ViewGroup,
        binding: ListItemLoadingNextBinding
    ) : RecyclerView.ViewHolder(binding.root)

    inner class Item(val parent: ViewGroup, private val binding: ListItemMovieBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(data: TmDbModel) {
            binding.apply {
                parent.context.let { ctx ->
                    /** set data to view **/
                    tvMovieName.text = data.title
                    tvRating.text = ctx.getString(R.string.ratingWValue, data.popularity.toString())
                    tvReleaseDate.text = ctx.getString(
                        R.string.releaseDateWVal,
                        convertDateFormat(data.release_date.toString(), "yyyy-MM-dd", "dd MMM yyyy")
                    )
                    if (!data.poster_path.isNullOrEmpty()) ivBackground.loadImage(
                        ctx,
                        IMAGE_URL + data.poster_path
                    )
                    else ivBackground.setImageDrawable(
                        ContextCompat.getDrawable(
                            ctx,
                            R.drawable.ic_dummy_background
                        )
                    )
                    cvMain.setOnClickListener {
                        onItemClick?.invoke(data)
                    }
                }
            }
        }
    }

    inner class ItemEmpty(val parent: ViewGroup, private val binding: ItemListErrorBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(data: TmDbModel) {
            binding.apply {
                parent.context.let { ctx ->
                    /** set data to view **/
                    tvMessage.text =
                        if (data.message == NO_DATA_EXIST) ctx.getString(R.string.no_data_exist)
                        else data.message
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        when (viewType) {
            RECYCLER_VIEW_SUCCESS -> {
                return Item(
                    parent,
                    ListItemMovieBinding.inflate(
                        LayoutInflater.from(parent.context),
                        parent,
                        false
                    )
                )
            }
            RECYCLER_VIEW_LOADING -> {
                return Loading(
                    parent,
                    ListItemMovieSkeletonBinding.inflate(
                        LayoutInflater.from(parent.context),
                        parent,
                        false
                    )
                )
            }
            RECYCLER_VIEW_LOADING_NEXT -> {
                return LoadingNextItem(
                    parent,
                    ListItemLoadingNextBinding.inflate(
                        LayoutInflater.from(parent.context),
                        parent,
                        false
                    )
                )
            }
            else -> {
                return ItemEmpty(
                    parent,
                    ItemListErrorBinding.inflate(
                        LayoutInflater.from(parent.context),
                        parent,
                        false
                    )
                )
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        return if (isLoading) {
            RECYCLER_VIEW_LOADING
        } else if (position == differ.currentList.lastIndex && isNextLoading) {
            RECYCLER_VIEW_LOADING_NEXT
        } else if (isError) {
            RECYCLER_VIEW_ERROR
        } else {
            RECYCLER_VIEW_SUCCESS
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is Item -> {
                holder.bind(differ.currentList[position])
            }
            is Loading ->{
                holder.adapterPosition
            }
            is LoadingNextItem ->{
                holder.adapterPosition
            }
            is ItemEmpty -> {
                holder.bind(differ.currentList[position])
            }
        }
    }

    override fun getItemCount(): Int = differ.currentList.size



    fun setViewLoading(state: Boolean) {
        isLoading = state
        notifyItemRangeChanged(0, differ.currentList.size)
    }

    fun changeIsLoading(value: Boolean) {
        isNextLoading = value
        notifyItemRangeChanged(0, differ.currentList.size)
    }

    fun setViewError(state: Boolean, message: String? = "") {
        isError = state
        if (isError) {
            differ.submitList(mutableListOf(TmDbModel(message = message)))
        }
    }

    fun setOnClickListener(listener: (Any?) -> Unit) {
        onItemClick = listener
    }
}