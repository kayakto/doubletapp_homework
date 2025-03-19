package com.kayakto.homework.util.extensions

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.os.Parcelable
import androidx.activity.OnBackPressedCallback
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.FragmentActivity

inline fun <reified T : Parcelable> Intent.getParcelableExtraCompat(key: String): T? {
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        getParcelableExtra(key, T::class.java)
    } else {
        @Suppress("DEPRECATION") getParcelableExtra(key) as? T
    }
}

inline fun <reified T : Parcelable> Bundle.getParcelableCompat(key: String): T? {
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        getParcelable(key, T::class.java)
    } else {
        @Suppress("DEPRECATION") getParcelable(key) as? T
    }
}

/**
 * Cкрывает боковую панель при onBackPressed
 */
fun FragmentActivity.setupBackPressWithDrawer(drawerLayout: DrawerLayout) {
    onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
        override fun handleOnBackPressed() {
            if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
                drawerLayout.closeDrawer(GravityCompat.START)
            } else {
                isEnabled = false
                onBackPressedDispatcher.onBackPressed()
            }
        }
    })
}