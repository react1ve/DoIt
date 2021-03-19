package com.reactive.todo.data.db

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.reactive.todo.ui.screens.todolist.STATUS
import kotlinx.android.parcel.Parcelize

@Entity(tableName = "todo")
@Parcelize
data class TodoRecord(
        @PrimaryKey(autoGenerate = true) val id: Long?,
        @ColumnInfo(name = "title") val title: String,
        @ColumnInfo(name = "content") val content: String,
        @ColumnInfo(name = "date") val date: String,
        @ColumnInfo(name = "status") val status: STATUS
) : Parcelable