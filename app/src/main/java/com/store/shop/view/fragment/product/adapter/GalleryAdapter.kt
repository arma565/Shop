package com.store.shop.view.fragment.product.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.store.shop.R
import com.store.shop.data.model.Gallery
import com.store.shop.databinding.GalleryRowBinding

class GalleryAdapter(private val activity : FragmentActivity ,private val list : List<Gallery>) : RecyclerView.Adapter<GalleryAdapter.GalleryVH>() {

    private lateinit var binding: GalleryRowBinding

    class GalleryVH(binding : GalleryRowBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GalleryVH {
       binding = DataBindingUtil.bind(LayoutInflater.from(parent.context).inflate(R.layout.gallery_row,parent,false))!!
        return GalleryVH(binding)
    }

    override fun getItemCount(): Int = list.size

    override fun onBindViewHolder(holder: GalleryVH, position: Int) {
        val gallery = list[position]
        binding.gallery = gallery
        Glide.with(activity).load(gallery.img).into(binding.imgGalleryImg)
    }
}