package com.msc.speaker_cleaner.component.main

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.msc.speaker_cleaner.component.cleanerauto.AutoCleanerFragment
import com.msc.speaker_cleaner.component.cleanermanual.ManualCleanerFragment
import com.msc.speaker_cleaner.component.cleanervibrate.VibrateCleanerFragment
import com.msc.speaker_cleaner.component.home.HomeFragment

class VPMainAdapter(fragmentActivity: FragmentActivity) : FragmentStateAdapter(fragmentActivity) {
    val listFragment = arrayListOf(HomeFragment(), AutoCleanerFragment(), ManualCleanerFragment(), VibrateCleanerFragment())
    init {

    }

    override fun getItemCount(): Int {
        return 4
    }

    override fun createFragment(position: Int): Fragment {
        return listFragment[position]
    }
}