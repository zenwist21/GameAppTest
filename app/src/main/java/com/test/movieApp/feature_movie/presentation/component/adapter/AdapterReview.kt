package com.test.movieApp.feature_movie.presentation.component.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.test.gamesapp.R
import com.test.gamesapp.databinding.ItemListErrorBinding
import com.test.gamesapp.databinding.ListItemLoadingNextBinding
import com.test.gamesapp.databinding.ListItemMovieSkeletonBinding
import com.test.gamesapp.databinding.ListItemReviewBinding
import com.test.movieApp.core.data.model.ReviewDataModel
import com.test.movieApp.core.utils.Constant.IMAGE_URL
import com.test.movieApp.core.utils.Constant.NO_DATA_EXIST
import com.test.movieApp.core.utils.Constant.RECYCLER_VIEW_ERROR
import com.test.movieApp.core.utils.Constant.RECYCLER_VIEW_LOADING
import com.test.movieApp.core.utils.Constant.RECYCLER_VIEW_LOADING_NEXT
import com.test.movieApp.core.utils.Constant.RECYCLER_VIEW_SUCCESS
import com.test.movieApp.core.utils.convertDateFormat
import com.test.movieApp.core.utils.loadImage

class AdapterReview :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var isLoading = false
    private var isNextLoading = false
    private var isError = false

    private val diffCallBack = object : DiffUtil.ItemCallback<ReviewDataModel>() {
        override fun areItemsTheSame(
            oldItem: ReviewDataModel,
            newItem: ReviewDataModel
        ): Boolean =
            oldItem == newItem

        override fun areContentsTheSame(
            oldItem: ReviewDataModel,
            newItem: ReviewDataModel
        ): Boolean =
            oldItem == newItem

        override fun getChangePayload(
            oldItem: ReviewDataModel,
            newItem: ReviewDataModel
        ): Any {
            return oldItem == newItem
        }

    }
    val differ = AsyncListDiffer(this, diffCallBack)

    inner class Loading(
        val parent: ViewGroup,
        private val binding: ListItemMovieSkeletonBinding
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind() {
            binding.apply {
                shimmerFrame.startShimmer()
            }
        }
    }

    inner class LoadingNextItem(
        val parent: ViewGroup,
        binding: ListItemLoadingNextBinding
    ) : RecyclerView.ViewHolder(binding.root)

    inner class Item(val parent: ViewGroup, private val binding: ListItemReviewBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(data: ReviewDataModel) {
            binding.apply {
                parent.context.let { ctx ->
                    /** set data to view **/
                    tvUserName.text = ctx.getString(
                        R.string.authorName, data.author, convertDateFormat(
                            data.created_at.toString(), "yyyy-MM-dd HH:mm:ss", "dd MMM yyyy"
                        )
                    )
                    tvReview.text = data.content
                    if (!data.author_details?.avatar_path.isNullOrEmpty())
                    profileImage.loadImage(ctx, IMAGE_URL+data.author_details?.avatar_path)
                    else profileImage.setImageDrawable(
                        ContextCompat.getDrawable(
                            ctx,
                            R.drawable.ic_profile
                        )
                    )
                }
            }
        }
    }

    inner class ItemEmpty(val parent: ViewGroup, private val binding: ItemListErrorBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(data: ReviewDataModel) {
            binding.apply {
                parent.context.let { ctx ->
                    /** set data to view **/
                    tvMessage.text =
                        if (data.author == NO_DATA_EXIST) ctx.getString(R.string.no_data_exist)
                        else data.author
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        when (viewType) {
            RECYCLER_VIEW_SUCCESS -> {
                return Item(
                    parent,
                    ListItemReviewBinding.inflate(
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
            is Loading -> {
                holder.bind()
            }
            is LoadingNextItem -> {
                holder.adapterPosition
            }
            is ItemEmpty -> {
                holder.bind(differ.currentList[position])
            }
        }
    }

    override fun getItemCount(): Int = differ.currentList.size


    @SuppressLint("NotifyDataSetChanged")
    fun changeIsLoading(value: Boolean) {
        isNextLoading = value
        notifyDataSetChanged()
    }

    fun setViewError(state: Boolean, message: String? = "") {
        isError = state
        if (isError) {
            differ.submitList(mutableListOf(ReviewDataModel(author = message)))
        }
    }

}