package com.alhamoly.redditnews.ui.news

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.alhamoly.redditnews.databinding.ItemNewsBinding
import com.alhamoly.redditnews.pojo.response.NewsResponse
import com.alhamoly.redditnews.utils.MediaUtils.loadUrl
import javax.inject.Inject


class NewsAdapter @Inject constructor() :
    ListAdapter<NewsResponse.Children, NewsAdapter.NewsViewHolder>(DiffCallback()) {

    inner class NewsViewHolder(private val binding: ItemNewsBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(news: NewsResponse.Children, position: Int) {

            val title = news.data.title
            if (title.length < 100) binding.tvTitle.text = title
            else {
                binding.tvTitle.text = "${title.subSequence(0, 100)}..."
            }


            val url :String? = news.data.secure_media?.oembed?.thumbnail_url
            if (url != null) {
                Log.d(TAG, "bind: $url")
                binding.ivImg.visibility = View.VISIBLE
                binding.ivImg.loadUrl(url)
            }else{
                binding.ivImg.visibility = View.GONE
            }


            binding.root.setOnClickListener {
                onItemClickListener?.let { click ->
                    click(news)
                }
            }


        }

    }

    class DiffCallback : DiffUtil.ItemCallback<NewsResponse.Children>() {
        override fun areItemsTheSame(
            oldItem: NewsResponse.Children, newItem: NewsResponse.Children
        ): Boolean = newItem == oldItem

        override fun areContentsTheSame(
            oldItem: NewsResponse.Children, newItem: NewsResponse.Children
        ): Boolean = newItem == oldItem
    }

    override fun onCreateViewHolder(parent: ViewGroup, Type: Int): NewsViewHolder = NewsViewHolder(
        ItemNewsBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
    )

    override fun onBindViewHolder(holder: NewsViewHolder, position: Int) {
        holder.bind(getItem(position), position)
    }

    private var onItemClickListener: ((NewsResponse.Children) -> Unit)? = null

    fun setOnItemClickListener(listener: (NewsResponse.Children) -> Unit) {
        onItemClickListener = listener
    }

    companion object {
        const val TAG = "NewsAdapter"
    }
}