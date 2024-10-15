package com.example.criminalhamster

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter

class CrimePagerAdapter(fragmentActivity: FragmentActivity, private val crimes : ArrayList<Crime>)
    : FragmentStateAdapter(fragmentActivity) {
    override fun getItemCount(): Int {
        return crimes.size
    }

    override fun createFragment(position: Int): Fragment {
        val crime = crimes[position]
        return CriminalFragment.newInstance(crime.getId().toString())
    }
}