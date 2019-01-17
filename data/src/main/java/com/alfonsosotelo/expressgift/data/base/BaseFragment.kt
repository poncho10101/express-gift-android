package com.alfonsosotelo.expressgift.data.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.NavDirections
import androidx.navigation.fragment.NavHostFragment
import com.alfonsosotelo.expressgift.data.interfaces.DataStatusHandler
import com.alfonsosotelo.expressgift.data.interfaces.ToolbarInitializer
import com.alfonsosotelo.expressgift.data.utils.DataStatus
import io.reactivex.disposables.CompositeDisposable

/**
 * This class is the Base for Fragments implementing Data Binding
 */
abstract class BaseFragment<BIND: ViewDataBinding>(@LayoutRes protected val layoutRes: Int): Fragment(), ToolbarInitializer, DataStatusHandler {
    protected var disposables = CompositeDisposable() // if you subscribe to an observable the disposable needs to be added here
    protected var binding: BIND? = null // Instance of binding xml class

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = bindView(inflater, container)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initToolbar()
    }

    protected open fun bindView(inflater: LayoutInflater, container: ViewGroup?): BIND {
        return DataBindingUtil.inflate<BIND>(inflater, layoutRes, container, false).apply {
            setLifecycleOwner(this@BaseFragment)
            setBindVariables(this)
        }
    }

    override fun onDestroy() {
        disposables.clear()
        super.onDestroy()
    }

    /**
     * Handle the data status changes here, needs to observe the viewModel status and callit
     * @sample observe(viewModel.status, ::handleDataStatus)
     */
    override fun handleDataStatus(dataStatus: DataStatus?) {}


    /**
     * In this method you should link the object with the variables in the xml
     */
    abstract fun setBindVariables(bind: BIND)

    /**
     * This method disallow Fragment.findNavController()
     */
    fun findNavController(): NavController {
        throw RuntimeException("Avoid use findNavController().navigate(), use navigate() instead")
    }

    /**
     * To get navController without use Fragment.findNavController()
     */
    fun myFindNavController(): NavController {
        return NavHostFragment.findNavController(this)
    }

    /**
     * Use this method instead findNavController().navigate() because exists a bug in navigation alpha07
     * This method avoid app crash when changing fast between navigations
     */
    fun navigate(navDirections: NavDirections) {
        try {
            myFindNavController().navigate(navDirections)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}