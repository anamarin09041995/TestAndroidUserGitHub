package com.anama.androidtest.data.db

import android.arch.persistence.room.Dao
import android.arch.persistence.room.Insert
import android.arch.persistence.room.Query
import android.arch.persistence.room.Update
import com.anama.androidtest.data.model.User
import io.reactivex.Maybe


@Dao
interface UserDao{

    @Query("SELECT * FROM user limit 1")
    fun  getUser(): Maybe<User>

    @Update()
    fun update(user:User)

    @Insert()
    fun insert(user:User)
}