package com.kayakto.homework.ui.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.kayakto.homework.R
import com.kayakto.homework.ui.edit.EditHabitFragment
import com.google.android.material.tabs.TabLayoutMediator
import com.kayakto.homework.data.adapter.ViewPagerAdapter
import com.kayakto.homework.databinding.FragmentHabitListBinding

class HabitListFragment : Fragment() {

    private lateinit var binding: FragmentHabitListBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHabitListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // ViewPager2 для горизонтального перелистывания по плохим и хорошим привычкам
        val viewPagerAdapter = ViewPagerAdapter(this)
        binding.viewPager.adapter = viewPagerAdapter

        // TabLayout для показа вкладок
        TabLayoutMediator(binding.tabLayout, binding.viewPager) { tab, position ->
            tab.text = when (position) {
                0 -> "Хорошие привычки"
                1 -> "Плохие привычки"
                else -> throw IllegalArgumentException("Такой вкладки не существует $position")
            }
        }.attach()

        // fab - создать привычку
        binding.fab.setOnClickListener {
            val fragment = EditHabitFragment()
            requireActivity().supportFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, fragment)
                .addToBackStack(null)
                .commit()
        }
    }
}