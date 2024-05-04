package com.store.shop.view.fragment.category.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.store.shop.R
import com.store.shop.data.model.Category
import com.store.shop.data.model.GlobalFunctions
import com.store.shop.databinding.CatRowBinding

class CategoryAdapter(private  val activity : FragmentActivity ,private val list: List<Category>) :
    RecyclerView.Adapter<CategoryAdapter.CategoryVH>() {

    private lateinit var binding: CatRowBinding

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryVH {
        binding = DataBindingUtil.bind(
            LayoutInflater.from(parent.context).inflate(R.layout.cat_row, parent, false)
        )!!
        return CategoryVH(binding)
    }

    class CategoryVH(binding: CatRowBinding) : RecyclerView.ViewHolder(binding.root)

    override fun getItemCount(): Int = list.size

    override fun onBindViewHolder(holder: CategoryVH, position: Int) {
        val category = list[position]
        binding.category = category

        Glide.with(activity).load(category.icon).into(binding.imgCategory)

        binding.cardCategory.setOnClickListener {
            GlobalFunctions.getNavControllerFragment(activity).navigate(R.id.action_categoryFragment_to_productCategoryFragment , bundleOf("category" to category))
        }
    }
}