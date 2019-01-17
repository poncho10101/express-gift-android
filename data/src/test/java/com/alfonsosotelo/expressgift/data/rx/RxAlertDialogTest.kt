package com.alfonsosotelo.expressgift.data.rx

import androidx.appcompat.app.AlertDialog
import com.alfonsosotelo.expressgift.data.BaseTest
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito

class RxAlertDialogTest: BaseTest() {
    @Mock
    lateinit var alertDialog: AlertDialog

    lateinit var rxAlertDialog: RxAlertDialog

    override fun setUp() {
        super.setUp()

        rxAlertDialog = Mockito.spy(RxAlertDialog(alertDialog))
    }

    @Test
    fun checkAlertDialogInstance() {
        assert(rxAlertDialog.alertDialog == alertDialog)
    }

    @Test
    fun isDisposed() {
        Mockito.`when`(alertDialog.isShowing).thenReturn(false)

        assert(rxAlertDialog.isDisposed)

        Mockito.`when`(alertDialog.isShowing).thenReturn(true)

        assert(!rxAlertDialog.isDisposed)
    }
}