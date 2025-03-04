package com.kayakto.homework.data.viewholder

import android.annotation.SuppressLint
import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import androidx.recyclerview.widget.RecyclerView
import com.kayakto.homework.data.model.Habit
import com.kayakto.homework.databinding.ItemHabitBinding

class HabitViewHolder(private val binding: ItemHabitBinding) : RecyclerView.ViewHolder(binding.root) {
    @SuppressLint("SetTextI18n")
    fun bind(habit: Habit) {
        binding.nameText.text = habit.name
        binding.descriptionText.text = "Описание: ${ habit.description }"
        binding.priorityText.text = "Приоритет: ${habit.getPriorityString()}"
        binding.typeText.text = "Тип: ${habit.type.getDisplayName()}"
        binding.cntExecutionsText.text = "Количество повторений: ${habit.cntExecutions}"
        binding.periodicityText.text = "Периодичность: ${habit.periodicity}"

        // цвет квадрата
        binding.colorSquare.setBackgroundColor(habit.color)

        // цвет RGB
        val red = Color.red(habit.color)
        val green = Color.green(habit.color)
        val blue = Color.blue(habit.color)
        binding.colorRGBText.text = "($red, $green, $blue)"

        // цвет HEX
        binding.colorHEXText.text = String.format("#%06X", 0xFFFFFF and habit.color)

        // цвет рамки
        val drawable = binding.habitContainer.background as GradientDrawable
        drawable.setStroke(4, habit.color)
    }
}
