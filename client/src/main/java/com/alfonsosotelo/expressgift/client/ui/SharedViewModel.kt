package com.alfonsosotelo.expressgift.client.ui

import android.app.Activity
import android.app.Application
import android.content.Context
import androidx.lifecycle.MutableLiveData
import com.alfonsosotelo.expressgift.data.base.BaseViewModel
import com.alfonsosotelo.expressgift.data.entities.Gift
import com.alfonsosotelo.expressgift.data.entities.QrData
import com.alfonsosotelo.expressgift.data.ui.ToolbarHandler
import com.alfonsosotelo.expressgift.data.utils.DataState
import com.alfonsosotelo.expressgift.data.utils.default
import com.google.android.gms.nearby.Nearby
import com.google.android.gms.nearby.messages.Message
import com.google.android.gms.nearby.messages.MessageListener
import com.google.gson.Gson

/**
 * This ViewModel is for share data between Fragments and parent Activity
 * call it using the Activity as Provider
 */
class SharedViewModel(application: Application): BaseViewModel<QrData>(application) {

    // Contains the data to the toolbar, should be modified from the fragments
    // Activity should observe and bind the toolbar
    var toolbarHandler = MutableLiveData<ToolbarHandler>()

    var messagesPublished = MutableLiveData<HashMap<String, Message>>().default(HashMap()) // An stack of messages published, to restore it when the activity is recreated
    private var listenForGiftReceived: MessageListener? = null // Listener to subscribe to nearby messages

    /**
     * This function sends a notify using Nearby, of the scan status (Authorization status)
     * @param activity Nearby needs to be called from an activity
     * @param status Status of Scan Authorization
     */
    fun notifyQrStatus(activity: Activity, status: String) {
        clearMessage(activity, SCAN_STATUS)

        val message = Message(status.toByteArray(), SCAN_STATUS)

        Nearby.getMessagesClient(activity).publish(message)
            .addOnFailureListener {
                it.printStackTrace()
            }

        (messagesPublished.value ?: HashMap())[SCAN_STATUS] = message // Adds the Message to te stack

        setStatus(DataState.SUCCESS, status)
    }

    /**
     * Notify that the Qr Status is authorized
     * @param activity Nearby needs to be called from an activity
     */
    fun notifyAuthorizedQR(activity: Activity) {
        notifyQrStatus(activity, QR_SCANNED_AUTHORIZED)
    }

    /**
     * Notify that the Qr Status is declined
     * @param activity Nearby needs to be called from an activity
     */
    fun notifyDeclinedQR(activity: Activity) {
        notifyQrStatus(activity, QR_SCANNED_DECLINED)
    }

    /**
     * Notify using Nearby which gift was selected.
     * @param activity Nearby needs to be called from an activity
     * @param gift Gift selected, send null to clear it
     */
    fun confirmGift(activity: Activity, gift: Gift?) {
        clearMessage(activity, GIFT_SELECTED)

        if (gift != null)
            setStatus(DataState.LOADING, GIFT_SELECTED)
        else
            setStatus(DataState.SUCCESS)

        val message = Message(Gson().toJson(gift).toByteArray(), GIFT_SELECTED) // Converts the gift Object to Json String
        Nearby.getMessagesClient(activity).publish(message)
            .addOnFailureListener { it.printStackTrace() }

        (messagesPublished.value ?: HashMap())[GIFT_SELECTED] = message // Adds the Message to the stack
    }

    /**
     * Clear the gift notify
     */
    fun cancelGift(context: Context) {
        if (context is Activity) {
            confirmGift(context, null)

            setStatus(DataState.SUCCESS)
        }
    }

    /**
     * This function publish all messages in the Stack
     * call it in the activity onStart function
     */
    fun publishMessages(activity: Activity) {
        val messagesClient = Nearby.getMessagesClient(activity)
        messagesPublished.value?.forEach { (_, v) ->
            messagesClient.publish(v).addOnFailureListener {
                it.printStackTrace()
            }
        }

        listenForGiftReceived = object: MessageListener() {
            override fun onFound(p0: Message?) {
                if (p0?.type == GIFT_SELECTED_RECEIVED && status.value?.dataState == DataState.LOADING) {
                    setStatus(DataState.SUCCESS, GIFT_SELECTED_RECEIVED)
                }
            }
        }.also {
            Nearby.getMessagesClient(activity).subscribe(it)
        }
    }

    /**
     * This function unpublish all messages in the Stack
     * call it in the activity onStop function
     */
    fun unpublishMessages(activity: Activity) {
        val messagesClient = Nearby.getMessagesClient(activity)
        messagesPublished.value?.forEach { (_, v) ->
            messagesClient.unpublish(v).addOnFailureListener {
                it.printStackTrace()
            }
        }

        listenForGiftReceived?.let {
            Nearby.getMessagesClient(activity).unsubscribe(it)
            listenForGiftReceived = null
        }
    }

    /**
     * Clear messages from the stack
     */
    private fun clearMessage(activity: Activity, key: String) {
        messagesPublished.value?.let { messages ->
            messages[key]?.let {
                Nearby.getMessagesClient(activity).unpublish(it).addOnFailureListener {
                    it.printStackTrace()
                }
            }
            messages.remove(key)
        }

    }

    companion object {
        const val QR_SCANNED_AUTHORIZED = "qr_authorized"
        const val QR_SCANNED_DECLINED = "qr_declined"

        const val SCAN_STATUS = "scan_status"
        const val GIFT_SELECTED = "gift_selected"

        const val GIFT_SELECTED_RECEIVED = "gift_received"
    }
}