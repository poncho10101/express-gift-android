package com.alfonsosotelo.expressgift.vendor.ui.qrgen

import android.app.Application
import androidx.lifecycle.MutableLiveData
import com.alfonsosotelo.expressgift.data.base.BaseViewModel
import com.alfonsosotelo.expressgift.data.entities.QrData
import com.alfonsosotelo.expressgift.data.entities.Vendor
import com.alfonsosotelo.expressgift.data.utils.addToDisposables
import com.alfonsosotelo.expressgift.data.utils.default
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import java.util.*
import java.util.concurrent.TimeUnit

/**
 * QrData ViewModel
 */
class QrGenViewModel(application: Application): BaseViewModel<QrData>(application) {

    private val timer = Observable.interval(1, TimeUnit.SECONDS)
        .timeInterval()
        .observeOn(AndroidSchedulers.mainThread())
    private var timerDisposables: Disposable? = null
    var qrExpireTime = MutableLiveData<Int>().default(0)


    /**
     * This method generates a new QrCode and puts in data
     */
    fun generateQRCode() {
        initTimer()
        data.postValue(
            QrData().apply {
                id = Math.abs(Random().nextInt())
                vendor = Vendor.getSelf()
                preferedGift = vendor?.getPreferedGift()
            }
        )
    }

    /**
     * This method initializes the timer count, and when it comes to 0 calls to generateQRCode()
     */
    fun initTimer() {
        timerDisposables?.dispose()
        timerDisposables = timer.subscribe {
            val time = it.value().toInt()
            if(time < QR_CODE_EXPIRE_TIME) {
                qrExpireTime.postValue(QR_CODE_EXPIRE_TIME - time)
            } else {
                qrExpireTime.postValue(0)
                generateQRCode()
            }
        }
        timerDisposables?.addToDisposables(disposables)
    }

    companion object {
        private const val QR_CODE_EXPIRE_TIME = 60
    }
}