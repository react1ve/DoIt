package com.reactive.todo.data.db

import androidx.room.TypeConverter
import com.reactive.todo.ui.screens.todolist.STATUS

class Converter {

    @TypeConverter
    fun fromStatus(priority: STATUS): String {
        return priority.name
    }

    @TypeConverter
    fun toStatus(priority: String): STATUS {
        return STATUS.valueOf(priority)
    }

}