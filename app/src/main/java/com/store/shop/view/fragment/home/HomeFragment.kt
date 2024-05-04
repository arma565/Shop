package com.store.shop.view.fragment.home

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
import com.store.shop.databinding.FragmentHomeBinding
import com.store.shop.view.fragment.home.adapter.HomeAdapter
import com.store.shop.view.fragment.home.adapter.NewsAdapter
import com.store.shop.viewmodel.ShopViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.launch

@AndroidEntryPoint
class HomeFragment : Fragment() {
    private lateinit var binding: FragmentHomeBinding
    private val viewModel: ShopViewModel by viewModels()
    private lateinit var owner: LifecycleOwner

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        owner = this
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        CoroutineScope(IO).launch LaunchIO@{
            viewModel.getNews().collect { newsList ->
                this.launch(Main) {
                    if (newsList.isEmpty()) return@launch
                    binding.viewPager2.adapter = NewsAdapter(newsList)
                    binding.springDotsIndicator.attachTo(binding.viewPager2)
                }
            }
        }

        viewModel.getBaseHome().observe(owner) { baseHome ->
            binding.recAmazing.adapter =
                HomeAdapter(requireActivity(), baseHome.amazingOffer)
            binding.recAmazing.layoutManager =
                LinearLayoutManager(requireContext(), RecyclerView.HORIZONTAL, false)

            binding.recMobiles.adapter = HomeAdapter(requireActivity(), baseHome.mobile)
            binding.recMobiles.layoutManager =
                LinearLayoutManager(requireContext(), RecyclerView.HORIZONTAL, false)

            binding.recMakeup.adapter = HomeAdapter(requireActivity(), baseHome.makeup)
            binding.recMakeup.layoutManager =
                LinearLayoutManager(requireContext(), RecyclerView.HORIZONTAL, false)

            binding.recDiscount.adapter =
                HomeAdapter(requireActivity(), baseHome.discount)
            binding.recDiscount.layoutManager =
                LinearLayoutManager(requireContext(), RecyclerView.HORIZONTAL, false)
        }
    }
}