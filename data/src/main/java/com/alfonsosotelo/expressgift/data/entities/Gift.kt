package com.alfonsosotelo.expressgift.data.entities

import com.alfonsosotelo.expressgift.data.base.BaseEntity

open class Gift: BaseEntity {

    open var id: Long = 0
    open var name: String = ""

    override fun getPrimaryKey(): Any? {
        return id
    }

    companion object {
        fun getGifts(): List<Gift> {
            return getDummyGifts()
        }

        fun getGiftsFromVendor(vendor: Vendor): List<Gift> {
            return getDummyGifts()
        }

        private fun getDummyGifts(): List<Gift> {
            return (1..15).map {
                Gift().apply {
                    id = it.toLong()
                    name = "Gift #$it"
                }
            }
        }
    }
}