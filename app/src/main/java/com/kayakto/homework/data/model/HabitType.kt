package com.kayakto.homework.data.model

enum class HabitType {
    GOOD,
    BAD;

    fun getDisplayName(): String {
        return when (this) {
            GOOD -> "Хорошая"
            BAD -> "Плохая"
        }
    }
}