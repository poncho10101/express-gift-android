package com.alfonsosotelo.expressgift.vendor.ui.qrgen

import android.os.Bundle
import android.view.View
import com.alfonsosotelo.expressgift.data.base.BaseFragment
import com.alfonsosotelo.expressgift.data.entities.Gift
import com.alfonsosotelo.expressgift.data.utils.observe
import com.alfonsosotelo.expressgift.data.utils.toast
import com.alfonsosotelo.expressgift.data.utils.withViewModel
import com.alfonsosotelo.expressgift.vendor.databinding.FragmentQrGenBinding
import com.alfonsosotelo.expressgift.vendor.R
import com.alfonsosotelo.expressgift.vendor.ui.SharedViewModel
import com.alfonsosotelo.expressgift.vendor.ui.SharedViewModel.Companion.QR_SCANNED_AUTHORIZED
import com.alfonsosotelo.expressgift.vendor.ui.SharedViewModel.Companion.QR_SCANNED_DECLINED

class QrGenFragment: BaseFragment<FragmentQrGenBinding>(R.layout.fragment_qr_gen) {
    private val viewModel by lazy {
        withViewModel({QrGenViewModel(activity!!.application)}) {
        }
    }

    private val sharedViewModel by lazy {
        activity!!.withViewModel({ SharedViewModel(activity!!.application) }) {
            observe(giftSelected, ::handleGiftSelected)
            observe(qrAuthStatus, ::handleQrAuthStatus)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.generateQRCode()
        sharedViewModel
    }

    override fun setBindVariables(bind: FragmentQrGenBinding) {
        bind.viewModel = viewModel
    }

    override fun initToolbar() {

    }

    /**
     * When receive a gift is selected sends the vendor to the GiftSelected Details
     */
    fun handleGiftSelected(gift: Gift?) {
        if (gift != null)
            navigate(QrGenFragmentDirections.actionFromQrGenToGiftSelected())
    }

    /**
     * This method sends the vendor to the GiftSelected details, When client authorize the Qr
     */
    fun handleQrAuthStatus(status: String?) {
        when(status) {
            QR_SCANNED_AUTHORIZED -> {
                context?.toast(R.string.client_authorized_message)
                navigate(QrGenFragmentDirections.actionFromQrGenToGiftSelected())
            }
            QR_SCANNED_DECLINED -> {
                context?.toast(R.string.client_declined_message)
            }
        }
    }
}