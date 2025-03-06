package com.kayakto.homework.data.model

import android.os.Parcel
import android.os.Parcelable

import java.util.UUID

data class Habit(
    val id: String = UUID.randomUUID().toString(),
    var name: String,
    var description: String,
    var priority: Int,
    var type: HabitType,
    var cntExecutions: Int,
    var periodicity: String,
    var color: Int
) : Parcelable {
    init {
        require(priority in 1..5) { "Приоритет должен быть от 1 до 5" }
        require(cntExecutions >= 0) { "Периодичность должна быть больше или равна 0" }
    }

    constructor(parcel: Parcel) : this(
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readInt(),
        HabitType.valueOf(parcel.readString()!!),
        parcel.readInt(),
        parcel.readString()!!,
        parcel.readInt()
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(id)
        parcel.writeString(name)
        parcel.writeString(description)
        parcel.writeInt(priority)
        parcel.writeString(type.name)
        parcel.writeInt(cntExecutions)
        parcel.writeString(periodicity)
        parcel.writeInt(color)
    }

    override fun describeContents(): Int = 0

    companion object CREATOR : Parcelable.Creator<Habit> {
        override fun createFromParcel(parcel: Parcel): Habit = Habit(parcel)
        override fun newArray(size: Int): Array<Habit?> = arrayOfNulls(size)
    }

    fun getPriorityString() : String =
        when(priority) {
            1 -> "Очень низкий"
            2 -> "Низкий"
            3 -> "Средний"
            4 -> "Высокий"
            5 -> "Очень высокий"
            else -> "Неизвестный"
        }
}
