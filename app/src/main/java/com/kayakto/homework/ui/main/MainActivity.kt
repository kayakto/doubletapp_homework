package com.kayakto.homework.ui.main

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.os.Parcelable
import androidx.activity.ComponentActivity
import androidx.activity.result.contract.ActivityResultContracts

import androidx.recyclerview.widget.LinearLayoutManager
import com.kayakto.homework.data.adapter.HabitAdapter
import com.kayakto.homework.data.model.Habit
import com.kayakto.homework.databinding.ActivityMainBinding
import com.kayakto.homework.ui.edit.EditHabitActivity


class MainActivity : ComponentActivity() {

    private lateinit var adapter: HabitAdapter
    private lateinit var binding: ActivityMainBinding

    inline fun <reified T : Parcelable> Intent.getParcelableExtraCompat(key: String): T? {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            getParcelableExtra(key, T::class.java)
        } else {
            @Suppress("DEPRECATION")
            getParcelableExtra(key) as? T
        }
    }

    private val editHabitLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == RESULT_OK) {
            val updatedHabit = result.data?.getParcelableExtraCompat<Habit>("habit")
            if (updatedHabit != null) {
                if (adapter.containsHabit(updatedHabit.id)) {
                    adapter.updateHabit(updatedHabit)
                } else {
                    adapter.addHabit(updatedHabit)
                }
                sortHabitsByPriority()
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val habits = mutableListOf<Habit>()

        adapter = HabitAdapter(habits)
        binding.recyclerView.layoutManager = LinearLayoutManager(this)
        binding.recyclerView.adapter = adapter

        binding.fab.setOnClickListener {
            val intent = Intent(this, EditHabitActivity::class.java)
            editHabitLauncher.launch(intent)
        }

        adapter.setOnItemClickListener { habit ->
            val intent = Intent(this, EditHabitActivity::class.java)
            intent.putExtra("habit", habit)
            editHabitLauncher.launch(intent)
        }
    }

    private fun sortHabitsByPriority() {
        val sortedHabits = adapter.getHabits().sortedByDescending { it.priority }
        adapter.updateHabits(sortedHabits)
    }
}
