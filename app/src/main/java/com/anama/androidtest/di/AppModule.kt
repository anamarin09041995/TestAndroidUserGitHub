package com.anama.androidtest.di

import android.arch.persistence.room.Room
import com.anama.androidtest.R
import com.anama.androidtest.data.db.AppDatabase
import com.anama.androidtest.data.db.UserDao
import com.anama.androidtest.data.net.SearchClient
import com.anama.androidtest.data.repository.UserRepository
import com.anama.androidtest.ui.user.UserViewModel
import io.reactivex.schedulers.Schedulers
import org.koin.android.ext.koin.androidApplication
import org.koin.android.viewmodel.experimental.builder.viewModel
import org.koin.dsl.module.module
import org.koin.experimental.builder.single
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

val appModule = module {

    single {
        Retrofit
                .Builder()
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.createWithScheduler(Schedulers.io()))
                .baseUrl(androidApplication().getString(R.string.base_url))
                .build()
    }

    single<SearchClient>{ get<Retrofit>().create(SearchClient::class.java) }

    single {
        Room
                .databaseBuilder(androidApplication(), AppDatabase::class.java, "user.db")
                .fallbackToDestructiveMigration()
                .build()
    }

    single{ get<AppDatabase>().userDao()}
    single<UserRepository>()

    viewModel<UserViewModel>()

}