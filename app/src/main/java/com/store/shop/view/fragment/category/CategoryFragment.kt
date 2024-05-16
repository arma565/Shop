package com.store.shop.view.fragment.category

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.LifecycleOwner
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.auth.login.data.model.IProgressbarState
import com.store.shop.databinding.FragmentCategoryBinding
import com.store.shop.view.fragment.category.adapter.CategoryAdapter
import com.store.shop.viewmodel.RemoteShopViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CategoryFragment : Fragment(), IProgressbarState {
    private lateinit var binding: FragmentCategoryBinding
    private val viewModel: RemoteShopViewModel by viewModels()
    private lateinit var owner: LifecycleOwner

    override fun onAttach(context: Context) {
        super.onAttach(context)
        owner = this
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentCategoryBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        this@CategoryFragment.onShowProgressBar()
        viewModel.getCategories().observe(owner) { baseCategories ->
            this@CategoryFragment.onHideProgressBar()
            binding.recBaseCategory.adapter =
                CategoryAdapter(requireActivity(), baseCategories.categories)
            binding.recBaseCategory.layoutManager =
                LinearLayoutManager(requireContext(), RecyclerView.VERTICAL, false)
        }
    }

    override fun onShowProgressBar() {
        binding.progressBar.visibility = View.VISIBLE
    }

    override fun onHideProgressBar() {
        binding.progressBar.visibility = View.GONE
    }
}