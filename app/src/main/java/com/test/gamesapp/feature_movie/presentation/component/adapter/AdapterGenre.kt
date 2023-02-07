package com.test.gamesapp.feature_movie.presentation.component.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.test.gamesapp.R
import com.test.gamesapp.databinding.ItemListErrorBinding
import com.test.gamesapp.databinding.ListItemGenreBinding
import com.test.gamesapp.databinding.ListItemGenreSkeletonBinding
import com.test.gamesapp.core.data.model.GenreModel
import com.test.gamesapp.core.utils.Constant.NO_DATA_EXIST
import com.test.gamesapp.core.utils.Constant.RECYCLER_VIEW_ERROR
import com.test.gamesapp.core.utils.Constant.RECYCLER_VIEW_LOADING
import com.test.gamesapp.core.utils.Constant.RECYCLER_VIEW_SUCCESS
import com.test.gamesapp.core.utils.getDummyGenres

class AdapterGenre :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var onItemClick: ((Any?) -> Unit)? = null
    private var isLoading = false
    private var isError = false

    private val diffCallBack = object : DiffUtil.ItemCallback<GenreModel>() {
        override fun areItemsTheSame(
            oldItem: GenreModel,
            newItem: GenreModel
        ): Boolean =
            oldItem == newItem

        override fun areContentsTheSame(
            oldItem: GenreModel,
            newItem: GenreModel
        ): Boolean =
            oldItem == newItem

        override fun getChangePayload(
            oldItem: GenreModel,
            newItem: GenreModel
        ): Any {
            return oldItem == newItem
        }

    }
    val differ = AsyncListDiffer(this, diffCallBack)

    inner class Loading(
        val parent: ViewGroup,
        binding: ListItemGenreSkeletonBinding
    ) : RecyclerView.ViewHolder(binding.root)

    inner class Item(val parent: ViewGroup, private val binding: ListItemGenreBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(data: GenreModel) {
            binding.apply {
                parent.context.let { ctx ->
                    /** set data to view **/
                    llcOptionAll.background = ContextCompat.getDrawable(
                        ctx,
                        if (data.isSelected) R.drawable.background_rounded_accent_blue else R.drawable.background_rounded_transparent

                    )
                    tvGenre.text = data.name
                    llcOptionAll.setOnClickListener {
                        if (!data.isSelected) {
                            setSelectedPosition(data)
                            onItemClick?.invoke(data)
                        } else {
                            clearSelection()
                            onItemClick?.invoke(null)
                        }
                    }
                }
            }
        }
    }

    inner class ItemEmpty(val parent: ViewGroup, private val binding: ItemListErrorBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(data: GenreModel) {
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
                    ListItemGenreBinding.inflate(
                        LayoutInflater.from(parent.context),
                        parent,
                        false
                    )
                )
            }
            RECYCLER_VIEW_LOADING -> {
                return Loading(
                    parent,
                    ListItemGenreSkeletonBinding.inflate(
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
        if (isLoading) differ.submitList(getDummyGenres())
    }

    fun setViewError(state: Boolean, message: String? = "") {
        isError = state
        if (isError) {
            differ.submitList(mutableListOf(GenreModel(message = message)))
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    fun setSelectedPosition(data: GenreModel) {
        for (i in 0 until differ.currentList.size) {
            differ.currentList[i].isSelected = data == differ.currentList[i]
        }
        notifyDataSetChanged()
    }

    @SuppressLint("NotifyDataSetChanged")
    fun clearSelection() {
        for (i in 0 until differ.currentList.size) {
            differ.currentList[i].isSelected = false
        }
        notifyDataSetChanged()
    }

    fun setOnClickListener(listener: (Any?) -> Unit) {
        onItemClick = listener
    }

}