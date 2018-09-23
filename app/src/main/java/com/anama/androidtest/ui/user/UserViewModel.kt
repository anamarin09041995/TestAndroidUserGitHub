package com.anama.androidtest.ui.user

import android.arch.lifecycle.ViewModel
import android.databinding.ObservableBoolean
import com.anama.androidtest.data.model.User
import com.anama.androidtest.data.repository.UserRepository
import io.reactivex.Observable
import io.reactivex.subjects.PublishSubject

class UserViewModel(private val repository: UserRepository) : ViewModel() {

    val loading: ObservableBoolean = ObservableBoolean(false)
    val query: PublishSubject<String> = PublishSubject.create()


    fun searchUser(): Observable<User> = query
            .startWith("")
            .doOnNext { loading.set(true) }
            .flatMapMaybe { q: String ->
                repository.userByName(q)
                        .doOnEvent { _, _ -> loading.set(false) }
            }


}