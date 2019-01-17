package com.alfonsosotelo.expressgift.vendor.ui

import android.app.Application
import androidx.lifecycle.MutableLiveData
import com.alfonsosotelo.expressgift.data.base.BaseViewModel
import com.alfonsosotelo.expressgift.data.entities.Gift
import com.alfonsosotelo.expressgift.data.ui.ToolbarHandler
import com.google.android.gms.nearby.messages.Message
import com.google.gson.Gson

/**
 * This ViewModel is for share data between Fragments and parent Activity
 * call it using the Activity as Provider
 */
class SharedViewModel(application: Application): BaseViewModel<Unit>(application) {

    // Contains the data to the toolbar, should be modified from the fragments
    // Activity should observe and bind the toolbar
    var toolbarHandler = MutableLiveData<ToolbarHandler>()
    var qrAuthStatus = MutableLiveData<String>()
    var giftSelected = MutableLiveData<Gift>()


    /**
     * This method handle Messages received from Client
     */
    fun notifyReceived(message: Message) {
        val messageContent = String(message.content)
        when(message.type) {
            SCAN_STATUS -> qrAuthStatus.postValue(messageContent) // If the message type was ScanStatus puts it in qrAuthStatus
            GIFT_SELECTED -> { // If the message type was GiftSelected puts it in giftSelected
                Gson().fromJson(messageContent, Gift::class.java)?.takeIf { it.id != 0L }?.let {
                    giftSelected.postValue(it)
                } ?: run {
                    giftSelected.postValue(null)
                }
            }
        }
    }

    companion object {
        const val QR_SCANNED_AUTHORIZED = "qr_authorized"
        const val QR_SCANNED_DECLINED = "qr_declined"

        const val SCAN_STATUS = "scan_status"
        const val GIFT_SELECTED = "gift_selected"
    }
}