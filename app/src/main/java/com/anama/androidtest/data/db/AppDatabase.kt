package com.anama.androidtest.data.db

import android.arch.persistence.room.Database
import android.arch.persistence.room.RoomDatabase
import com.anama.androidtest.data.model.User

@Database(entities = arrayOf(User::class), version = 4)

abstract class AppDatabase: RoomDatabase(){

    abstract fun userDao(): UserDao
}