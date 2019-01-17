package com.alfonsosotelo.expressgift.vendor.ui.dashboard

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.databinding.DataBindingUtil
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.alfonsosotelo.expressgift.data.entities.Gift
import com.alfonsosotelo.expressgift.data.utils.observe
import com.alfonsosotelo.expressgift.data.utils.withViewModel
import com.alfonsosotelo.expressgift.vendor.R
import com.alfonsosotelo.expressgift.vendor.databinding.ActivityDashboardBinding
import com.alfonsosotelo.expressgift.vendor.ui.SharedViewModel
import com.google.android.gms.nearby.Nearby
import com.google.android.gms.nearby.messages.BleSignal
import com.google.android.gms.nearby.messages.Distance
import com.google.android.gms.nearby.messages.Message
import com.google.android.gms.nearby.messages.MessageListener
import kotlinx.android.synthetic.main.activity_dashboard.*

/**
 * This Activity is the "Main" where the user will pass almost all the time
 * Use Fragments to show things in this Activity, preferably with Navigation Components (Nav Graphs)
 */
class DashboardActivity : AppCompatActivity() {

    private var currentController: NavController? = null // Instance here the current NavController
    private var giftReceivedPublication: Message? = null

    private val sharedViewModel by lazy { // Instance of SharedViewModel loaded lazily
        withViewModel({ SharedViewModel(application) }) { // Instancing the SharedViewModel with application parameter
            observe(toolbarHandler) {
                binding.toolbarHandler = it
            }
            observe(giftSelected, ::handleGiftSelected)
        }
    }

    private lateinit var binding: ActivityDashboardBinding

    private val messageListener: MessageListener by lazy { // Listen the messages received from the client, loaded lazily
        object: MessageListener() {
            override fun onFound(message: Message?) {
                super.onFound(message)
                Log.d("TAG - Found", message?.content?.let{ String(it) } ?: "None")

                message?.let { sharedViewModel.notifyReceived(it) } // Notify to viewmodel that a message was received
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView<ActivityDashboardBinding>(this, R.layout.activity_dashboard).apply {
            toolbarHandler = this@DashboardActivity.sharedViewModel.toolbarHandler.value
        }

        currentController = findNavController(R.id.navFragments)

        // Set the toolbar with nav controller
        initToolbar()
    }

    override fun onStart() {
        super.onStart()

        listenForNearbyMessages()
    }

    override fun onStop() {
        super.onStop()

        unsuscribeForNearbyMessages()
    }

    // When the user clicks in the back toolbar arrow
    override fun onSupportNavigateUp(): Boolean {
        return currentController?.navigateUp() ?: true
    }

    // When back pressed tell the current NavController to back in his BackStack
    // If is no more fragments in the backstack closes the Activity
    override fun onBackPressed() {
        currentController?.let {
            if (it.popBackStack().not()) {
                finish()
            }
        } ?: run { finish() }
    }

    private fun initToolbar() {
        currentController?.let { toolbar.setupWithNavController(it) }
    }


    /**
     * Method to subscribe listener and messages to Nearby
     * Call it in onStart
     */
    fun listenForNearbyMessages() {
        Nearby.getMessagesClient(this).subscribe(messageListener).addOnFailureListener {
            it.printStackTrace()
        }
    }

    /**
     * Method to unsubscribe listener and messages to Nearby
     * Call it in onStop
     */
    fun unsuscribeForNearbyMessages() {
        Nearby.getMessagesClient(this).unsubscribe(messageListener)
            .addOnFailureListener {
                it.printStackTrace()
            }

        unsubscribeGiftReceived()
    }

    /**
     * Notify to client that the gift selected was received
     */
    fun handleGiftSelected(gift: Gift?) {
        unsubscribeGiftReceived()
        if(gift != null) {
            giftReceivedPublication = Message("gift_selected_received".toByteArray(), "gift_received").also {
                Nearby.getMessagesClient(this).publish(it)
            }
        }
    }

    /**
     * Unsubscribe that the gift selected was received
     */
    fun unsubscribeGiftReceived() {
        giftReceivedPublication?.let {
            Nearby.getMessagesClient(this).unpublish(it)
            giftReceivedPublication = null
        }
    }

}
