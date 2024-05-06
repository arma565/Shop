package com.auth.login.view.fragment.setting.account.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter

class AccountTabsAdapter(activity : FragmentActivity, private var list : List<Fragment>) : FragmentStateAdapter(activity) {

    /**
     * Return fragment list
     */
    override fun getItemCount(): Int {
        return list.size
    }

    /**
     * Sort using position
     */
    override fun createFragment(position: Int): Fragment {
       return list[position]
    }
}