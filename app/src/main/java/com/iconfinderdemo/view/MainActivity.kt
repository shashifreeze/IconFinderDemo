package com.iconfinderdemo.view

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.iconfinderdemo.R
import com.iconfinderdemo.adapters.IconSetAdapter
import com.iconfinderdemo.viewmodel.IconViewModel
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    lateinit var viewModel: IconViewModel
    lateinit var iconSetAdapter: IconSetAdapter
    private val TAG = "IconListTAG"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        viewModel = ViewModelProviders.of(this).get(IconViewModel::class.java)
        iconSetAdapter = IconSetAdapter(this)

        iconRV.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = iconSetAdapter
        }
        observeViewModel()
    }

    private fun observeViewModel() {

        viewModel.loading.postValue(true)
        viewModel.iconLoadError.postValue(false)
        viewModel.iconPagedList.observe(this, Observer {
            iconSetAdapter.submitList(it)

        })

        viewModel.loading.observe(this, Observer {
            if (it) {
                pBar.visibility = View.VISIBLE
                errorMessageTV.visibility = View.INVISIBLE
            } else {
                pBar.visibility = View.INVISIBLE
                iconRV.visibility = View.VISIBLE
            }
        })

        viewModel.iconLoadError.observe(this, Observer {
            if (it) {
                iconRV.visibility = View.INVISIBLE
                pBar.visibility = View.INVISIBLE
                errorMessageTV.visibility = View.VISIBLE
            } else {
                errorMessageTV.visibility = View.INVISIBLE
                iconRV.visibility = View.VISIBLE
            }
        })
    }
}