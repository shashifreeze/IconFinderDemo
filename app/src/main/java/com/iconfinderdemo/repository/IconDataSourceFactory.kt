package com.iconfinderdemo.repository


import androidx.lifecycle.MutableLiveData
import androidx.paging.DataSource
import androidx.paging.DataSource.Factory
import androidx.paging.PageKeyedDataSource
import com.iconfinderdemo.model.Icon
import com.iconfinderdemo.model.IconSet
import com.iconfinderdemo.network.IconApiService
import io.reactivex.disposables.CompositeDisposable

class IconDataSourceFactory(iconApiSerive: IconApiService, compositDisposable:CompositeDisposable, iconSetId:Int) : Factory<Int, Icon>() {

    val iconLiveDataSource = MutableLiveData<PageKeyedDataSource<Int, Icon>>()
    public var iconSetDataSource: IconDataSource = IconDataSource(iconApiSerive,compositDisposable,iconSetId)
    override fun create(): DataSource<Int, Icon> {
        iconLiveDataSource.postValue(iconSetDataSource)
        return iconSetDataSource
    }
}