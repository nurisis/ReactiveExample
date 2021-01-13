package com.hinuri.reactiveexample

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Maybe
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.Scheduler
import io.reactivex.rxjava3.core.Single
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

        createWithSingle()

        createWithMaybe()
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

    /**
     * Emit just 1 value.
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
//            it.onSuccess(emitData())
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
     * 안드로이드에서 제공하는 LiveData와 유사한 역할을 한다. 출처: https://selfish-developer.com/entry/RxJava-Subject-PublishSubject-BehaviorSubject [아는 개발자]
     *
     * todo :: 여기서부터 !
     * */
    private fun createWithSubject() {
    }

    private fun emitData() : Int{
        return 100
    }
}