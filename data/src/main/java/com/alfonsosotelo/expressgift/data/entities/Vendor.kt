package com.alfonsosotelo.expressgift.data.entities

open class Vendor {
    open var id: Long = 0
    open var name: String = ""

    fun getPreferedGift(): Gift {
        return getDummyPreferedGift()
    }

    private fun getDummyPreferedGift(): Gift {
        return Gift.getGifts()[5]
    }

    companion object {
        fun getSelf(): Vendor {
            return getDummyVendor()
        }

        private fun getDummyVendor(): Vendor {
            return Vendor().apply {
                id = 1
                name = "Dummy Vendor"
            }
        }
    }

}