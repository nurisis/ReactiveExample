package com.hinuri.reactiveexample

import androidx.lifecycle.ViewModel
import io.reactivex.rxjava3.core.Observable

class MyViewModel: ViewModel() {

    var countObservable = Observable.just(emitData())

    init {
//        countObservable = Observable.fromArray(1,2,3,4)
//        countObservable = Observable.fromIterable(listOf(1,2,3,4,5))
//        countObservable = Observable.fromCallable{
//
//
//            return@fromCallable 5
//        }
    }

    fun emitData() : Int{
        println("LOG>> emitData ** ")

        return 10
    }
}