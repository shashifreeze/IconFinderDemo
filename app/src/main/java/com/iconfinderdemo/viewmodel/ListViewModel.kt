package com.iconfinderdemo.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.iconfinderdemo.model.Icon
import com.iconfinderdemo.network.IconApiService
import com.iconfinderdemo.network.apiresponse.IconResponse
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.observers.DisposableSingleObserver
import io.reactivex.schedulers.Schedulers

class ListViewModel : ViewModel() {

    var apiService:IconApiService = IconApiService()
    val disposable = CompositeDisposable()
    val icons = MutableLiveData<List<Icon>>()
    val iconLoadError = MutableLiveData<Boolean>()
    val loading = MutableLiveData<Boolean>()

    fun refresh(iconSetId:Int,totalIcon: Int) {
        fetchIcons(iconSetId,totalIcon)
    }

    private fun fetchIcons(iconSetId:Int,totalIcon: Int) {

        loading.value = true
        disposable.add(
            apiService.getAllIconsOfIconSet(iconSetId,totalIcon)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableSingleObserver<IconResponse>() {
                    override fun onSuccess(value: IconResponse?) {
                        icons.value = value?.icons
                        loading.value = false
                        iconLoadError.value = false
                    }
                    override fun onError(e: Throwable?) {
                        loading.value = false
                        iconLoadError.value=true
                    }
                })
        )
    }

    override fun onCleared() {
        super.onCleared()
        disposable.clear()
    }
}