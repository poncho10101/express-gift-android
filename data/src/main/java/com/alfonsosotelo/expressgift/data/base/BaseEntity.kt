package com.alfonsosotelo.expressgift.data.base

/**
 * This interface must be Implemented in RealmObjects
 */
interface BaseEntity {

    fun getPrimaryKey(): Any?
}