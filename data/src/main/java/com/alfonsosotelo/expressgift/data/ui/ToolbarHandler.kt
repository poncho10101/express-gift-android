package com.alfonsosotelo.expressgift.data.ui

import android.view.MenuItem
import androidx.annotation.DrawableRes
import androidx.annotation.MenuRes

/**
 * Class containing data of the toolbar
 * @param title Title to put in the toolbar
 * @param menuRes Menu Resource Id to put in the toolbar
 * @param drawableRes Drawable Resource Id to put in the nav icon in the toolbar
 * @param menuClickListener Listener to menu click in the toolbar
 * @param navigationClickListener Listener to navigation icon click in the toolbar
 * @param clear it first value is true the toolbar will be clean(title, menuRes and menuClick) before put the new data
 *          if the second value is true too, the rest of the toolbar data(drawableRes, navigationClick) will be clean before put the new data
 *          you can put this value like (true to true) or (Pair(true, true)) to clean all the toolbar.
 */
class ToolbarHandler(var title: String? = null,
                     @MenuRes var menuRes: Int? = null,
                     @DrawableRes var drawableRes: Int? = null,
                     var menuClickListener: ((MenuItem) -> Unit)? = null,
                     var navigationClickListener: (() -> Unit)? = null,
                     var clear: Pair<Boolean, Boolean> = false to false
)