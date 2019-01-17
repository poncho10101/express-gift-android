package com.alfonsosotelo.expressgift.data.utils

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.annotation.StringRes
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.*
import com.alfonsosotelo.expressgift.data.rx.RxAlertDialog
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import io.reactivex.Completable
import androidx.lifecycle.Observer as LifeCycleObserver


/**
 * Activity and Context Extensions
 */

/**
 * Start an activity from a fragment
 * @sample this@Fragment.startActivity<Sample>()
 * @param extras if you want to put extras bundle
 */
inline fun <reified T: Activity> Fragment.startActivity(extras: Bundle? = null) {
    startActivity(Intent(context, T::class.java).apply {
        extras?.let{ putExtras(it) }
    })
}

/**
 * Start an activity from a context
 * @sample this@Activity.startActivity<Sample>()
 * @param extras if you want to put extras bundle
 */
inline fun <reified T: Activity> Context.startActivity(extras: Bundle? = null) {
    startActivity(Intent(this, T::class.java).apply {
        extras?.let{ putExtras(it) }
    })
}

fun Context.toast(message: CharSequence, duration: Int = Toast.LENGTH_SHORT)
        = Toast.makeText(this, message, duration).apply { show() }

fun Context.toast(@StringRes stringRes: Int, duration: Int = Toast.LENGTH_SHORT)
        = Toast.makeText(this, stringRes, duration).apply { show() }



/**
 * Views Extensions
 */

/**
 * Add a snackbar to a View
 *
 * @param actionListener lambda to receive actionText click
 */
fun View.setSnackbar(message: CharSequence, @StringRes actionText: Int? = null, duration: Int = Snackbar.LENGTH_INDEFINITE, actionListener: ((View) -> Unit)? = null): Snackbar {
    return Snackbar.make(
        this,
        message,
        duration.takeIf { actionText != null || duration != Snackbar.LENGTH_INDEFINITE } ?: Snackbar.LENGTH_LONG
    ).apply {
        actionText?.let { setAction(it, actionListener) }

        show()
    }
}

/**
 * Add an error message to the parent (TextInputLayout) if exists else set error to itself
 */
fun TextInputEditText.setLayoutError(message: CharSequence?){
    if (parent?.parent is TextInputLayout) {
        (parent.parent as TextInputLayout).error = message
    } else {
        error = message
    }
}

/**
 * Add an alert dialog to disposables using RxAlertDialog class
 */
fun AlertDialog.addToDisposables(disposables: CompositeDisposable) {
    disposables.add(RxAlertDialog(this))
}




/**
 * RX Extensions
 */

/**
 * Add disposable instance to a CompositeDisposable
 */
fun Disposable.addToDisposables(compositeDisposables: CompositeDisposable) = compositeDisposables.add(this)

/**
 * Schedulers to a reactiveX Single instance
 */
fun <T> Single<T>.setDefaultSchedulers(): Single<T> {
    return subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
}

/**
 * Schedulers to a reactiveX Completable instance
 */
fun Completable.setDefaultSchedulers(): Completable {
    return subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
}

/**
 * Schedulers to a reactiveX Observable instance
 */
fun <T> Observable<T>.setDefaultSchedulers(): Observable<T> {
    return subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
}





/**
 * ViewModels Extensions
 */

/**
 * Add default value to a MutableLiveData
 * @sample MutableLiveData<Int>().default(1)
 */
fun <T> MutableLiveData<T>.default(initialValue: T) = apply { setValue(initialValue) }

/**
 * Get viewmodel without constructor parameterse from an Activity
 * @sample this@Activity.withViewModel<ActivityViewModel>()
 */
inline fun <reified T: ViewModel> FragmentActivity.getViewModel()
        = ViewModelProviders.of(this)[T::class.java]

/**
 * Get viewmodel without constructor parameterse from an Activity
 * with a body
 * @param body lambda with ViewModel Instance, do something with it
 * @sample this@Activity.withViewModel<ActivityViewModel> { it: ActivityViewModel ->
 *       it.doSomething()
 * }
 */
