package com.auth.login.view.fragment.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.auth.login.data.local.config.UserAutoLoginConfig
import com.auth.login.data.model.GlobalFunctions
import com.auth.login.databinding.FragmentAuthHomeBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeAuthFragment : Fragment() {
    private lateinit var binding: FragmentAuthHomeBinding
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAuthHomeBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onStart() {
        super.onStart()
        if (!UserAutoLoginConfig(requireParentFragment().requireActivity()).getEmail()
                .isNullOrEmpty()
        )
            GlobalFunctions.logIn(requireActivity())
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.btnStart.setOnClickListener {
            GlobalFunctions.getNavControllerFragmentAuth(requireActivity())
                .navigate(HomeAuthFragmentDirections.actionHomeLoginFragmentToLoginFragment())
        }
    }
}