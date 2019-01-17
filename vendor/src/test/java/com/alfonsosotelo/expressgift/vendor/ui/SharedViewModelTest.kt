package com.alfonsosotelo.expressgift.vendor.ui

import com.alfonsosotelo.expressgift.data.entities.Gift
import com.alfonsosotelo.expressgift.vendor.BaseTest
import com.alfonsosotelo.expressgift.vendor.ui.SharedViewModel.Companion.GIFT_SELECTED
import com.alfonsosotelo.expressgift.vendor.ui.SharedViewModel.Companion.QR_SCANNED_AUTHORIZED
import com.alfonsosotelo.expressgift.vendor.ui.SharedViewModel.Companion.QR_SCANNED_DECLINED
import com.alfonsosotelo.expressgift.vendor.ui.SharedViewModel.Companion.SCAN_STATUS
import com.google.android.gms.nearby.messages.Message
import com.google.gson.Gson
import org.junit.Test
import org.mockito.Mockito

class SharedViewModelTest: BaseTest() {

    lateinit var viewModel: SharedViewModel

    override fun setUp() {
        super.setUp()

        viewModel = Mockito.spy(SharedViewModel(applicationMock))
    }

    @Test
    fun notifyReceivedQrAuth() {
        val message = Mockito.mock(Message::class.java)

        Mockito.`when`(message.type).thenReturn(SCAN_STATUS)
        Mockito.`when`(message.content).thenReturn(QR_SCANNED_AUTHORIZED.toByteArray())

        viewModel.notifyReceived(message)

        assert(viewModel.qrAuthStatus.value == QR_SCANNED_AUTHORIZED)

        Mockito.`when`(message.content).thenReturn(QR_SCANNED_DECLINED.toByteArray())

        viewModel.notifyReceived(message)

        assert(viewModel.qrAuthStatus.value == QR_SCANNED_DECLINED)
    }

    @Test
    fun notifyReceivedGiftSelected() {
        val message = Mockito.mock(Message::class.java)

        val gift = Mockito.spy(Gift())
        gift.id = 12
        gift.name = "Gift #12"

        var giftJson = Gson().toJson(gift)

        Mockito.`when`(message.type).thenReturn(GIFT_SELECTED)
        Mockito.`when`(message.content).thenReturn(giftJson.toByteArray())

        viewModel.notifyReceived(message)

        assert(viewModel.giftSelected.value != null)
        assert(gift.id == viewModel.giftSelected.value?.id)
        assert(Gson().toJson(viewModel.giftSelected.value) == giftJson)

        gift.id = 0
        giftJson = Gson().toJson(gift)

        Mockito.`when`(message.content).thenReturn(giftJson.toByteArray())

        viewModel.notifyReceived(message)

        assert(viewModel.giftSelected.value == null)
    }
}