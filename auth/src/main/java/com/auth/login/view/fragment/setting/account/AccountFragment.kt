package com.auth.login.view.fragment.setting.account


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.auth.login.R
import com.auth.login.databinding.FragmentAccountBinding
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.auth.login.view.fragment.setting.account.adapter.AccountTabsAdapter
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AccountFragment : Fragment() {
    private lateinit var binding: FragmentAccountBinding
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAccountBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.tabLayoutAccount.addTab(binding.tabLayoutAccount.newTab())
        binding.tabLayoutAccount.addTab(binding.tabLayoutAccount.newTab())
        binding.tabLayoutAccount.tabGravity = TabLayout.GRAVITY_FILL
        val fragList = listOf(AccountDataFragment(), PersonalDataFragment())
        val adapter = AccountTabsAdapter(requireActivity(), fragList)
        binding.pager2.adapter = adapter

        TabLayoutMediator(binding.tabLayoutAccount, binding.pager2) { tab, position ->
            when (position) {
                0 -> {
                    tab.text = getString(R.string.account_data)
                }

                1 -> {
                    tab.text = getString(R.string.personal_data)
                }
            }

        }.attach()
    }
}