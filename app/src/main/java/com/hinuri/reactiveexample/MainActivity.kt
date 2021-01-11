package com.hinuri.reactiveexample

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.Scheduler
import io.reactivex.rxjava3.schedulers.Schedulers
import org.koin.android.viewmodel.ext.android.viewModel
import java.util.concurrent.Callable
import java.util.concurrent.Executors
import java.util.concurrent.Flow
import java.util.concurrent.Future

/**
 * reactiveX official site : http://reactivex.io/documentation/observable.html
 *
 * " An observer subscribes to an Observable. "
 * */

class MainActivity : AppCompatActivity() {

    private val viewModel by viewModel<MyViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        createWithJust()

        createWithCreate()

        createWithFromCaller()

        createWithFromFuture()
    }

    /**
     * just() 의 경우, 옵저버 subscribe를 하지 않아도 observable 생성됨.
     * */
    private fun createWithJust() {
        val justObservable = Observable.just(emitData())

        justObservable.subscribe { println("LOG>> [just()] onNext : $it") }
    }

    private fun createWithCreate() {
        val createObservable = Observable.create<Int> { emitter ->
            emitter.onNext(emitData())
            emitter.onNext(2*emitData())
            emitter.onError(Exception("Error Test"))
            emitter.onComplete()
        }

        createObservable.subscribe(
            {
                println("LOG>> [create()] onNext : $it")
            }, {
                println("LOG>> [create()] onError: $it")
            }, {
                println("LOG>> [create()] onComplete")
            }
        )
    }

    private fun createWithFromCaller() {
        val callable = Callable {
            Thread.sleep(5000)
            return@Callable emitData()
        }

        val fromCallableObservable = Observable.fromCallable(callable)
            .subscribeOn(Schedulers.io())

        fromCallableObservable.subscribe {
            println("LOG>> [fromCallable()] onNext : $it")
        }
    }

    private fun createWithFromFuture() {
        val future = Executors.newSingleThreadExecutor().submit(Callable {
            Thread.sleep(5000)
            return@Callable emitData()
        })

        val fromFutureObservable = Observable.fromFuture(future)
            .subscribeOn(Schedulers.io())

        fromFutureObservable.subscribe {
            println("LOG>> [fromFuture()] onNext : $it")
        }
    }

    private fun emitData() : Int{
        return 100
    }
}