package com.kayakto.homework.ui.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.kayakto.homework.R
import com.kayakto.homework.data.adapter.HabitAdapter
import com.kayakto.homework.data.model.HabitType
import com.kayakto.homework.data.model.HabitViewModel
import com.kayakto.homework.databinding.FragmentGoodHabitsBinding
import com.kayakto.homework.ui.edit.EditHabitFragment

class GoodHabitsFragment : Fragment() {

    private lateinit var binding: FragmentGoodHabitsBinding
    private val viewModel: HabitViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentGoodHabitsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val adapter = HabitAdapter(mutableListOf())
        binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerView.adapter = adapter

        // отображение вкладки
        viewModel.habits.observe(viewLifecycleOwner) { habits ->
            val goodHabits = habits
            .filter { it.type == HabitType.GOOD }
            .sortedByDescending { it.priority }
            adapter.updateHabits(goodHabits)
        }

        // нажатие на привычку для редактирования
        adapter.setOnItemClickListener { habit ->
            val fragment = EditHabitFragment().apply {
                arguments = bundleOf("habit" to habit)
            }
            requireActivity().supportFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, fragment)
                .addToBackStack(null)
                .commit()
        }
    }
}