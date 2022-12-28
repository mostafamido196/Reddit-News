package com.alhamoly.redditnews.ui.news

import android.content.Context
import android.opengl.Visibility
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import com.alhamoly.redditnews.R
import com.alhamoly.redditnews.databinding.FragmentNewsBinding
import com.alhamoly.redditnews.pojo.response.NewsResponse
import com.alhamoly.redditnews.utils.Constants
import com.alhamoly.redditnews.utils.NetworkState
import com.alhamoly.redditnews.utils.Utils
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class NewsFragment : Fragment() {

    private lateinit var binding: FragmentNewsBinding
    private val viewModel: NewsViewModel by viewModels()


    @Inject
    lateinit var newsAdapter: NewsAdapter

    val mActivity: FragmentActivity by lazy {
        requireActivity()
    }

    private val mContext: Context by lazy {
        requireContext()
    }

    private val navController: NavController by lazy {
        findNavController()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_news, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        onBack()
        setup()
        observe()
        data()


    }

    private fun setup() {
        activity?.title = "Kotlin News"
        binding.rvNews.adapter = newsAdapter


        binding.swipeRefreshLayout.setOnRefreshListener {
            data()
        }


        newsAdapter.setOnItemClickListener {
            val bundle = Bundle()
            bundle.putSerializable(Constants.DETAILS, it.data.selftext)
            bundle.putSerializable(Constants.URL, it.data.secure_media?.oembed?.thumbnail_url)
            bundle.putSerializable(Constants.TITLE, it.data.title)
            Log.d(TAG, "setup: url: ${it.data.secure_media?.oembed?.thumbnail_url}")
            navController.navigate(R.id.detailsFragment, bundle)
        }


    }

    private fun onBack() {
        activity?.onBackPressedDispatcher?.addCallback(viewLifecycleOwner,
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    mActivity.finish()
                }
            })
    }

    private fun data() {
        if (!Utils.isInternetAvailable())
            viewModel.getNews()
        else {
            visProgress(false)
            Toast.makeText(context, "Check the internet connection", Toast.LENGTH_SHORT).show()
        }
    }

    private fun observe() {
        lifecycleScope.launchWhenStarted {
            viewModel.getNewsStateFlow.collect {
                Log.d(TAG, "observe: $it")
                when (it) {
                    is NetworkState.Idle -> {
                        return@collect
                    }
                    is NetworkState.Loading -> {
                        visProgress(true)
                    }
                    is NetworkState.Error -> {
                        visProgress(false)
                        it.handleErrors(mContext, null)
                    }
                    is NetworkState.Result<*> -> {
                        visProgress(false)
                        handleResult(it.response as NewsResponse)

                    }
                }
            }
        }
    }


    private fun <T> handleResult(response: T) {
        Log.d(TAG, "handleResult: $response")
        when (response) {
            is NewsResponse -> {
                ui(response.data.children)
            }
        }


    }

    private fun ui(children: List<NewsResponse.Children>) {
        val arr = ArrayList<NewsResponse.Children>()
        children.forEach {
            Log.d(TAG, "ui: $it")
            arr.add(it)
        }
        newsAdapter.submitList(arr)
    }

    private fun visProgress(s: Boolean) {

        if (s) {
            binding.progressLayout.startShimmer()
            binding.progressLayout.visibility = View.VISIBLE
            binding.rvNews.visibility = View.INVISIBLE
        } else {
            binding.progressLayout.stopShimmer()
            binding.progressLayout.visibility = View.GONE
            binding.rvNews.visibility = View.VISIBLE
        }
        if (binding.swipeRefreshLayout.isRefreshing && !s) {
            binding.swipeRefreshLayout.isRefreshing = false
            binding.swipeRefreshLayout.visibility = View.INVISIBLE

        }else{
            binding.swipeRefreshLayout.visibility = View.VISIBLE
        }
    }

    companion object {
        private val TAG = this::class.java.name
    }
}