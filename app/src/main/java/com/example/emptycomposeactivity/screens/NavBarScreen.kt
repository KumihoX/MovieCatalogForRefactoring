package com.example.emptycomposeactivity.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.emptycomposeactivity.BottomNavBarNavigation
import com.example.emptycomposeactivity.navigation.BottomNavItem
import com.example.emptycomposeactivity.screens.mainScreen.MainViewModel
import com.example.emptycomposeactivity.ui.theme.BlackForNavBar
import com.example.emptycomposeactivity.ui.theme.DarkRed
import com.example.emptycomposeactivity.ui.theme.Black
import com.example.emptycomposeactivity.ui.theme.Gray

@Composable
fun NavBarScreen(logout: () -> Unit, movieDescription: () -> Unit) {

    val navController = rememberNavController()

    Scaffold(
        topBar = {},
        bottomBar = { BottomNavBar(navController) },
        content = { padding ->
            Box(modifier = Modifier.padding(padding)) {
                BottomNavBarNavigation(
                    navController = navController,
                    { logout() },
                    { movieDescription() })
            }
        },
        backgroundColor = Black
    )
}

@Composable
fun BottomNavBar(navController: NavController) {

    val items = listOf(
        BottomNavItem.Main,
        BottomNavItem.Profile
    )

    BottomNavigation(
        backgroundColor = BlackForNavBar,
        contentColor = Gray
    ) {
        val navBackStackEntry by navController.currentBackStackEntryAsState()
        val currentRoute = navBackStackEntry?.destination?.route
        items.forEach { item ->
            BottomNavigationItem(
                icon = { Icon(painterResource(item.icon), contentDescription = item.title) },
                label = { Text(text = item.title) },
                selectedContentColor = DarkRed,
                unselectedContentColor = Gray,
                alwaysShowLabel = true,
                selected = currentRoute == item.route,
                onClick = {
                    navController.navigate(item.route) {
                        navController.graph.startDestinationRoute?.let { route ->
                            popUpTo(route) {
                                saveState = true
                            }
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                }
            )
        }
    }
}