package com.kayakto.homework.data.model

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class HabitViewModel : ViewModel() {
    // LiveData чтобы уведомлять ui об изменениях списка
    val habits = MutableLiveData<MutableList<Habit>>(mutableListOf())

    fun addHabit(habit: Habit) {
        val currentHabits = habits.value ?: mutableListOf()
        currentHabits.add(habit)
        habits.value = currentHabits
    }

    fun updateHabit(updatedHabit: Habit) {
        val currentHabits = habits.value ?: mutableListOf()
        val index = currentHabits.indexOfFirst { it.id == updatedHabit.id }
        if (index != -1) {
            currentHabits[index] = updatedHabit
            habits.value = currentHabits
        }
    }
}