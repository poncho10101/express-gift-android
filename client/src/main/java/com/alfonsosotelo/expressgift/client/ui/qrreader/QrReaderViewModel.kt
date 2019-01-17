package com.alfonsosotelo.expressgift.client.ui.qrreader

import android.app.Application
import com.alfonsosotelo.expressgift.client.R
import com.alfonsosotelo.expressgift.data.base.BaseViewModel
import com.alfonsosotelo.expressgift.data.entities.QrData
import com.alfonsosotelo.expressgift.data.utils.DataState
import com.google.gson.Gson

/**
 * ViewModel of a QrData
 */
class QrReaderViewModel(application: Application): BaseViewModel<QrData>(application), QrReaded {

    /**
     * Send here the string of a qr scanned, if it is correct will post in data
     * @param json qr scanned text
     */
    override fun onQrReaded(json: String) {
        setStatus(DataState.LOADING)

        val qrData = Gson().fromJson(json, QrData::class.java)
        if (qrData.id > 0) {
            data.postValue(qrData)
        } else {
            setStatus(DataState.ERROR, getString(R.string.qr_code_error))
        }
    }
}


interface QrReaded { // This interface is used to be able to use onQrReaded in xml data binding
    fun onQrReaded(json: String)
}