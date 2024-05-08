package com.store.shop.view.fragment.home.adapter

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
import com.store.shop.databinding.HomeRowBinding

class HomeAdapter(private val activity: FragmentActivity, private val list: List<Product>) :
    RecyclerView.Adapter<HomeAdapter.HomeVH>() {


    private lateinit var binding: HomeRowBinding

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HomeVH {
        binding = DataBindingUtil.bind(
            LayoutInflater.from(parent.context).inflate(R.layout.home_row, parent, false)
        )!!
        return HomeVH(binding)
    }

    class HomeVH(binding: HomeRowBinding) : RecyclerView.ViewHolder(binding.root)

    override fun getItemCount(): Int = list.size

    override fun onBindViewHolder(holder: HomeVH, position: Int) {
        val product = list[position]
        binding.product = product


        Glide.with(activity).load(product.icon).into(binding.imgProduct)

        binding.cardView.setOnClickListener {
            GlobalFunctions.getNavControllerFragment(activity).navigate(
                R.id.action_homeFragment_to_productDetailsFragment,
                bundleOf("product" to product)
            )
        }
    }
}