package com.alfonsosotelo.expressgift.data.utils

import androidx.appcompat.widget.Toolbar
import androidx.databinding.BindingAdapter
import com.alfonsosotelo.expressgift.data.ui.ToolbarHandler
import com.google.android.material.textfield.TextInputEditText


/**
 * Binds TextInputLayout
 * @param errorText set this error to InputLayout
 */
@BindingAdapter("error")
fun bindTextInputLayout(view: TextInputEditText, errorText: String?) {
    view.setLayoutError(errorText)
}

/**
 * Binds Toolbar
 * @param toolbarHandler Toolbar Data to put in the Toolbar View
 */
@BindingAdapter("toolbarHandler")
fun bindToolbar(view: Toolbar, toolbarHandler: ToolbarHandler?) {
    toolbarHandler?.clear?.takeIf { it.first }?.let {
        view.title = ""
        view.menu?.clear()
        view.setOnMenuItemClickListener(null)
        if (it.second) {
            view.navigationIcon = null
            view.setNavigationOnClickListener(null)
        }
    }

    toolbarHandler?.title?.let { view.title = it }
    toolbarHandler?.menuRes?.let { view.inflateMenu(it) }
    toolbarHandler?.drawableRes?.let { view.setNavigationIcon(it) }
    toolbarHandler?.menuClickListener?.let { listener ->
        view.setOnMenuItemClickListener{
            listener.invoke(it)
            true
        }
    }
    toolbarHandler?.navigationClickListener?.let { listener ->
        view.setNavigationOnClickListener {
            listener.invoke()
        }
    }
}
