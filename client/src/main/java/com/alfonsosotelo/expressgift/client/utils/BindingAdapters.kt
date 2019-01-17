package com.alfonsosotelo.expressgift.client.utils

import android.app.Activity
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.RecyclerView
import com.alfonsosotelo.expressgift.client.ui.qrreader.QrReaded
import com.alfonsosotelo.expressgift.data.base.BaseAdapter
import com.google.zxing.BarcodeFormat
import com.google.zxing.ResultPoint
import com.google.zxing.client.android.BeepManager
import com.journeyapps.barcodescanner.BarcodeCallback
import com.journeyapps.barcodescanner.BarcodeResult
import com.journeyapps.barcodescanner.DecoratedBarcodeView
import com.journeyapps.barcodescanner.DefaultDecoderFactory


/**
 * Binds DecoratedBarCodeView
 * @param barcodeFormat format accepted in the scan
 * @param onScannedListener listener to be called when the something is scanned
 */
@BindingAdapter("barcodeFormat", "onScannedListener", requireAll = false)
fun bindBarcodeView(view: DecoratedBarcodeView, barcodeFormat: BarcodeFormat? = null, onScannedListener: QrReaded?) {
    barcodeFormat?.let {
        view.barcodeView.decoderFactory = DefaultDecoderFactory(listOf(it))
    }

    view.decodeContinuous(object: BarcodeCallback {
        override fun barcodeResult(result: BarcodeResult?) {
            (view.context as? Activity)?.let {
                BeepManager(it).playBeepSoundAndVibrate()
            }
            result?.text?.let { onScannedListener?.onQrReaded(it) }
        }

        override fun possibleResultPoints(resultPoints: MutableList<ResultPoint>?) {

        }

    })
}

/**
 * Binds RecyclerView
 * @param adapter Adapter to put in RecyclerView, needs to extends from BaseAdapter
 * @param adapterItems List of items to put in adapter
 * @param dividerItemOrientation If you want a DividerItemDecoration pass the Orientation
 */
@Suppress("UNCHECKED_CAST")
@BindingAdapter("adapter", "adapterItems", "dividerItemOrientation")
fun bindRecyclerView(view: RecyclerView, adapter: BaseAdapter<*, *>?, adapterItems: List<Any>?, dividerItemOrientation: Int?){
    (adapter as? BaseAdapter<Any, *>)?.submitList(adapterItems)
    dividerItemOrientation?.let {
        view.addItemDecoration(DividerItemDecoration(view.context, it))
    }
    if (view.adapter == null)
        view.adapter = adapter
}


