package com.test.gamesapp.feature_movie.presentation.reviews

import android.os.Bundle
import androidx.activity.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.RecyclerView
import com.test.gamesapp.core.base.BaseActivity
import com.test.gamesapp.databinding.ActivityReviewBinding
import com.test.gamesapp.core.data.model.ReviewDataModel
import com.test.gamesapp.feature_movie.presentation.component.adapter.AdapterReview
import com.test.gamesapp.feature_movie.util.DetailType
import com.test.gamesapp.core.utils.Constant
import com.test.gamesapp.core.utils.hideView
import com.test.gamesapp.core.utils.showView
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class ReviewActivity : BaseActivity() {
    private var _binding: ActivityReviewBinding? = null
    private val binding get() = _binding!!
    private val viewModel: ReviewViewModel by viewModels()
    @Inject
    lateinit var adapter: AdapterReview

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityReviewBinding.inflate(layoutInflater)
        binding.vm = viewModel
        initController()
        setContentView(binding.root)
    }

    override fun initController() {
        viewModel.setMovieId(
            intent.getIntExtra(Constant.itemID, 0),
            DetailType.valueOf(intent.extras?.getString(Constant.itemTYPE) ?:DetailType.MOVIE.name)
        )
        viewModel.getData()
        usedView.value = mutableListOf(
            binding.iLoading.llcLoading,
            binding.llcMain,
            null,
            binding.iError.llcError,
            null
        )
        initListener()
        initAdapter()
        observe()
    }

    override fun observe() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.apply {
                    viewModel.currentLayout.observe(this@ReviewActivity) {
                        setBaseLayout(it)
                    }
                    state.collectLatest {
                        adapter.changeIsLoading(it.loadingNextPage)
                        if (it.initialLoading) setCurrentLayoutStage(0)
                        else {
                            setCurrentLayoutStage(200)
                            it.listReview.let { data ->
                                if (data.isEmpty()) {
                                    adapter.setViewError(true, Constant.NO_DATA_EXIST)
                                } else {
                                    val temp = data as MutableList<ReviewDataModel>
                                    temp.remove(ReviewDataModel(author = Constant.DUMMY))
                                    adapter.setViewError(false)
                                    adapter.differ.submitList(temp)
                                }
                            }
                        }
                        if (!it.error.isNullOrEmpty()){
                            if (it.error != Constant.NO_DATA_EXIST){
                                setCurrentLayoutStage(500)
                            }
                        }


                    }
                }
            }
        }
    }

    private fun initAdapter() {
        binding.rvList.adapter = adapter
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
        binding.iToolbar.ivBack.setOnClickListener {
            this.finish()
        }
        binding.btnToTop.setOnClickListener{
            binding.rvList.smoothScrollToPosition(0)
        }
        binding.srlMain.setOnRefreshListener {
            if (binding.srlMain.isRefreshing) {
                viewModel.getData()
                lifecycleScope.launch {
                    delay(1000)
                    binding.srlMain.isRefreshing = false
                }
            }
        }
    }
}