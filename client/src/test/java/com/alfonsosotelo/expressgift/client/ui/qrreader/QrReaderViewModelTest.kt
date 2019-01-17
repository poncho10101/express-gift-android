package com.alfonsosotelo.expressgift.client.ui.qrreader

import com.alfonsosotelo.expressgift.client.BaseTest
import com.alfonsosotelo.expressgift.data.entities.QrData
import com.alfonsosotelo.expressgift.data.utils.DataState
import com.google.gson.Gson
import org.junit.Test
import org.mockito.ArgumentMatchers.anyInt
import org.mockito.Mockito
import java.lang.NullPointerException

class QrReaderViewModelTest: BaseTest() {
    lateinit var viewModel: QrReaderViewModel

    override fun setUp() {
        super.setUp()

        viewModel = Mockito.spy(QrReaderViewModel(applicationMock))
        Mockito.`when`(applicationMock.getString(anyInt())).thenReturn("Mocked String")
    }

    @Test(expected = NullPointerException::class)
    fun onQrReadedMalformedJson() {
        viewModel.onQrReaded("")
    }

    @Test
    fun onQrReadedFailure() {
        viewModel.onQrReaded(Gson().toJson(QrData()))

        assert(viewModel.status.value?.dataState == DataState.ERROR)
    }

    @Test
    fun onQrReadedSuccess() {
        val json = Gson().toJson(QrData().apply { id = 1 })
        viewModel.onQrReaded(json)

        assert(viewModel.status.value?.dataState == DataState.LOADING)
        assert(Gson().toJson(viewModel.data.value) == json)
    }

}