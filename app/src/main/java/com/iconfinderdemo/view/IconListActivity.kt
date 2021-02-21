package com.iconfinderdemo.view

import android.Manifest
import android.annotation.TargetApi
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.GridLayoutManager
import com.iconfinderdemo.R
import com.iconfinderdemo.adapters.IconListAdapter
import com.iconfinderdemo.model.Icon
import com.iconfinderdemo.util.Constants.KEY_ICON_COUNT
import com.iconfinderdemo.util.Constants.KEY_ICON_SET_ID
import com.iconfinderdemo.util.Constants.MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE
import com.iconfinderdemo.util.Downloader
import com.iconfinderdemo.util.downloadImage
import com.iconfinderdemo.viewmodel.ListViewModel
import kotlinx.android.synthetic.main.activity_icon_list.*

class IconListActivity : AppCompatActivity(),Downloader {
    lateinit var iconViewModel: ListViewModel
    lateinit var iconAdapter: IconListAdapter
    lateinit var imageUrl : String
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_icon_list)
        val iconSetId = intent.getIntExtra(KEY_ICON_SET_ID, 0)
        val totalIconCount = intent.getIntExtra(KEY_ICON_COUNT, 10)
        //getting viewmodel object
        iconViewModel = ViewModelProviders.of(this).get(ListViewModel::class.java)
        //iconViewModel.initViewModel(iconSetId)
        iconViewModel.refresh(iconSetId,totalIconCount)
        iconAdapter = IconListAdapter(ArrayList<Icon>(),this)
        val mlayoutManager = GridLayoutManager(applicationContext,4)

        iconRV.apply {
            layoutManager = mlayoutManager
            adapter = iconAdapter
        }
        //observe model live data
        observeViewModel()
    }

    private fun observeViewModel() {
        iconViewModel.loading.postValue(true)
        iconViewModel.iconLoadError.postValue(false)
//        iconViewModel.iconPagedList.observe(this, Observer {
//            iconAdapter.submitList(it)
//            iconRV.visibility= View.VISIBLE
//        })
        iconViewModel.icons.observe(this, Observer {
            iconAdapter.updateIcons(it)
        })

        iconViewModel.loading.observe(this, Observer {
            if (it) {
                iconPBar.visibility = View.VISIBLE
                iconLoadError.visibility = View.INVISIBLE
            } else {
                iconPBar.visibility = View.INVISIBLE
                iconRV.visibility = View.VISIBLE
            }
        })

        iconViewModel.iconLoadError.observe(this, Observer {
            if (it) {
                iconRV.visibility = View.INVISIBLE
                iconPBar.visibility = View.INVISIBLE
                iconLoadError.visibility = View.VISIBLE
            } else {
                iconLoadError.visibility = View.INVISIBLE
                iconRV.visibility = View.VISIBLE
            }
        })
    }

    override fun donwload(url: String) {
        //assigning globle variable  to use url globally in this class
        imageUrl = url
        // After API 23 (Marshmallow) and lower Android 10 need to ask permission for storage access
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && Build.VERSION.SDK_INT < Build.VERSION_CODES.Q) {
            askPermissions(imageUrl)
        } else {
            downloadImage(imageUrl)
            Toast.makeText(applicationContext,"Icon will be downloaded in Picture directory",Toast.LENGTH_LONG).show()
        }
    }

    @TargetApi(Build.VERSION_CODES.M)
    fun askPermissions(imageUrl:String) {
        if (ContextCompat.checkSelfPermission(
                applicationContext,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            // Permission is not granted
            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(
                    this,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE
                )
            ) {
                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.
                AlertDialog.Builder(this)
                    .setTitle("Permission required")
                    .setMessage("Permission required to save photos from the Web.")
                    .setPositiveButton("Allow") { dialog, id ->
                        ActivityCompat.requestPermissions(
                            this,
                            arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE),
                            MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE
                        )
                        finish()
                    }
                    .setNegativeButton("Deny") { dialog, id -> dialog.cancel() }
                    .show()
            } else {
                // No explanation needed, we can request the permission.
                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE),
                    MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE
                )
                // MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE is an
                // app-defined int constant. The callback method gets the
                // result of the request.

            }
        } else {
            // Permission has already been granted
            downloadImage(imageUrl)
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        when (requestCode) {
            MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE -> {
                // If request is cancelled, the result arrays are empty.
                if ((grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                    // permission was granted, yay!
                    // Download the Image
                    downloadImage(imageUrl)
                }
                return
            }
        }
    }
}