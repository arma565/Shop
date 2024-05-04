package com.store.shop.view.fragment.home.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.store.shop.R
import com.store.shop.data.model.New
import com.store.shop.databinding.NewsRowBinding

class NewsAdapter(private val list: List<New>) :
    RecyclerView.Adapter<NewsAdapter.NewsVH>() {

    private lateinit var binding: NewsRowBinding
    private lateinit var parent: ViewGroup

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NewsVH {
        this.parent = parent
        binding = DataBindingUtil.bind(
            LayoutInflater.from(parent.context).inflate(R.layout.news_row, parent, false)
        )!!
        return NewsVH(binding)
    }

    class NewsVH(binding: NewsRowBinding) : RecyclerView.ViewHolder(binding.root)

    override fun getItemCount(): Int = list.size


    override fun onBindViewHolder(holder: NewsVH, position: Int) {
        val news = list[position]
        binding.news = news
        Glide.with(binding.root).load(news.icon).into(binding.imgNews)

    }
}