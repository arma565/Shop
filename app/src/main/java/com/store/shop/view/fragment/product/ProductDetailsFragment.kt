package com.store.shop.view.fragment.product

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.store.shop.R
import com.store.shop.data.model.GlobalFunctions
import com.store.shop.data.model.Product
import com.store.shop.databinding.DialogLayoutBinding
import com.store.shop.databinding.FragmentProductDetailsBinding
import com.store.shop.view.fragment.product.adapter.GalleryAdapter
import dagger.hilt.android.AndroidEntryPoint

@Suppress("DEPRECATION")
@AndroidEntryPoint
class ProductDetailsFragment : Fragment() {

    private lateinit var binding: FragmentProductDetailsBinding
    private lateinit var product: Product
    private lateinit var container: ViewGroup
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

        val myActivity = requireActivity() as AppCompatActivity
        binding.toolbarProduct.title = ""
        myActivity.setSupportActionBar(binding.toolbarProduct)

        binding.imgClose.setOnClickListener {
            GlobalFunctions.getResult(requireActivity() as AppCompatActivity)
        }

        binding.pagerProduct.adapter = GalleryAdapter(requireActivity(), product.gallery)
        binding.springDotsIndicator.attachTo(binding.pagerProduct)

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
}