package com.alfonsosotelo.expressgift.client.ui.giftlist

import android.view.View
import com.alfonsosotelo.expressgift.client.R
import com.alfonsosotelo.expressgift.data.base.BaseAdapter
import com.alfonsosotelo.expressgift.data.entities.Gift
import kotlinx.android.synthetic.main.row_gift.view.*

class GiftListAdapter: BaseAdapter<Gift, GiftListAdapter.ViewHolder>(R.layout.row_gift, DefaultDiffCallback()) {
    override fun onBindVH(holder: ViewHolder, position: Int) {

    }

    override fun instanceViewHolder(view: View): ViewHolder {
        return ViewHolder(view)
    }

    inner class ViewHolder(view: View): BaseViewHolder(view) {
        override fun bind(element: Gift) {
            //Binding view with data
            itemView.apply {
                tvName.text = element.name
            }
        }
    }
}