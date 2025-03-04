package com.kayakto.homework.data.model

enum class HabitType {
    HEALTH,
    PERSONAL_GROWTH,
    WORK,
    SPORT,
    OTHER;

    fun getDisplayName(): String {
        return when (this) {
            HEALTH -> "Здоровье"
            PERSONAL_GROWTH -> "Саморазвитие"
            WORK -> "Работа"
            SPORT -> "Спорт"
            OTHER -> "Другое"
        }
    }
}