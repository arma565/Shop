package com.store.shop.view.fragment.productCategory

import android.content.Context
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.LifecycleOwner
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.auth.login.data.model.IProgressbarState
import com.store.shop.data.model.Category
import com.store.shop.databinding.FragmentProductCategoryBinding
import com.store.shop.view.fragment.productCategory.adapter.ProductCategoryAdapter
import com.store.shop.viewmodel.RemoteShopViewModel
import dagger.hilt.android.AndroidEntryPoint

@Suppress("DEPRECATION")
@AndroidEntryPoint
class ProductCategoryFragment : Fragment(), IProgressbarState {
    private lateinit var binding: FragmentProductCategoryBinding
    private val viewModel: RemoteShopViewModel by viewModels()
    private lateinit var owner: LifecycleOwner
    private lateinit var category: Category

    override fun onAttach(context: Context) {
        super.onAttach(context)
        owner = this
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentProductCategoryBinding.inflate(inflater, container, false)
        category = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU)
            arguments?.getParcelable("category", Category::class.java)!!
        else
            arguments?.getParcelable("category")!!
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        this@ProductCategoryFragment.onShowProgressBar()
        viewModel.getProductCategory(category.id).observe(owner) { baseProductCategory ->
            this@ProductCategoryFragment.onHideProgressBar()
            binding.recProductCategory.adapter =
                ProductCategoryAdapter(requireActivity(), baseProductCategory.products)
            binding.recProductCategory.layoutManager =
                GridLayoutManager(requireContext(), 2, RecyclerView.VERTICAL, false)
        }
    }

    override fun onShowProgressBar() {
        binding.progressBar.visibility = View.VISIBLE
    }

    override fun onHideProgressBar() {
        binding.progressBar.visibility = View.GONE
    }
}