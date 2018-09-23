package com.anama.androidtest.ui.detail

import android.databinding.DataBindingUtil
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.anama.androidtest.R
import com.anama.androidtest.data.model.User
import com.anama.androidtest.databinding.ActivityUserBinding

class DetailActivity : AppCompatActivity() {

    lateinit var binding: ActivityUserBinding

    val user: User by lazy { intent.extras.getParcelable<User>(USER) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_user)

        supportActionBar!!.setDisplayShowHomeEnabled(true)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.setTitle("Detalle")

        binding.user = user
    }

    override fun onResume() {
        super.onResume()
    }

    companion object {
        val USER: String = "user"
    }
}
