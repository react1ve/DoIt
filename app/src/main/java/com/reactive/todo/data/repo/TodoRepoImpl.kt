package com.reactive.todo.data.repo

import androidx.lifecycle.LiveData
import com.reactive.todo.data.db.TodoDao
import com.reactive.todo.data.db.TodoRecord
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

class TodoRepoImpl(private val todoDao: TodoDao) : TodoRepo {

    override fun saveTodo(todo: TodoRecord) {
        runBlocking {
            this.launch(Dispatchers.IO) {
                todoDao.saveTodo(todo)
            }
        }
    }

    override fun updateTodo(todo: TodoRecord) {
        runBlocking {
            this.launch(Dispatchers.IO) {
                todoDao.updateTodo(todo)
            }
        }
    }


    override fun deleteTodo(todo: TodoRecord) {
        runBlocking {
            this.launch(Dispatchers.IO) {
                todoDao.deleteTodo(todo)
            }
        }
    }

    override fun getAllTodoList(): LiveData<List<TodoRecord>> {
        return todoDao.getAllTodoList()
    }
}