inline fun <reified T : ViewModel> FragmentActivity.withViewModel(body: T.() -> Unit): T {
    val vm = getViewModel<T>()
    vm.body()
    return vm
}

/**
 * Get viewmodel with constructor parameterse from an Activity
 * @sample this@Activity.withViewModel({ActivityViewModel(param)})
 */
inline fun <reified T : ViewModel> FragmentActivity.getViewModel(crossinline factory: () -> T): T {

    @Suppress("UNCHECKED_CAST")
    val vmFactory = object : ViewModelProvider.Factory {
        override fun <U : ViewModel> create(modelClass: Class<U>): U = factory() as U
    }

    return ViewModelProviders.of(this, vmFactory)[T::class.java]
}

/**
 * Get viewmodel with constructor parameterse from an Activity
 * with a body
 * @param body lambda with ViewModel Instance, do something with it
 * @sample this@Activity.withViewModel({ActivityViewModel(param)}) { it: ActivityViewModel ->
 *       it.doSomething()
 * }
 */
inline fun <reified T : ViewModel> FragmentActivity.withViewModel(
    crossinline factory: () -> T,
    body: T.() -> Unit
): T {
    val vm = getViewModel(factory)
    vm.body()
    return vm
}

/**
 * Get viewmodel without constructor parameterse from a Fragment
 * @sample this@Activity.withViewModel<ActivityViewModel>()
 */
inline fun <reified T: ViewModel> Fragment.getViewModel()
        = ViewModelProviders.of(this)[T::class.java]

/**
 * Get viewmodel without constructor parameterse from an Activity
 * with a body
 * @param body lambda with ViewModel Instance, do something with it
 * @sample this@Activity.withViewModel<ActivityViewModel> { it: ActivityViewModel ->
 *       it.doSomething()
 * }
 */
inline fun <reified T : ViewModel> Fragment.withViewModel(body: T.() -> Unit): T {
    val vm = getViewModel<T>()
    vm.body()
    return vm
}

/**
 * Get viewmodel with constructor parameterse from a Fragment
 * @sample this@Activity.withViewModel({ActivityViewModel(param)})
 */
inline fun <reified T : ViewModel> Fragment.getViewModel(crossinline factory: () -> T): T {

    @Suppress("UNCHECKED_CAST")
    val vmFactory = object : ViewModelProvider.Factory {
        override fun <U : ViewModel> create(modelClass: Class<U>): U = factory() as U
    }

    return ViewModelProviders.of(this, vmFactory)[T::class.java]
}

/**
 * Get viewmodel with constructor parameterse from a Fragment
 * with a body
 * @param body lambda with ViewModel Instance, do something with it
 * @sample this@Activity.withViewModel({ActivityViewModel(param)}) { it: ActivityViewModel ->
 *       it.doSomething()
 * }
 */
inline fun <reified T : ViewModel> Fragment.withViewModel(
    crossinline factory: () -> T,
    body: T.() -> Unit
): T {
    val vm = getViewModel(factory)
    vm.body()
    return vm
}

/**
 * Observe a LiveData from a LifeCycleOwner (Activity, Fragment)
 * @param liveData the live data to observe
 * @param body lambda called when the livedata changes
 * @sample this@Activity.observe(viewModel.data) { data: LiveData ->
 *      doSomething(data)
 * }
 */
fun <T: Any, L: LiveData<T>> LifecycleOwner.observe(liveData: L, body: (T?) -> Unit)
        = liveData.observe(this, LifeCycleObserver(body))

/**
 * Add one element to a list in MutabeLiveData
 */
fun <T> MutableLiveData<List<T>>.addElement(element: T) {
    addElements(listOf(element))
}

/**
 * Add a list of elements to a list in MutabeLiveData
 */
fun <T> MutableLiveData<List<T>>.addElements(element: List<T>) {
    this.postValue(
        (this.value?.toMutableList() ?: mutableListOf()).apply {
            addAll(element)
        }
    )
}