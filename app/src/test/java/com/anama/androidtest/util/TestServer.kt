package com.anama.androidtest.util

import com.anama.androidtest.data.model.User
import com.google.gson.Gson
import okhttp3.HttpUrl
import okhttp3.mockwebserver.Dispatcher
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import okhttp3.mockwebserver.RecordedRequest

object TestServer {

    val httpUrl: HttpUrl by lazy { prepareServer() }
    private val gson: Gson = Gson()


    private fun prepareServer(): HttpUrl {
        val server: MockWebServer = MockWebServer()

        val dispatcher: Dispatcher = object : Dispatcher() {
            override fun dispatch(request: RecordedRequest): MockResponse = when (request.path) {
                "/anita" -> {
                    val user = User().apply {
                        name = "Anita"
                        avatarUrl = "url"
                    }
                    MockResponse().setResponseCode(200)
                            .setBody(gson.toJson(user))
                }
                else -> MockResponse().setResponseCode(404)
            }
        }

        server.setDispatcher(dispatcher)

        return server.url("/")
    }

}