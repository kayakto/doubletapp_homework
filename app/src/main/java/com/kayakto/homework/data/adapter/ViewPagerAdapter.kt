package com.kayakto.homework.data.adapter

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.kayakto.homework.ui.main.BadHabitsFragment
import com.kayakto.homework.ui.main.GoodHabitsFragment

class ViewPagerAdapter(fragment: Fragment) : FragmentStateAdapter(fragment) {

    override fun getItemCount(): Int = 2

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> GoodHabitsFragment()
            1 -> BadHabitsFragment()
            else -> throw IllegalArgumentException("Такой вкладки не существует: $position")
        }
    }
}