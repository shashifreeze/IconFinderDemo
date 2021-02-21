package com.iconfinderdemo.viewmodel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.iconfinderdemo.model.IconSet
import com.iconfinderdemo.network.IconApiService
import com.iconfinderdemo.network.apiresponse.IconSetResponse
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.observers.DisposableSingleObserver
import io.reactivex.schedulers.Schedulers

class IconListViewModel: ViewModel(){

    //@Inject
    var iconApiService:IconApiService
    private val disposable = CompositeDisposable()
    val iconSet = MutableLiveData<List<IconSet>>()
    val loadError = MutableLiveData<Boolean>()
    val loading = MutableLiveData<Boolean>()

    init {
        //DaggerApiComponent.create().inject(this)
        iconApiService = IconApiService()
    }

    fun fetchIcons()
    {
        loading.value = true
        disposable.add(
            iconApiService.getIconSets(10,1).subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableSingleObserver<IconSetResponse>() {
                    override fun onSuccess(value: IconSetResponse?) {
                        value?.let {
                            iconSet.value = it.iconSets
                            loading.value=false
                            loadError.value = false
                        }

                    }

                    override fun onError(e: Throwable?) {
                        loading.value=false
                        loadError.value = true
                        Log.d("IconList","${e?.message}")
                    }

                })
        )
    }

    override fun onCleared() {
        super.onCleared()
        disposable.clear()
    }

}