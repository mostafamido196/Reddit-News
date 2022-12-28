package com.alhamoly.redditnews.ui.details

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.FragmentActivity
import com.alhamoly.redditnews.R
import com.alhamoly.redditnews.databinding.FragmentDetailsBinding
import com.alhamoly.redditnews.ui.news.NewsAdapter
import com.alhamoly.redditnews.utils.Constants
import com.alhamoly.redditnews.utils.MediaUtils.loadUrl
import com.alhamoly.redditnews.utils.Utils
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DetailsFragment : Fragment() {

    private lateinit var binding: FragmentDetailsBinding


    private lateinit var details: String
    private lateinit var title: String
    private var url: String? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_details, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setup()
    }

    private fun setup() {
        details = requireArguments().getSerializable(Constants.DETAILS) as String
        title = requireArguments().getSerializable(Constants.TITLE) as String
        url = requireArguments().getSerializable(Constants.URL) as String?

        activity?.title = title

        binding.tvDetails.text = details

        if (url != null) {
            Log.d(TAG, "setup: $url")
            if (!Utils.isInternetAvailable()) {
                Log.d(TAG, "setup: isInternetAvailable")
                binding.ivImg.loadUrl(url!!)
            } else {
                Log.d(TAG, "setup: no isInternetAvailable")
                Toast.makeText(context, "Check the internet connection", Toast.LENGTH_SHORT).show()
            }
        } else {
            binding.ivImg.visibility = View.GONE
        }

    }


    companion object {
        private val TAG = this::class.java.name
    }
}