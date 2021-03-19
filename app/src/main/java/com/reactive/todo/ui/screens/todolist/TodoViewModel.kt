package com.reactive.todo.ui.screens.todolist

import androidx.lifecycle.LiveData
import com.reactive.todo.base.BaseViewModel
import com.reactive.todo.data.db.TodoRecord
import com.reactive.todo.data.repo.TodoRepo

class TodoViewModel(private val todoRepo: TodoRepo) : BaseViewModel() {

    fun saveTodo(todo: TodoRecord) {
        todoRepo.saveTodo(todo)
    }

    fun updateTodo(todo: TodoRecord) {
        todoRepo.updateTodo(todo)
    }

    fun deleteTodo(todo: TodoRecord) {
        todoRepo.deleteTodo(todo)
    }

    fun getAllTodoList(): LiveData<List<TodoRecord>> {
        return todoRepo.getAllTodoList()
    }
}
