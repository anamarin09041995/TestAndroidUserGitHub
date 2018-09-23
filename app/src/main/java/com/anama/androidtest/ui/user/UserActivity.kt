package com.anama.androidtest.ui.user

import android.app.SearchManager
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.SearchView
import android.view.Menu
import com.anama.androidtest.R
import com.anama.androidtest.data.model.User
import com.anama.androidtest.databinding.ActivityUserBinding
import com.anama.androidtest.ui.detail.DetailActivity
import com.anama.androidtest.util.LifeDisposable
import com.anama.androidtest.util.subscribeByAction
import com.jakewharton.rxbinding2.support.v7.widget.queryTextChangeEvents
import com.jakewharton.rxbinding2.view.clicks
import io.reactivex.subjects.PublishSubject
import kotlinx.android.synthetic.main.activity_user.*
import org.jetbrains.anko.startActivity
import org.jetbrains.anko.toast
import org.koin.android.viewmodel.ext.android.viewModel

class UserActivity : AppCompatActivity() {


    lateinit var binding: ActivityUserBinding
    var dis: LifeDisposable = LifeDisposable(this)
    var query: String? = null
    val vm: UserViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_user)
        supportActionBar!!.setTitle("Busqueda de usuarios")
    }

    override fun onResume() {
        super.onResume()
        binding.loader = vm.loading

        dis add vm.searchUser()
                .subscribeByAction(
                        onNext = { binding.user = it },
                        onHttpError = { toast(it) },
                        onError = { toast(it.message!!) }
                )

        dis add userLayout.clicks()
                .subscribe {
                    startActivity<DetailActivity>(USER to binding.user)
                }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.search, menu)
        val searchManager = getSystemService(SEARCH_SERVICE) as SearchManager
        val searchView = menu.findItem(R.id.action_search).actionView as SearchView
        searchView.setSearchableInfo(searchManager.getSearchableInfo(componentName))
        searchView.setIconifiedByDefault(true)
        dis add searchView.queryTextChangeEvents()
                .subscribe {
                    if (it.isSubmitted) vm.query.onNext(it.queryText().toString())
                }
        return true
    }

    companion object {
        val USER: String = "user"
    }

}
