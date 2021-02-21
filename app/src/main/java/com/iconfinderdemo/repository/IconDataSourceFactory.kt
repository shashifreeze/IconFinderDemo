package com.iconfinderdemo.repository


import androidx.lifecycle.MutableLiveData
import androidx.paging.DataSource
import androidx.paging.DataSource.Factory
import androidx.paging.PageKeyedDataSource
import com.iconfinderdemo.model.IconSet
import com.iconfinderdemo.network.IconApiService
import io.reactivex.disposables.CompositeDisposable

class IconDataSourceFactory(private val iconApiSerive: IconApiService, private val compositDisposable:CompositeDisposable) : Factory<Int, IconSet>() {

    val iconLiveDataSource = MutableLiveData<PageKeyedDataSource<Int, IconSet>>()
    public var iconDataSource: IconDataSource = IconDataSource(iconApiSerive,compositDisposable)
    override fun create(): DataSource<Int, IconSet> {
        iconLiveDataSource.postValue(iconDataSource)
        return iconDataSource
    }
}