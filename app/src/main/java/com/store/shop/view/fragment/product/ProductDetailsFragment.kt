package com.store.shop.view.fragment.product

import android.content.Context
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.LifecycleOwner
import com.auth.login.data.model.IProgressbarState
import com.store.shop.R
import com.store.shop.data.model.GlobalFunctions
import com.store.shop.data.model.Product
import com.store.shop.databinding.DialogLayoutBinding
import com.store.shop.databinding.FragmentProductDetailsBinding
import com.store.shop.view.fragment.product.adapter.GalleryAdapter
import com.store.shop.viewmodel.LocalShopViewModel
import com.store.shop.viewmodel.RemoteShopViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

@Suppress("DEPRECATION")
@AndroidEntryPoint
class ProductDetailsFragment : Fragment(),IProgressbarState {

    private lateinit var binding: FragmentProductDetailsBinding
    private lateinit var product: Product
    private lateinit var owner: LifecycleOwner
    private lateinit var container: ViewGroup
    private val localViewModel: LocalShopViewModel by viewModels()
    private val remoteShopViewModel: RemoteShopViewModel by viewModels()

    override fun onAttach(context: Context) {
        super.onAttach(context)
        owner = this
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        this.container = container!!
        binding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_product_details, container, false)
        product = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU)
            arguments?.getParcelable("product", Product::class.java)!!
        else
            arguments?.getParcelable("product")!!
        binding.product = product
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        this@ProductDetailsFragment.onShowProgressBar()
        var list: List<Product> = listOf()
        val job = CoroutineScope(Dispatchers.IO).launch LaunchIO@{
            localViewModel.productList().collect {
                list = it
            }
        }
        runBlocking {
            job.join()
            this@ProductDetailsFragment.onHideProgressBar()
            if (list.any { faveProduct -> faveProduct.id == product.id }) {
                binding.imgFav.setImageResource(R.drawable.baseline_favorite_24)
                return@runBlocking
            }
            binding.imgFav.setImageResource(R.drawable.baseline_favorite_border_24)
        }
        runBlocking {
            job.join()
            binding.imgFav.setOnClickListener {
                if (list.any { faveProduct -> faveProduct.id == product.id }) {
                    localViewModel.deleteProduct(list.first { faveProduct -> faveProduct.id == product.id }.productId)
                    binding.imgFav.setImageResource(R.drawable.baseline_favorite_border_24)
                } else {
                    localViewModel.upsertProduct(
                        Product(
                            id = product.id,
                            catId = product.catId,
                            catName = product.catName,
                            title = product.title,
                            brand = product.brand,
                            garanti = product.garanti,
                            count = product.count,
                            shortDescription = product.shortDescription,
                            fullDescription = product.fullDescription,
                            special = product.special,
                            discount = product.discount,
                            rate = product.rate,
                            price = product.price,
                            icon = product.icon
                        )
                    )
                    binding.imgFav.setImageResource(R.drawable.baseline_favorite_24)
                }
            }
        }


        val myActivity = requireActivity() as AppCompatActivity
        binding.toolbarProduct.title = ""
        myActivity.setSupportActionBar(binding.toolbarProduct)

        binding.imgClose.setOnClickListener {
            GlobalFunctions.getResult(requireActivity() as AppCompatActivity)
        }

        if (product.gallery.isEmpty()) {
            remoteShopViewModel.searchProduct(product.title).observe(owner) {
                binding.pagerProduct.adapter =
                    GalleryAdapter(requireActivity(), it.products.first().gallery)
                binding.springDotsIndicator.attachTo(binding.pagerProduct)
            }
        } else {
            binding.pagerProduct.adapter = GalleryAdapter(requireActivity(), product.gallery)
            binding.springDotsIndicator.attachTo(binding.pagerProduct)
        }


        binding.txtShort.setOnClickListener {
            val dialog = AlertDialog.Builder(requireContext()).create()
            val binding: DialogLayoutBinding = DataBindingUtil.bind(
                LayoutInflater.from(requireContext())
                    .inflate(R.layout.dialog_layout, container, false)
            )!!
            binding.product = product
            binding.btnOk.setOnClickListener {
                dialog.dismiss()
            }
            dialog.setView(binding.root)
            dialog.show()
        }



    }

    override fun onShowProgressBar() {
        binding.progressBar.visibility = View.VISIBLE
    }

    override fun onHideProgressBar() {
        binding.progressBar.visibility = View.GONE
    }

}