package com.iconfinderdemo.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.paging.LivePagedListBuilder
import androidx.paging.PageKeyedDataSource
import androidx.paging.PagedList
import com.iconfinderdemo.repository.IconDataSource
import com.iconfinderdemo.repository.IconDataSourceFactory
import com.iconfinderdemo.model.IconSet
import com.iconfinderdemo.network.IconApiService
import io.reactivex.disposables.CompositeDisposable

class IconViewModel: ViewModel() {

     var iconPagedList : LiveData<PagedList<IconSet>>
     private var liveDataSource : LiveData<PageKeyedDataSource<Int,IconSet>>
     private var compositeDisposable:CompositeDisposable = CompositeDisposable()
     public var iconLoadError = MutableLiveData<Boolean>()
     public var loading = MutableLiveData<Boolean>()

    init {
        val iconApiService=IconApiService()
        val  iconDataSourceFactory = IconDataSourceFactory(iconApiService,compositeDisposable)
        liveDataSource = iconDataSourceFactory.iconLiveDataSource
        val config = PagedList.Config.Builder()
            .setEnablePlaceholders(false)
            .setPageSize(IconDataSource.PAGE_SIZE)
            .build()
        iconPagedList= LivePagedListBuilder(iconDataSourceFactory,config).build()
        iconLoadError = iconDataSourceFactory.iconDataSource.iconLoadError
        loading = iconDataSourceFactory.iconDataSource.loading
    }

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.dispose()
    }
}