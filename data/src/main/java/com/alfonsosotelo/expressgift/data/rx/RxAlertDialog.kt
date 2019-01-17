package com.alfonsosotelo.expressgift.data.rx

import androidx.appcompat.app.AlertDialog
import io.reactivex.disposables.Disposable

/**
 * Use this method to add AlertDialogs to disposables
 * @param alertDialog AlertDialog to dissmis when dispose is called
 */
class RxAlertDialog(var alertDialog: AlertDialog): Disposable {
    override fun isDisposed(): Boolean {
        return !alertDialog.isShowing
    }

    override fun dispose() {
        alertDialog.dismiss()
    }
}