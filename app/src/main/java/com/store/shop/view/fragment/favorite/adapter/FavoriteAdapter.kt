package com.store.shop.view.fragment.favorite.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.store.shop.R
import com.store.shop.data.config.FavoriteProductConfig
import com.store.shop.data.model.GlobalFunctions
import com.store.shop.data.model.Product
import com.store.shop.databinding.FavRowBinding

class FavoriteAdapter(
    private val activity: FragmentActivity,
    private val list: List<Product>
) :
    RecyclerView.Adapter<FavoriteAdapter.FavoriteVH>() {

    private lateinit var binding: FavRowBinding

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavoriteVH {
        binding = DataBindingUtil.bind(
            LayoutInflater.from(parent.context).inflate(R.layout.fav_row, parent, false)
        )!!
        return FavoriteVH(binding)
    }

    class FavoriteVH(binding: FavRowBinding) : RecyclerView.ViewHolder(binding.root)

    override fun getItemCount(): Int = list.size

    override fun onBindViewHolder(holder: FavoriteVH, position: Int) {
        val product = list[position]
        binding.product = product


        Glide.with(activity).load(product.icon).into(binding.imgProduct)

        binding.cardView.setOnClickListener {
            GlobalFunctions.getNavControllerFragment(activity).navigate(
                R.id.action_favoriteFragment_to_productDetailsFragment,
                bundleOf("product" to product)
            )
        }
    }

}