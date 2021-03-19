package com.reactive.todo.di

import androidx.lifecycle.MutableLiveData
import androidx.room.Room
import com.reactive.todo.data.db.TodoDatabase
import com.reactive.todo.data.repo.TodoRepo
import com.reactive.todo.data.repo.TodoRepoImpl
import com.reactive.todo.ui.screens.BottomNavViewModel
import com.reactive.todo.ui.screens.todolist.TodoViewModel
import org.koin.android.ext.koin.androidApplication
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.qualifier.named
import org.koin.dsl.module

val applicationModule = module {

    // Room database
    single {
        Room.databaseBuilder(androidApplication(), TodoDatabase::class.java, "TODO_DB").build()
    }

    // TodoDao
    single { get<TodoDatabase>().todoDao() }

    // TodoRepository
    factory<TodoRepo> { TodoRepoImpl(get()) }
}

val viewModelModule = module {
    fun provideMutableLiveData() = MutableLiveData<Any>()

    // SharedLiveData
    single { provideMutableLiveData() }
    single(named("sharedLive")) { provideMutableLiveData() }

    // BottomNavViewModel
    viewModel { BottomNavViewModel() }

    // TodoViewModel
    viewModel { TodoViewModel(get()) }
}
