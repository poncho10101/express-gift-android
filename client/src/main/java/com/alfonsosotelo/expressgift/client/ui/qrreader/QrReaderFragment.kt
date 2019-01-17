package com.alfonsosotelo.expressgift.client.ui.qrreader

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import com.alfonsosotelo.expressgift.client.databinding.FragmentQrReaderBinding
import com.alfonsosotelo.expressgift.client.R
import com.alfonsosotelo.expressgift.client.ui.SharedViewModel
import com.alfonsosotelo.expressgift.client.ui.SharedViewModel.Companion.QR_SCANNED_AUTHORIZED
import com.alfonsosotelo.expressgift.data.base.BaseFragment
import com.alfonsosotelo.expressgift.data.entities.QrData
import com.alfonsosotelo.expressgift.data.utils.*
import kotlinx.android.synthetic.main.fragment_qr_reader.*

class QrReaderFragment: BaseFragment<FragmentQrReaderBinding>(R.layout.fragment_qr_reader) {

    private var requestPermissionCount = 2 // This variable is a counter when the user decline a permission n times, sends it to app settings

    private val viewModel by lazy { // ViewModel of this fragment, loaded lazily
        withViewModel({QrReaderViewModel(activity!!.application)}) {
            observe(status, ::handleDataStatus) // Observes changes in viewModel status
            observe(data, ::handleQr) // Observes changes in viewModel data
        }
    }

    private val sharedViewModel by lazy { // ViewModel shared in the Activity, loaded lazily
        activity!!.withViewModel({ SharedViewModel(activity!!.application) }) {
            observe(status, ::handleDataStatus) // Observes changes in the SharedViewModel status
        }
    }

    override fun onStart() {
        super.onStart()
        requestPermissionCount = 2 // restart counter on start
        requestPermissions()
    }

    override fun onResume() {
        super.onResume()
        scanner.resume() // starts the qr scanner
    }

    override fun onPause() {
        super.onPause()
        scanner.pause() // stops the qr scanner
    }

    override fun handleDataStatus(dataStatus: DataStatus?) {
        when(dataStatus?.dataState) {
            DataState.SUCCESS -> {
                if (dataStatus.message == QR_SCANNED_AUTHORIZED) { // If the client authorize the qr, sends him to GiftList
                    sharedViewModel.data.postValue(viewModel.data.value)
                    viewModel.data.postValue(null)
                    viewModel.setStatus(DataState.SUCCESS)
                    navigate(QrReaderFragmentDirections.actionFromQrReaderToGiftList())
                } else { // If the client decline the qr authorization, start the scanner again
                    scanner.resume()
                }
            }
            DataState.LOADING -> { // if scan a qr stops the scanner until client authorize or decline
                scanner.pause()
            }
            DataState.ERROR -> { // shows a toast and starts the scanner again
                dataStatus.message?.let {
                    context?.toast(it)
                }
                scanner.resume()
            }
        }
    }

    override fun setBindVariables(bind: FragmentQrReaderBinding) {
        bind.viewModel = viewModel
    }

    override fun initToolbar() {

    }

    /**
     * If a correct qr is scanned shows an alert to the Client asking if accept or decline the qr.
     */
    fun handleQr(qrData: QrData?) {
        qrData?.let { _qrData ->
            AlertDialog.Builder(context!!)
                .setMessage(getString(R.string.authorize_qr_format, _qrData.id, _qrData.vendor?.name))
                .setTitle(R.string.authorize_title)
                .setCancelable(false)
                .setPositiveButton(R.string.accept) { _, _ ->
                    sharedViewModel.notifyAuthorizedQR(activity!!)
                }
                .setNegativeButton(R.string.decline) { _, _ ->
                    sharedViewModel.notifyDeclinedQR(activity!!)
                }
                .show()
                .addToDisposables(disposables)
        }
    }

    /**
     * Request for permissions
     * (Camera)
     */
    fun requestPermissions() {
        if(ContextCompat.checkSelfPermission(context!!, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            requestPermissionCount--
            if (requestPermissionCount < 0) {
                context!!.toast(getString(R.string.permission_manually), Toast.LENGTH_LONG)
                startActivity(
                    Intent().apply {
                        action = android.provider.Settings.ACTION_APPLICATION_DETAILS_SETTINGS
                        data = Uri.fromParts("package", activity!!.packageName, null)
                    }
                )
            } else {
                if (shouldShowRequestPermissionRationale(Manifest.permission.CAMERA)) {
                    AlertDialog.Builder(context!!)
                        .setTitle(R.string.camera_permission_title)
                        .setMessage(R.string.camera_permission_explanation)
                        .setPositiveButton(R.string.accept) { _, _ ->
                            requestPermissions(
                                arrayOf(Manifest.permission.CAMERA),
                                PERMISSIONS_REQUEST_CAMERA
                            )
                        }
                        .show()
                        .addToDisposables(disposables)
                } else {
                    requestPermissions(
                        arrayOf(Manifest.permission.CAMERA),
                        PERMISSIONS_REQUEST_CAMERA
                    )
                }
            }
        }
    }

    /**
     * Checks if the permission is granted after a request
     */
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        when(requestCode) {
            PERMISSIONS_REQUEST_CAMERA -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    scanner.resume()
                } else {
                    requestPermissions()
                }
            }
        }
    }

    companion object {
        private const val PERMISSIONS_REQUEST_CAMERA = 312
    }
}