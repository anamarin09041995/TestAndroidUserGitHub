package com.anama.androidtest

import com.anama.androidtest.data.db.UserDao
import com.anama.androidtest.data.model.User
import com.anama.androidtest.data.net.SearchClient
import com.anama.androidtest.data.repository.UserRepository
import com.anama.androidtest.util.TestServer
import com.anama.androidtest.util.TrampolineSchedulerRule
import io.reactivex.Maybe
import io.reactivex.schedulers.Schedulers
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner
import retrofit2.HttpException
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import org.mockito.Mockito.`when` as mWhen


@RunWith(MockitoJUnitRunner::class)
class UserRepositoryTest{

    @Rule
    @JvmField
    val trampolineSchedulerRule: TrampolineSchedulerRule = TrampolineSchedulerRule()

    @Mock
    lateinit var dao:UserDao

    lateinit var client:SearchClient

    lateinit var repository:UserRepository

    @Before
    fun prepare() {

        val retrofit =  Retrofit
                .Builder()
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.createWithScheduler(Schedulers.io()))
                .baseUrl(TestServer.httpUrl)
                .build()

        client = retrofit.create(SearchClient::class.java)
        repository = UserRepository(dao, client)

    }

    @Test
    fun cache_isEmpty(){
        mWhen(dao.getUser()).thenReturn(Maybe.empty<User>())
        repository.userByName("")
                .test()
                .assertComplete()
    }

    @Test
    fun cache_isSuccess(){
        val user = User().apply {
            name = "Milo2005"
            avatarUrl = ""
        }
        mWhen(dao.getUser()).thenReturn(Maybe.just(user))
        repository.userByName("")
                .test()
                .assertValue { it.name == user.name }
    }

    @Test
    fun query_isSuccess_noCache(){
        mWhen(dao.getUser()).thenReturn(Maybe.empty<User>())

        repository.userByName("anita")
                .test()
                .assertValue { it.name == "Anita" }


    }

    @Test
    fun query_isSuccess_withCache(){
        val user = User().apply {
            idUser = 1234
            name = "Anita"
            avatarUrl = "url"
        }
        mWhen(dao.getUser()).thenReturn(Maybe.just(user))

        repository.userByName("anita")
                .test()
                .assertValue { it.name == "Anita" }



    }

    @Test
    fun query_notFound(){

        repository.userByName("maria")
                .test()
                .assertError {
                    val ex = it as HttpException
                    ex.code() == 404
                }
    }

}