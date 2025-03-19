package com.kayakto.homework.ui.edit

import android.annotation.SuppressLint
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.LinearLayout
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.kayakto.homework.R
import com.kayakto.homework.data.model.Habit
import com.kayakto.homework.data.model.HabitType
import com.kayakto.homework.data.model.HabitViewModel
import com.kayakto.homework.databinding.FragmentEditHabitBinding
import com.kayakto.homework.util.HueGradientDrawable
import com.kayakto.homework.util.extensions.getParcelableCompat

class EditHabitFragment : Fragment() {

    private lateinit var binding: FragmentEditHabitBinding
    private var selectedPriority = 1
    private var selectedType = HabitType.GOOD
    private var habitId: String? = null
    private var selectedSquare: View? = null
    private var selectedColor = Color.BLACK
    private val viewModel: HabitViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentEditHabitBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

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
            val square = View(requireContext()).apply {
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
        val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, priorities)
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
                R.id.radioGood -> HabitType.GOOD
                R.id.radioBad -> HabitType.BAD
                else -> HabitType.GOOD
            }
        }
    }

    /**
     * Загружает данные привычки для редактирования
     */
    @SuppressLint("SetTextI18n")
    private fun loadHabitData() {
        arguments?.getParcelableCompat<Habit>("habit")?.let { habit ->
            habitId = habit.id
            binding.edHabitName.setText(habit.name)
            binding.edHabitDescription.setText(habit.description)
            binding.edHabitCntExecutions.setText(habit.cntExecutions.toString())
            binding.edHabitPeriodicity.setText(habit.periodicity)
            selectedColor = habit.color
            binding.tvColorRGB.text = "(${Color.red(habit.color)}, " +
                    "${Color.green(habit.color)}, ${Color.blue(habit.color)})"
            binding.tvColorHEX.text = String.format("#%06X", 0xFFFFFF and habit.color)
            binding.edHabitPriority.setSelection(habit.priority - 1)
            binding.radioGroupHabitType.check(
                when (habit.type) {
                    HabitType.GOOD -> R.id.radioGood
                    HabitType.BAD -> R.id.radioBad
                }
            )

            // выделение выбранного цвета рамкой
            val colorContainer = binding.colorContainer
            for (i in 0 until colorContainer.childCount) {
                val square = colorContainer.getChildAt(i)
                if (square.background is ColorDrawable && (square.background as ColorDrawable).color == habit.color) {
                    square.setBackgroundResource(R.drawable.selected_square_border)
                    selectedSquare = square
                    break
                }
            }
        }
    }

    /**
     * Сохраняет привычку
     */
    private fun saveHabit() {
        val name = binding.edHabitName.text.toString()
        val description = binding.edHabitDescription.text.toString()
        val cntExecutions = binding.edHabitCntExecutions.text.toString().toIntOrNull() ?: 0
        val periodicity = binding.edHabitPeriodicity.text.toString()

        if (name.isEmpty() || description.isEmpty() || periodicity.isEmpty()) {
            Toast.makeText(requireContext(), "Все поля должны быть заполнены",
                Toast.LENGTH_SHORT).show()
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

        if (habitId == null)
            viewModel.addHabit(updatedHabit)
        else
            viewModel.updateHabit(updatedHabit)

        // возврат на экран со списком
        parentFragmentManager.popBackStack()
    }
}
