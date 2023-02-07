package com.test.gamesapp.feature_movie.presentation.main.ui.movie

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.RecyclerView
import com.test.gamesapp.feature_movie.presentation.detail.DetailMovieActivity
import com.test.gamesapp.feature_movie.presentation.component.adapter.AdapterMovie
import com.test.gamesapp.core.base.BaseFragment
import com.test.gamesapp.databinding.FragmentMovieBinding
import com.test.gamesapp.core.data.model.GenreModel
import com.test.gamesapp.core.data.model.TmDbModel
import com.test.gamesapp.feature_movie.util.DetailType
import com.test.gamesapp.core.utils.Constant
import com.test.gamesapp.core.utils.Constant.NO_DATA_EXIST
import com.test.gamesapp.core.utils.collectLatestLifecycleFlow
import com.test.gamesapp.core.utils.hideView
import com.test.gamesapp.core.utils.showView
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class MovieFragment : BaseFragment() {

    @Inject
    lateinit var adapterMovie: AdapterMovie
    private var _binding: FragmentMovieBinding? = null
    private val binding get() = _binding!!
    private val viewModel: MovieViewModel by viewModels()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentMovieBinding.inflate(inflater, container, false)
        binding.vm = viewModel
        initController()
        return (binding.root)
    }

    override fun initController() {
        initListener()
        setupAdapter()
        observer()
    }

    override fun setupAdapter() {
        binding.rvGenre.adapter = adapterGenre
        binding.rvList.adapter = adapterMovie
        binding.rvList.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                if (!recyclerView.canScrollVertically(1) && dy > 0 && !viewModel.state.value.loadingNextPage) {
                    viewModel.loadMoreActivities()
                }
                if (dy > 0) { // scrolling down
                    lifecycleScope.launch {
                        delay(5000)
                        binding.btnToTop.hideView()
                    }
                } else if (dy < 0) { // scrolling up
                    binding.btnToTop.showView()
                }
            }
        })
    }

    override fun initListener() {
        binding.apply {
            adapterMovie.setOnClickListener { data ->
                Intent(requireActivity(), DetailMovieActivity::class.java).also {
                    it.putExtra(Constant.itemID, (data as TmDbModel).id)
                    it.putExtra(Constant.itemTYPE, DetailType.MOVIE.name)
                    startActivity(it)
                }
            }
            adapterGenre.setOnClickListener { data ->
                if (data != null) {
                    data as GenreModel
                    viewModel.setGenre(data.id)
                    viewModel.getMoviesList()
                } else {
                    viewModel.setGenre(null)
                    viewModel.getMoviesList()
                }
            }
            srlMain.setOnRefreshListener {
                if (srlMain.isRefreshing) {
                    viewModel.getMoviesList()
                    lifecycleScope.launch {
                        delay(1000)
                        srlMain.isRefreshing = false
                    }
                }
            }
            btnToTop.setOnClickListener {
                binding.rvList.smoothScrollToPosition(0)
                btnToTop.hideView()
            }
        }
    }

    override fun observer() {
        requireActivity().collectLatestLifecycleFlow(viewModel.state) {
            Log.e("TAG", "observer: ${it.totalPages}, ${it.currentPage}")
            if (it.genreLoading) {
                adapterGenre.setViewLoading(true)
            } else {
                adapterGenre.setViewLoading(false)
                adapterGenre.differ.submitList(emptyList())
                adapterGenre.differ.submitList(it.listGenre)
            }
            adapterMovie.setViewLoading(it.listLoading)
//            binding.srlMain.isRefreshing = it.listLoading
            adapterMovie.changeIsLoading(it.loadingNextPage)
            it.listMovie.let { data ->
                if (data.isEmpty()) {
                    adapterMovie.setViewError(true, NO_DATA_EXIST)
                } else {
                    adapterMovie.differ.submitList(data)
                }
            }
            adapterMovie.setViewError(!it.errorMovie.isNullOrEmpty(), it.errorMovie)
            adapterGenre.setViewError(!it.errorGenre.isNullOrEmpty(), it.errorGenre)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding?.unbind()
        _binding = null
    }


}