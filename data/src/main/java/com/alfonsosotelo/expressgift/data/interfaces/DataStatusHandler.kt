package com.alfonsosotelo.expressgift.data.interfaces

import com.alfonsosotelo.expressgift.data.utils.DataStatus

/**
 * Use this interface to handle the DataStatus from ViewModels
 */
interface DataStatusHandler {
    /**
     * Use this method to receive the DataStatus info from a livedata observer
     */
    fun handleDataStatus(dataStatus: DataStatus?)
}