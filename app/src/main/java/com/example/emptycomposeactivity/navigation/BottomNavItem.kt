package com.example.emptycomposeactivity.navigation

import com.example.emptycomposeactivity.R

sealed class BottomNavItem(var route: String, var icon: Int, var title: String) {
    object Main : BottomNavItem("main_screen", R.drawable.tv_icon, "Главный экран")
    object Profile : BottomNavItem("profile_screen", R.drawable.person_icon, "Профиль")
}