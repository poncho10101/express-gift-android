package com.alfonsosotelo.expressgift.client.ui.giftlist

import com.alfonsosotelo.expressgift.client.BaseTest
import com.alfonsosotelo.expressgift.data.entities.QrData
import com.alfonsosotelo.expressgift.data.entities.Vendor
import com.alfonsosotelo.expressgift.data.utils.DataState
import org.junit.Test
import org.mockito.Mockito


class GiftListViewModelTest: BaseTest() {
    lateinit var viewModel: GiftListViewModel

    override fun setUp() {
        super.setUp()

        viewModel = Mockito.spy(GiftListViewModel(applicationMock))
    }

    @Test
    fun loadGiftListVendorNull() {
        val qrData = Mockito.mock(QrData::class.java)

        Mockito.`when`(qrData.vendor).thenReturn(null)

        viewModel.loadGiftList(qrData)

        assert(viewModel.status.value?.dataState == DataState.ERROR)

    }

    @Test
    fun loadGiftList() {
        val qrData = Mockito.mock(QrData::class.java)
        val vendor = Mockito.mock(Vendor::class.java)

        Mockito.`when`(qrData.vendor).thenReturn(vendor)

        viewModel.loadGiftList(qrData)

        assert(viewModel.data.value?.isEmpty() == false)

    }
}