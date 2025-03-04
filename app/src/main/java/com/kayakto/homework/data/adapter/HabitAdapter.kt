package com.kayakto.homework.data.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.kayakto.homework.data.model.Habit
import com.kayakto.homework.data.viewholder.HabitViewHolder
import com.kayakto.homework.databinding.ItemHabitBinding

class HabitAdapter(private val habits: MutableList<Habit>) : RecyclerView.Adapter<HabitViewHolder>() {

    private var onItemClickListener: ((Habit) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HabitViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemHabitBinding.inflate(inflater, parent, false)
        return HabitViewHolder(binding)
    }

    override fun getItemCount(): Int = habits.size

    fun getHabits(): MutableList<Habit> = habits

    fun setOnItemClickListener(listener: (Habit) -> Unit) {
        this.onItemClickListener = listener
    }

    override fun onBindViewHolder(holder: HabitViewHolder, position: Int) {
        val habit = habits[position]
        holder.bind(habit)


        holder.itemView.setOnClickListener {
            onItemClickListener?.invoke(habit)
        }
    }

    fun updateHabit(updatedHabit: Habit) {
        val index = habits.indexOfFirst { it.id == updatedHabit.id }
        if (index != -1) {
            // Обновление привычки
            habits[index] = updatedHabit
            notifyItemChanged(index)
        } else {
            // Добавление новой
            habits.add(updatedHabit)
            notifyItemInserted(habits.size - 1)
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    fun updateHabits(updatedHabits: List<Habit>) {
        habits.clear()
        habits.addAll(updatedHabits)
        notifyDataSetChanged()
    }

    fun containsHabit(id: String): Boolean {
        return habits.any { it.id == id }
    }

    @SuppressLint("NotifyDataSetChanged")
    fun addHabit(habit: Habit) {
        habits.add(habit)
        notifyDataSetChanged()
    }
}