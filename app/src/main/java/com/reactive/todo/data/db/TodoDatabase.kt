package com.reactive.todo.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters

@Database(entities = [TodoRecord::class], version = 1, exportSchema = false)
@TypeConverters(Converter::class)
abstract class TodoDatabase : RoomDatabase() {

    abstract fun todoDao(): TodoDao

}