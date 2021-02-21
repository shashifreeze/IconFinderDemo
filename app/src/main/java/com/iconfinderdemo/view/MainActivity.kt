package com.iconfinderdemo.view

import android.content.pm.PackageManager
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.iconfinderdemo.R
import com.iconfinderdemo.adapters.IconSetAdapter
import com.iconfinderdemo.util.Constants.MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE
import com.iconfinderdemo.viewmodel.IconSetViewModel
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    lateinit var setViewModel: IconSetViewModel
    lateinit var iconSetAdapter: IconSetAdapter
    private val TAG = "IconListTAG"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setViewModel = ViewModelProviders.of(this).get(IconSetViewModel::class.java)
        iconSetAdapter = IconSetAdapter(this)
        iconSetRV.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = iconSetAdapter
        }
        observeViewModel()
    }

    private fun observeViewModel() {

        setViewModel.loading.postValue(true)
        setViewModel.iconLoadError.postValue(false)
        setViewModel.iconPagedList.observe(this, Observer {
            iconSetAdapter.submitList(it)

        })

        setViewModel.loading.observe(this, Observer {
            if (it) {
                pBar.visibility = View.VISIBLE
                errorMessageTV.visibility = View.INVISIBLE
            } else {
                pBar.visibility = View.INVISIBLE
                iconSetRV.visibility = View.VISIBLE
            }
        })

        setViewModel.iconLoadError.observe(this, Observer {
            if (it) {
                iconSetRV.visibility = View.INVISIBLE
                pBar.visibility = View.INVISIBLE
                errorMessageTV.visibility = View.VISIBLE
            } else {
                errorMessageTV.visibility = View.INVISIBLE
                iconSetRV.visibility = View.VISIBLE
            }
        })
    }

}