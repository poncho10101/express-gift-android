package com.alfonsosotelo.expressgift.vendor.utils

import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.google.gson.Gson
import com.google.zxing.BarcodeFormat
import com.journeyapps.barcodescanner.BarcodeEncoder

/**
 * Binds ImageView
 * @param qrCode set qr code to ImageView
 */
@BindingAdapter("qrCode")
fun bindImageView(view: ImageView, qrCode: Any?) {
    qrCode?.let {
        view.setImageBitmap(
            BarcodeEncoder().encodeBitmap(Gson().toJson(it), BarcodeFormat.QR_CODE, 400, 400)
        )
    } ?: run {
        view.setImageResource(0)
    }
}