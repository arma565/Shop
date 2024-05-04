package com.store.shop.view.fragment.productCategory.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.store.shop.R
import com.store.shop.data.model.GlobalFunctions
import com.store.shop.data.model.Product
import com.store.shop.databinding.ProductCategoryRowBinding

class ProductCategoryAdapter(private val activity: FragmentActivity, private val list: List<Product>) : RecyclerView.Adapter<ProductCategoryAdapter.ProductCategoryVH>() {

    private lateinit var binding : ProductCategoryRowBinding



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductCategoryVH {
        binding = DataBindingUtil.bind(LayoutInflater.from(parent.context).inflate(R.layout.product_category_row,parent,false))!!
        return ProductCategoryVH(binding)
    }

    class ProductCategoryVH(binding: ProductCategoryRowBinding) : RecyclerView.ViewHolder(binding.root)

    override fun getItemCount(): Int = list.size

    override fun onBindViewHolder(holder: ProductCategoryVH, position: Int) {
        val product = list[position]
        binding.product = product

        Glide.with(activity).load(product.icon).into(binding.imgProduct)

        binding.cardView.setOnClickListener {
            GlobalFunctions.getNavControllerFragment(activity).navigate(
                R.id.action_productCategoryFragment_to_productDetailsFragment,
                bundleOf("product" to product)
            )
        }
    }
}