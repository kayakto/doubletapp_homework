package com.kayakto.homework.ui.edit

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.os.Parcelable
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.LinearLayout
import android.widget.Toast
import androidx.activity.ComponentActivity
import com.kayakto.homework.R
import com.kayakto.homework.data.model.Habit
import com.kayakto.homework.data.model.HabitType

import com.kayakto.homework.databinding.ActivityEditHabitBinding
import com.kayakto.homework.util.HueGradientDrawable

class EditHabitActivity : ComponentActivity() {

    private lateinit var binding: ActivityEditHabitBinding
    private var selectedPriority = 1
    private var selectedType = HabitType.OTHER
    private var habitId: String? = null
    private var selectedSquare: View? = null
    private var selectedColor = Color.BLACK

    private inline fun <reified T : Parcelable> Bundle.getParcelableCompat(key: String): T? {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            getParcelable(key, T::class.java)
        } else {
            @Suppress("DEPRECATION") getParcelable(key) as? T
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEditHabitBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setupColorPicker()
        binding.colorContainer.background = HueGradientDrawable()
        setupPrioritySpinner()
        setupRadioGroup()
        loadHabitData()

        binding.buttonSave.setOnClickListener { saveHabit() }
    }

    private fun setupColorPicker() {
        val colorContainer = binding.colorContainer
        val squareSize = resources.getDimensionPixelSize(R.dimen.color_square_size)
        val margin = resources.getDimensionPixelSize(R.dimen.color_square_margin)
        val hueStep = 360f / 16

        repeat(16) { i ->
            val color = Color.HSVToColor(floatArrayOf(i * hueStep, 1f, 1f))
            val square = View(this).apply {
                layoutParams = LinearLayout.LayoutParams(squareSize, squareSize).apply {
                    setMargins(margin, margin, margin, margin)
                }
                setBackgroundColor(color)
                setOnClickListener { selectColor(this, color) }
            }

            if (color == selectedColor) {
                square.setBackgroundResource(R.drawable.selected_square_border)
                selectedSquare = square
            }

            colorContainer.addView(square)
        }
    }

    @SuppressLint("SetTextI18n")
    private fun selectColor(square: View, color: Int) {
        selectedSquare?.background = null
        square.setBackgroundResource(R.drawable.selected_square_border)
        selectedSquare = square
        selectedColor = color
        binding.tvColorRGB.text = "(${Color.red(color)}, ${Color.green(color)}, ${Color.blue(color)})"
        binding.tvColorHEX.text = String.format("#%06X", 0xFFFFFF and color)
    }

    private fun setupPrioritySpinner() {
        val priorities = resources.getStringArray(R.array.HabitPriorities)
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, priorities)
        binding.edHabitPriority.adapter = adapter
        binding.edHabitPriority.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {
                selectedPriority = position + 1
            }
            override fun onNothingSelected(parent: AdapterView<*>) {}
        }
    }

    private fun setupRadioGroup() {
        binding.radioGroupHabitType.setOnCheckedChangeListener { _, checkedId ->
            selectedType = when (checkedId) {
                R.id.radioHealth -> HabitType.HEALTH
                R.id.radioPersonalGrowth -> HabitType.PERSONAL_GROWTH
                R.id.radioWork -> HabitType.WORK
                R.id.radioSport -> HabitType.SPORT
                else -> HabitType.OTHER
            }
        }
    }

    @SuppressLint("SetTextI18n")
    private fun loadHabitData() {
        intent.extras?.getParcelableCompat<Habit>("habit")?.let { habit ->
            habitId = habit.id
            binding.edHabitName.setText(habit.name)
            binding.edHabitDescription.setText(habit.description)
            binding.edHabitCntExecutions.setText(habit.cntExecutions.toString())
            binding.edHabitPeriodicity.setText(habit.periodicity)
            selectedColor = habit.color
            binding.tvColorRGB.text = "(${Color.red(habit.color)}, ${Color.green(habit.color)}, ${Color.blue(habit.color)})"
            binding.tvColorHEX.text = String.format("#%06X", 0xFFFFFF and habit.color)
            binding.edHabitPriority.setSelection(habit.priority - 1)
            binding.radioGroupHabitType.check(
                when (habit.type) {
                    HabitType.HEALTH -> R.id.radioHealth
                    HabitType.PERSONAL_GROWTH -> R.id.radioPersonalGrowth
                    HabitType.WORK -> R.id.radioWork
                    HabitType.SPORT -> R.id.radioSport
                    else -> R.id.radioOther
                }
            )
        }
    }

    private fun saveHabit() {
        val name = binding.edHabitName.text.toString()
        val description = binding.edHabitDescription.text.toString()
        val cntExecutions = binding.edHabitCntExecutions.text.toString().toIntOrNull() ?: 0
        val periodicity = binding.edHabitPeriodicity.text.toString()

        if (name.isEmpty() || description.isEmpty() || periodicity.isEmpty()) {
            Toast.makeText(this, "Все поля должны быть заполнены", Toast.LENGTH_SHORT).show()
            return
        }

        val updatedHabit = Habit(
            id = habitId ?: System.currentTimeMillis().toString(),
            name = name,
            description = description,
            priority = selectedPriority,
            type = selectedType,
            cntExecutions = cntExecutions,
            periodicity = periodicity,
            color = selectedColor
        )

        setResult(RESULT_OK, Intent().apply { putExtra("habit", updatedHabit) })
        finish()
    }
}
