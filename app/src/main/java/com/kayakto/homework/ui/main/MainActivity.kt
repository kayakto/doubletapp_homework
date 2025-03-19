package com.kayakto.homework.ui.main

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.google.android.material.navigation.NavigationView
import com.kayakto.homework.R
import com.kayakto.homework.databinding.ActivityMainBinding
import com.kayakto.homework.ui.about.AboutFragment
import com.kayakto.homework.util.extensions.setupBackPressWithDrawer

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var drawerLayout: DrawerLayout // контейнер для бокового меню
    private lateinit var navView: NavigationView // навигация в боковом меню

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        drawerLayout = binding.drawerLayout
        navView = binding.navView

        // нажатие на кнопки в боковой панели перекидывают на другие фрагменты
        navView.setNavigationItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.nav_home -> {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.fragment_container, HabitListFragment())
                        .commit()
                }
                R.id.nav_about -> {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.fragment_container, AboutFragment())
                        .commit()
                }
            }
            drawerLayout.closeDrawer(GravityCompat.START)
            true
        }

        setupBackPressWithDrawer(drawerLayout)

        // загрузка фрагмента список привычек
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, HabitListFragment())
                .commit()
        }
    }
}
