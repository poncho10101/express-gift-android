package com.alfonsosotelo.expressgift.vendor.ui.qrgen

import com.alfonsosotelo.expressgift.vendor.BaseTest
import org.junit.Test
import org.mockito.Mockito
import io.reactivex.android.plugins.RxAndroidPlugins
import io.reactivex.schedulers.Schedulers


class QrGenViewModelTest: BaseTest() {

    lateinit var viewModel: QrGenViewModel

    override fun setUp() {
        super.setUp()

        RxAndroidPlugins.setInitMainThreadSchedulerHandler { scheduler -> Schedulers.trampoline()}

        viewModel = Mockito.spy(QrGenViewModel(applicationMock))
    }

    @Test
    fun generateQR() {
        viewModel.generateQRCode()

        assert(viewModel.data.value != null)
        assert(viewModel.data.value!!.id > 0)
        assert(viewModel.data.value?.vendor != null)
        assert(viewModel.data.value?.preferedGift != null)
    }

    @Test
    fun initTimer() {
        viewModel.initTimer()

        assert(viewModel.qrExpireTime.value != null)
        assert(viewModel.qrExpireTime.value!! >= 0)
        assert(viewModel.qrExpireTime.value!! <= 60)
    }
}