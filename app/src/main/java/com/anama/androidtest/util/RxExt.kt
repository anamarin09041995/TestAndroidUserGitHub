package com.anama.androidtest.util

import android.arch.lifecycle.Lifecycle
import android.arch.lifecycle.LifecycleObserver
import android.arch.lifecycle.LifecycleOwner
import android.arch.lifecycle.OnLifecycleEvent
import com.anama.androidtest.R
import io.reactivex.Maybe
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import retrofit2.HttpException
import java.net.SocketTimeoutException

class LifeDisposable(owner: LifecycleOwner): LifecycleObserver {

    init {
        owner.lifecycle.addObserver(this)
    }

    val dis: CompositeDisposable = CompositeDisposable()

    infix fun add(disposable: Disposable){
        dis.add(disposable)
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
    fun clearDisposable(){
        dis.clear()
    }
}

fun <T> Maybe<T>.applySchedulers(): Maybe<T> = compose {
    it.subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
}


fun <T> Observable<T>.subscribeByAction(onNext: (T) -> Unit, onHttpError: (resString: Int) -> Unit,
                                        onError: ((error: Throwable) -> Unit)? = null): Disposable =

        doOnError {
            when (it) {
                is SocketTimeoutException -> onHttpError(R.string.socket)
                is HttpException -> {
                    when (it.code()) {
                        404 -> onHttpError(R.string.http_404)
                        401 -> onHttpError(R.string.http_401)
                        else -> onHttpError(R.string.http_500)
                    }
                }
                else -> onError?.invoke(it)
            }
        }
                .retry()
                .subscribe(onNext, {})