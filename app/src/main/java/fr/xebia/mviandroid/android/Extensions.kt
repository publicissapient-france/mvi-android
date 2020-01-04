package fr.xebia.mviandroid.android

import android.view.View

/**
 * Sets the view's visibility to [VISIBLE][android.view.View.VISIBLE] when visible is true.
 * Otherwise, it sets it to [GONE][android.view.View.GONE].
 */
fun View.setVisibility(visible: Boolean) {
    this.visibility = if (visible) View.VISIBLE else View.GONE
}