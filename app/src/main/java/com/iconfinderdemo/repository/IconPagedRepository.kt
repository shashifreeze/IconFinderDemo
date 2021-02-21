package com.iconfinderdemo.repository

import androidx.lifecycle.LiveData
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import com.iconfinderdemo.model.IconSet
import com.iconfinderdemo.network.IconApiService
import io.reactivex.disposables.CompositeDisposable

class IconPagedRepository(private val api:IconApiService) {
    lateinit var iconPagedList : LiveData<PagedList<IconSet>>
    lateinit var liveDataSourceFactory : IconDataSourceFactory

   fun fetchLiveIconPagedList(compositDisposable:CompositeDisposable):LiveData<PagedList<IconSet>>
   {
       liveDataSourceFactory = IconDataSourceFactory(api,compositDisposable)
       val config = PagedList.Config.Builder()
           .setEnablePlaceholders(false)
           .setPageSize(IconDataSource.PAGE_SIZE)
           .build()
       iconPagedList= LivePagedListBuilder(liveDataSourceFactory,config).build()
       return iconPagedList
   }

//    fun getIconSetLiveData():LiveData<List<IconSet>>
//    {
//        return Transformations.switchMap(liveDataSourceFactory.iconLiveDataSource,IconDataSource::)
//    }
}