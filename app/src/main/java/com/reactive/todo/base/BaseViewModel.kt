package com.reactive.todo.base

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import org.koin.core.KoinComponent
import org.koin.core.inject
import org.koin.core.qualifier.named

abstract class BaseViewModel : ViewModel(), KoinComponent {

    val data: MutableLiveData<Any> by inject()
    val shared: MutableLiveData<Any> by inject(named("sharedLive"))
}