package com.iconfinderdemo.repository

import androidx.lifecycle.MutableLiveData
import androidx.paging.PageKeyedDataSource
import com.iconfinderdemo.model.IconSet
import com.iconfinderdemo.network.IconApiService
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

class IconSetDataSource(
    private val iconApiSerive: IconApiService,
    private val disposable: CompositeDisposable
) :
    PageKeyedDataSource<Int, IconSet>() {
    companion object {
        val PAGE_SIZE = 15
        val FIRST_PAGE = 1
        var LAST_ICON_SET_ID = 0
    }

    val iconLoadError = MutableLiveData<Boolean>()
    val loading = MutableLiveData<Boolean>()

    override fun loadInitial(
        params: LoadInitialParams<Int>,
        callback: LoadInitialCallback<Int, IconSet>
    ) {
        disposable.add(
            iconApiSerive.getIconSets(PAGE_SIZE).subscribeOn(Schedulers.io())
                .subscribe({
                    //No Error got success
                    iconLoadError.postValue(false)
                    //stop loading
                    loading.postValue(false)
                    LAST_ICON_SET_ID = it.iconSets.last().iconSetId
                    callback.onResult(it.iconSets, null, FIRST_PAGE + 1)
                }
                ) {
                    //get error
                    iconLoadError.postValue(true)
                    //stop loading
                    loading.postValue(false)
                }
        )
    }

    override fun loadBefore(params: LoadParams<Int>, callback: LoadCallback<Int, IconSet>) {
        loading.postValue(true)
        disposable.add(
            iconApiSerive.getIconSets(PAGE_SIZE, LAST_ICON_SET_ID).subscribeOn(Schedulers.io())
                .subscribe({
                    it?.let {
                        val key = if (params.key > FIRST_PAGE) params.key - 1 else null
                        //No Error got success
                        iconLoadError.postValue(false)
                        //stop loading
                        loading.postValue(false)
                        LAST_ICON_SET_ID = it.iconSets.last().iconSetId
                        callback.onResult(it.iconSets, key)
                    }
                },
                    {
                        //get error
                        iconLoadError.postValue(true)
                        //stop loading
                        loading.postValue(false)
                    })
        )

    }

    override fun loadAfter(params: LoadParams<Int>, callback: LoadCallback<Int, IconSet>) {
        loading.postValue(true)
        disposable.add(
            iconApiSerive.getIconSets(PAGE_SIZE, LAST_ICON_SET_ID).subscribeOn(Schedulers.io())
                .subscribe({
                    it?.let {
                        //getting last page key
                        val key = if (it.totalCount > params.key) params.key + 1 else null
                        //No Error got success
                        iconLoadError.postValue(false)
                        //stop loading
                        loading.postValue(false)
                        LAST_ICON_SET_ID = it.iconSets.last().iconSetId
                        callback.onResult(it.iconSets, key)
                    }
                },
                    {
                        //get error
                        iconLoadError.postValue(true)
                        //stop loading
                        loading.postValue(false)
                    })
        )
    }
}