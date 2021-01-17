package com.hinuri.reactiveexample

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.*
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.functions.Supplier
import io.reactivex.rxjava3.internal.operators.observable.ObservableSubscribeOn
import io.reactivex.rxjava3.schedulers.Schedulers
import org.koin.android.viewmodel.ext.android.viewModel
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.*

/**
 * reactiveX official site : http://reactivex.io/documentation/observable.html
 *
 * " An observer subscribes to an Observable. "
 * */

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        createWithJust()

        createWithCreate()

        createWithFromCaller()

        createWithFromFuture()

        createWithFromIterable()

        createWithFromDefer()

        createWithSingle()

        createWithMaybe()

        createWithTimer()

        createWithRange()

        createWithInterval()

        createWithNever()
        createWithEmpty()
        createWithThrow()
    }

    /**
     * The case of just(), observable is created even if observer is not subscribed.
     * */
    private fun createWithJust() {
        val justObservable = Observable.just(System.currentTimeMillis())

        justObservable.subscribe { println("LOG>> [just()] onNext : $it") }

        Thread.sleep(1000)

        justObservable.subscribe { println("LOG>> [just() 2] onNext : $it") }
    }

    private fun createWithCreate() {
        val createObservable = Observable.create<Int> { emitter ->
            emitter.onNext(emitData())
            emitter.onNext(2*emitData())
            emitter.onError(Exception("Error Test"))
            emitter.onComplete()
        }

        createObservable.subscribe(
            { onNext ->
                println("LOG>> [create()] onNext : $onNext")
            }, { throwable ->
                println("LOG>> [create()] onError: $throwable")
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

    private fun createWithFromDefer() {
        val fromDeferObservable = Observable.defer {
            Observable.just(System.currentTimeMillis())
        }

        fromDeferObservable.subscribe { onNext ->
            println("LOG>> [defer()] onNext : $onNext")
        }

        Thread.sleep(1000)

        fromDeferObservable.subscribe(
            { onNext ->
                println("LOG>> [defer()] onNext : $onNext")
            }, { throwable ->
                println("LOG>> [defer()] onError: $throwable")
            }, {
                println("LOG>> [defer()] onComplete")
            }
        )
    }

    private fun createWithFromIterable() {
        val fromIterableObservable = Observable.fromIterable(listOf(1,2,3))

        fromIterableObservable.subscribe(
            { onNext ->
                println("LOG>> [fromIterable()] onNext : $onNext")
            }, { throwable ->
                println("LOG>> [fromIterable()] onError: $throwable")
            }, {
                println("LOG>> [fromIterable()] onComplete")
            }
        )
    }

    private fun createWithTimer() {
        val timerObservable = Observable.timer(2, TimeUnit.SECONDS)

        timerObservable.subscribe(
            { onNext ->
                println("LOG>> [timer()] onNext : $onNext, Timer done!")
            }, { throwable ->
                println("LOG>> [timer()] onError: $throwable")
            }, {
                println("LOG>> [timer()] onComplete")
            }
        )
    }

    private fun createWithRange() {
        val rangeObservable = Observable.range(1, 3)

        rangeObservable.subscribe(
            { onNext ->
                println("LOG>> [range()] onNext : $onNext")
            }, { throwable ->
                println("LOG>> [range()] onError: $throwable")
            }, {
                println("LOG>> [range()] onComplete")
            }
        )
    }

    private fun createWithInterval() {
        val intervalObservable = Observable.interval(1, TimeUnit.SECONDS)

        intervalObservable.subscribe { onNext ->
            println("LOG>> [interval() ${SimpleDateFormat("mm:ss").format(Date())} ] onNext : $onNext")
        }
    }

    private fun createWithNever() {
        val neverObservable = Observable.never<Int>()

        neverObservable.subscribe(
            { onNext ->
                println("LOG>> [never()] onNext : $onNext")
            }, { throwable ->
                println("LOG>> [never()] onError: $throwable")
            }, {
                println("LOG>> [never()] onComplete")
            }
        )
    }

    private fun createWithEmpty() {
        val emptyObservable = Observable.empty<Int>()

        emptyObservable.subscribe(
            { onNext ->
                println("LOG>> [empty()] onNext : $onNext")
            }, { throwable ->
                println("LOG>> [empty()] onError: $throwable")
            }, {
                println("LOG>> [empty()] onComplete")
            }
        )
    }

    private fun createWithThrow() {
        val throwObservable = Observable.error<Int>(Exception("Create Error!"))

        throwObservable.subscribe(
            { onNext ->
                println("LOG>> [error()] onNext : $onNext")
            }, { throwable ->
                println("LOG>> [error()] onError: $throwable")
            }, {
                println("LOG>> [error()] onComplete")
            }
        )
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

    /**
     * Emit just 1 value.
     * http://reactivex.io/documentation/single.html
     * */
    private fun createWithSingle() {
        var single = Single.fromObservable<Int> {
            it.onNext(emitData())
            it.onComplete()
        }

        single.subscribeOn(Schedulers.io())
            .subscribe(
            {
                println("LOG>> [single()] onSuccess : $it")
            }, {
                println("LOG>> [single()] onError: $it")
            }
        )
    }

    /**
     * Emit value only 0 or 1.
     * That's why named Maybe.
     * */
    private fun createWithMaybe() {
        var maybe = Maybe.create<Int> {
            it.onError(Exception("Maybe Error"))
            it.onComplete()
        }

        maybe.subscribeOn(Schedulers.io())
            .subscribe(
                {
                    println("LOG>> [maybe()] onSuccess : $it")
                }, {
                    println("LOG>> [maybe()] onError: $it")
                }
        )
    }

    /**
     * Subject : http://reactivex.io/documentation/subject.html
     * RxJava에서 Subject 클래스는 구독하고 있는 관찰자(Observer)에게 새로운 값을 전달 할 때 사용하는 클래스다.
     * 따로 Observable로 새로운 값을 만들 필요 없이 Subject 객체에 내장된 onNext 함수로 새로운 값을 옵저버에게 전달할 수 있기 때문에 짧은 코드로도 reactive하게 구현하는 것이 가능하다.
     * 안드로이드에서 제공하는 LiveData와 유사한 역할을 한다. 출처: https://selfish-developer.com/entry/RxJava-Subject-PublishSubject-BehaviorSubject
     *
     * todo :: Next step !
     * */
    private fun createWithSubject() {
    }

    private fun emitData() : Int{
        return 100
    }
}