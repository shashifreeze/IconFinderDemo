package com.iconfinderdemo.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.paging.LivePagedListBuilder
import androidx.paging.PageKeyedDataSource
import androidx.paging.PagedList
import com.iconfinderdemo.model.Icon
import com.iconfinderdemo.repository.IconSetDataSource
import com.iconfinderdemo.repository.IconSetDataSourceFactory
import com.iconfinderdemo.model.IconSet
import com.iconfinderdemo.network.IconApiService
import com.iconfinderdemo.repository.IconDataSource
import com.iconfinderdemo.repository.IconDataSourceFactory
import io.reactivex.disposables.CompositeDisposable
/*
Not using this class as pagination was not working properly with this class for icons list
 */
class IconViewModel(): ViewModel() {

     lateinit var iconPagedList : LiveData<PagedList<Icon>>
     private lateinit var liveDataSource : LiveData<PageKeyedDataSource<Int,Icon>>
     private var compositeDisposable:CompositeDisposable = CompositeDisposable()
     public var iconLoadError = MutableLiveData<Boolean>()
     public var loading = MutableLiveData<Boolean>()

    fun initViewModel(iconSetId:Int){
        val iconApiService=IconApiService()
        val  iconDataSourceFactory = IconDataSourceFactory(iconApiService,compositeDisposable,iconSetId)
        liveDataSource = iconDataSourceFactory.iconLiveDataSource
        val config = PagedList.Config.Builder()
            .setEnablePlaceholders(false)
            .setPageSize(IconDataSource.PAGE_SIZE)
            .build()
        iconPagedList= LivePagedListBuilder(iconDataSourceFactory,config).build()
        iconLoadError = iconDataSourceFactory.iconSetDataSource.iconLoadError
        loading = iconDataSourceFactory.iconSetDataSource.loading
    }

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.dispose()
    }
}