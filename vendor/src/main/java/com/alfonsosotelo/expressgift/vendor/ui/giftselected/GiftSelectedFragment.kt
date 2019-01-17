package com.alfonsosotelo.expressgift.vendor.ui.giftselected

import com.alfonsosotelo.expressgift.data.base.BaseFragment
import com.alfonsosotelo.expressgift.data.utils.observe
import com.alfonsosotelo.expressgift.data.utils.withViewModel
import com.alfonsosotelo.expressgift.vendor.R
import com.alfonsosotelo.expressgift.vendor.databinding.FragmentGiftSelectedBinding
import com.alfonsosotelo.expressgift.vendor.ui.SharedViewModel
import com.alfonsosotelo.expressgift.vendor.ui.SharedViewModel.Companion.QR_SCANNED_AUTHORIZED

class GiftSelectedFragment: BaseFragment<FragmentGiftSelectedBinding>(R.layout.fragment_gift_selected) {

    val sharedViewModel by lazy {
        activity!!.withViewModel({ SharedViewModel(activity!!.application) }) {
            observe(qrAuthStatus, ::handleQrStatus)
        }
    }

    override fun setBindVariables(bind: FragmentGiftSelectedBinding) {
        bind.sharedViewModel = sharedViewModel
    }

    override fun initToolbar() {

    }

    fun handleQrStatus(status: String?) {
        if(status != QR_SCANNED_AUTHORIZED) {
            activity?.onBackPressed()
        }
    }
}