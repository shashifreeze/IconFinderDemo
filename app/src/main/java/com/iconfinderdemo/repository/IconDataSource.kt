package com.iconfinderdemo.repository

import androidx.lifecycle.MutableLiveData
import androidx.paging.PageKeyedDataSource
import com.iconfinderdemo.model.Icon
import com.iconfinderdemo.model.IconSet
import com.iconfinderdemo.network.IconApiService
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

class IconDataSource(
    private val iconApiSerive: IconApiService,
    private val disposable: CompositeDisposable,
    private val iconSetId: Int
) :
    PageKeyedDataSource<Int, Icon>() {
    companion object {
        val PAGE_SIZE = 16
        val FIRST_PAGE = 1
    }

    val iconLoadError = MutableLiveData<Boolean>()
    val loading = MutableLiveData<Boolean>()

    override fun loadInitial(
        params: LoadInitialParams<Int>,
        callback: LoadInitialCallback<Int, Icon>
    ) {
        disposable.add(
            iconApiSerive.getAllIconsOfIconSet(iconSetId,PAGE_SIZE).subscribeOn(Schedulers.io())
                .subscribe({
                    //No Error got success
                    iconLoadError.postValue(false)
                    //stop loading
                    loading.postValue(false)
                    callback.onResult(it.icons, null, FIRST_PAGE + 1)
                }
                ) {
                    //get error
                    iconLoadError.postValue(true)
                    //stop loading
                    loading.postValue(false)
                }
        )
    }

    override fun loadBefore(params: LoadParams<Int>, callback: LoadCallback<Int, Icon>) {
        loading.postValue(true)
        disposable.add(
            iconApiSerive.getAllIconsOfIconSet(iconSetId,PAGE_SIZE).subscribeOn(Schedulers.io())
                .subscribe({
                    it?.let {
                        val key = if (params.key > FIRST_PAGE) params.key - 1 else null
                        //No Error got success
                        iconLoadError.postValue(false)
                        //stop loading
                        loading.postValue(false)

                        callback.onResult(it.icons, key)
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

    override fun loadAfter(params: LoadParams<Int>, callback: LoadCallback<Int, Icon>) {
        loading.postValue(true)
        disposable.add(
            iconApiSerive.getAllIconsOfIconSet(iconSetId,PAGE_SIZE).subscribeOn(Schedulers.io())
                .subscribe({
                    it?.let {
                        //getting last page key
                        val key = if ((it.totalCount) > params.key) params.key + 1 else null
                        //No Error got success
                        iconLoadError.postValue(false)
                        //stop loading
                        loading.postValue(false)
                        callback.onResult(it.icons, key)
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