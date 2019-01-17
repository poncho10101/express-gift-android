package com.alfonsosotelo.expressgift.data.entities

import com.alfonsosotelo.expressgift.data.BaseTest
import org.junit.Test
import org.mockito.Mockito

class GiftTest: BaseTest() {

    lateinit var gift: Gift

    override fun setUp() {
        super.setUp()

        gift = Mockito.spy(Gift())
    }

    @Test
    fun primaryKey() {
        gift.id = 5

        assert(gift.getPrimaryKey() == 5L)
    }
}