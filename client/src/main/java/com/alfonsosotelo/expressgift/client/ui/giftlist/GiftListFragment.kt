package com.alfonsosotelo.expressgift.client.ui.giftlist

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.alfonsosotelo.expressgift.client.R
import com.alfonsosotelo.expressgift.client.databinding.FragmentGiftListBinding
import com.alfonsosotelo.expressgift.client.ui.SharedViewModel
import com.alfonsosotelo.expressgift.client.ui.SharedViewModel.Companion.GIFT_SELECTED
import com.alfonsosotelo.expressgift.data.base.BaseAdapter
import com.alfonsosotelo.expressgift.data.base.BaseFragment
import com.alfonsosotelo.expressgift.data.entities.Gift
import com.alfonsosotelo.expressgift.data.utils.*

class GiftListFragment: BaseFragment<FragmentGiftListBinding>(R.layout.fragment_gift_list) {

    private val viewModel by lazy { // ViewModel of this Fragment loaded lazily
        withViewModel({ GiftListViewModel(activity!!.application) }) {

        }
    }

    private val sharedViewModel by lazy { // ViewModel shared in the Activity loaded lazily
        activity!!.withViewModel({ SharedViewModel(activity!!.application) }) {
            observe(status, ::handleDataStatus)
        }
    }

    private val adapter by lazy {
        GiftListAdapter().apply { // Adapter of the gift list, loaded lazily
            getClickObservable().subscribe(::onClickItem).addToDisposables(disposables) // Subscribing to adapter item click
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Loading gift list
        sharedViewModel.data.value?.let {
            viewModel.loadGiftList(it)
            sharedViewModel.confirmGift(activity!!, viewModel.giftSelected.value)
        } ?: run {
            context?.toast(R.string.qr_data_empty_error, Toast.LENGTH_LONG)
            activity?.onBackPressed()
        }
    }

    override fun onDestroy() {
        activity?.let {
            sharedViewModel.confirmGift(it, null) // Setting selected gift in null before destroy the fragment, to notify to the Vendor
        }
        super.onDestroy()
    }

    override fun setBindVariables(bind: FragmentGiftListBinding) {
        //Load variables to the xml binding
        bind.viewModel = viewModel
        bind.adapter = adapter
        bind.sharedViewModel = sharedViewModel
    }

    override fun initToolbar() {

    }

    override fun handleDataStatus(dataStatus: DataStatus?) {
        when(dataStatus?.dataState) {
            DataState.SUCCESS -> {}
            DataState.LOADING -> {}
            DataState.ERROR -> {}

        }
    }

    /**
     * When click an adapter item (if it is not another gift loading), it will be ask to the user for confirmation
     */
    private fun onClickItem(clickListener: BaseAdapter<Gift, GiftListAdapter.ViewHolder>.ClickListener) {
        sharedViewModel.status.value?.let {
            if (it.dataState != DataState.LOADING && it.message != GIFT_SELECTED) {
                confirmGiftDialog(clickListener.entity)
            }
        } ?: let {
            confirmGiftDialog(clickListener.entity)
        }
    }

    /**
     * Confirmation dialog for selecting Gift
     */
    private fun confirmGiftDialog(gift: Gift) {
        AlertDialog.Builder(context!!)
            .setTitle(R.string.select_gift_title)
            .setMessage(getString(R.string.select_gift_format, gift.name))
            .setPositiveButton(R.string.accept) { _, _ ->
                viewModel.giftSelected.postValue(gift)
                sharedViewModel.confirmGift(activity!!, gift)
            }
            .setNegativeButton(R.string.cancel) { _, _ ->

            }
            .show()
            .addToDisposables(disposables)
    }
}