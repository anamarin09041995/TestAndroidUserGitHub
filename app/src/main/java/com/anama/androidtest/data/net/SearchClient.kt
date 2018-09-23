package com.anama.androidtest.data.net

import com.anama.androidtest.data.model.User
import io.reactivex.Observable
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Path

interface SearchClient{

    @GET("{name}")
    fun searchUser(@Path ("name") name: String): Single<User>
}