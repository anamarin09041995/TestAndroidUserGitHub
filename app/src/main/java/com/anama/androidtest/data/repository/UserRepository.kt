package com.anama.androidtest.data.repository

import com.anama.androidtest.data.db.UserDao
import com.anama.androidtest.data.model.User
import com.anama.androidtest.data.net.SearchClient
import com.anama.androidtest.util.applySchedulers
import io.reactivex.Maybe

class UserRepository(private val dao: UserDao, private val client: SearchClient) {

    fun userByName(name: String): Maybe<User> = Maybe.just(name)
            .flatMap { if (it == "") dao.getUser() else query(name) }
            .applySchedulers()


    private fun query(name: String): Maybe<User> = client.searchUser(name)
            .flatMapMaybe { usr ->
                dao.getUser()
                        .flatMap { cache -> Maybe.fromCallable { dao.update(usr.apply { idUser = cache.idUser }) } }
                        .switchIfEmpty(Maybe.fromCallable { dao.insert(usr) })
                        .map { usr }
            }

}