package com.test.gamesapp.feature_movie.presentation.main.ui.tv

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.RecyclerView
import com.test.gamesapp.feature_movie.presentation.component.adapter.AdapterTVShow
import com.test.gamesapp.feature_movie.presentation.detail.DetailMovieActivity
import com.test.gamesapp.core.base.BaseFragment
import com.test.gamesapp.databinding.FragmentTvBinding
import com.test.gamesapp.core.data.model.GenreModel
import com.test.gamesapp.core.data.model.TmDbModel
import com.test.gamesapp.feature_movie.util.DetailType
import com.test.gamesapp.core.utils.Constant
import com.test.gamesapp.core.utils.collectLatestLifecycleFlow
import com.test.gamesapp.core.utils.hideView
import com.test.gamesapp.core.utils.showView
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject


@AndroidEntryPoint
class TvFragment : BaseFragment() {

    @Inject
    lateinit var adapterTvShow: AdapterTVShow
    private var _binding: FragmentTvBinding? = null
    private val binding get() = _binding!!
    private val viewModel: TVViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentTvBinding.inflate(inflater, container, false)
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
        binding.rvList.adapter = adapterTvShow
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
            adapterGenre.setOnClickListener { data ->
                if (data != null) {
                    viewModel.setGenre((data as GenreModel).id)
                    viewModel.getTvList()
                } else {
                    viewModel.setGenre(null)
                    viewModel.getTvList()
                }
            }
            adapterTvShow.setOnItemClickListener { data ->
                Intent(requireActivity(), DetailMovieActivity::class.java).also {
                    it.putExtra(Constant.itemID, (data as TmDbModel).id)
                    it.putExtra(Constant.itemTYPE, DetailType.TV_SHOW.name)
                    startActivity(it)
                }
            }
            srlMain.setOnRefreshListener {
                if (srlMain.isRefreshing) {
                    viewModel.getTvList()
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
        requireActivity().collectLatestLifecycleFlow(viewModel.state) { state ->
            if (state.genreLoading) {
                adapterGenre.setViewLoading(true)
            } else {
                adapterGenre.setViewLoading(false)
                adapterGenre.differ.submitList(emptyList())
                adapterGenre.differ.submitList(state.listGenre)
            }
            adapterTvShow.setViewLoading(state.listLoading)
            adapterTvShow.changeIsLoading(state.loadingNextPage)
            state.listTvShow.let { data ->
                if (data.isEmpty()) {
                    adapterTvShow.setViewError(true, Constant.NO_DATA_EXIST)
                } else {
                    adapterTvShow.differ.submitList(data)
                }
            }
            adapterTvShow.setViewError(!state.errorTvShow.isNullOrEmpty(), state.errorTvShow)
            adapterGenre.setViewError(!state.errorGenre.isNullOrEmpty())
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding?.unbind()
        _binding = null
    }


}