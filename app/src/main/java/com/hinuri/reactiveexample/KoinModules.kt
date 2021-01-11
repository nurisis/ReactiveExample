package com.hinuri.reactiveexample

import org.koin.android.viewmodel.dsl.viewModel
import org.koin.dsl.module

val koinModule = module {

    viewModel { MyViewModel() }
}