package com.alfonsosotelo.expressgift.data.base

import android.app.Application
import androidx.annotation.StringRes
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.alfonsosotelo.expressgift.data.utils.DataState
import com.alfonsosotelo.expressgift.data.utils.DataStatus
import com.alfonsosotelo.expressgift.data.utils.default
import io.reactivex.disposables.CompositeDisposable

/**
 * Base ViewModel, you should inherits from this class in the ViewModels
 * T: the data LiveData type, if you will don't need it, you can use Unit
 */
abstract class BaseViewModel<T>(application: Application): AndroidViewModel(application) {
    open var data = MutableLiveData<T>() // the data of an entity/list or what you need
    open var status = MutableLiveData<DataStatus>().default(DataStatus(DataState.SUCCESS)) // status of the process
    protected val disposables = CompositeDisposable() // Rx disposables need to be added here


    /**
     * disposes the disposables
     */
    override fun onCleared() {
        disposables.clear()
        super.onCleared()
    }


    /**
     * use this method to change the status, easier than status.postValue(DataStatus(dataState, message?))
     * @param dataState the State of the Status
     * @param message optional message, can be used to error messages or successful actions
     * @sample setStatus(DataState.ERROR)
     */
    fun setStatus(dataState: DataState, message: String? = null) {
        status.postValue(DataStatus(dataState, message))
    }

    /**
     * use this method to get strings from resources
     * @param stringRes Resource Id
     */
    fun getString(@StringRes stringRes: Int): String {
        return getApplication<Application>().getString(stringRes)
    }
}