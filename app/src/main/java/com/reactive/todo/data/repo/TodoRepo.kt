package com.reactive.todo.data.repo

import androidx.lifecycle.LiveData
import com.reactive.todo.data.db.TodoRecord

interface TodoRepo {
    fun saveTodo(todo: TodoRecord)
    fun updateTodo(todo: TodoRecord)
    fun deleteTodo(todo: TodoRecord)
    fun getAllTodoList(): LiveData<List<TodoRecord>>
}