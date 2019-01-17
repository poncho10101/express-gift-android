package com.alfonsosotelo.expressgift.client.ui.giftlist

import android.app.Application
import androidx.lifecycle.MutableLiveData
import com.alfonsosotelo.expressgift.client.R
import com.alfonsosotelo.expressgift.data.base.BaseViewModel
import com.alfonsosotelo.expressgift.data.entities.Gift
import com.alfonsosotelo.expressgift.data.entities.QrData
import com.alfonsosotelo.expressgift.data.utils.DataState

/**
 * ViewModel of a GiftList
 */
class GiftListViewModel(application: Application): BaseViewModel<List<Gift>>(application) {
    override var data = MutableLiveData<List<Gift>>()
    var giftSelected = MutableLiveData<Gift>()

    /**
     * Load Gifts from a vendor, obtained of a QrData
     * @param qrData QrData entity with the vendor data
     */
    fun loadGiftList(qrData: QrData) {
        qrData.vendor?.let {
            data.postValue(Gift.getGiftsFromVendor(it))
        } ?: run {
            setStatus(DataState.ERROR, getString(R.string.vendor_is_missing))
        }
    }
}