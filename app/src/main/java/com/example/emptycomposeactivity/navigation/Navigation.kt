package com.example.emptycomposeactivity

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.emptycomposeactivity.navigation.BottomNavItem
import com.example.emptycomposeactivity.navigation.Screens
import com.example.emptycomposeactivity.screens.NavBarScreen
import com.example.emptycomposeactivity.screens.mainScreen.MainScreen
import com.example.emptycomposeactivity.screens.movieScreen.MovieScreen
import com.example.emptycomposeactivity.screens.profileScreen.ProfileScreen
import com.example.emptycomposeactivity.screens.signInScreen.SignInScreen
import com.example.emptycomposeactivity.screens.signUpScreen.SignUpScreen

@Composable
fun Navigation(navController: NavHostController) {

    NavHost(navController = navController, startDestination = Screens.SignInScreen.route) {
        composable(route = Screens.SignInScreen.route) {
            SignInScreen(navController = navController)
        }

        composable(route = Screens.SignUpScreen.route) {
            SignUpScreen(navController = navController)
        }

        composable(route = Screens.MovieScreen.route) {
            MovieScreen()
        }

        composable(route = Screens.NavBarScreen.route) {
            NavBarScreen(
                {
                    navController.navigate(Screens.SignInScreen.route)
                    {
                        popUpTo(Screens.NavBarScreen.route) {
                            saveState = false
                            inclusive = true
                        }
                        restoreState = false
                        launchSingleTop = true
                    }
                },
                { navController.navigate(Screens.MovieScreen.route) })
        }
    }
}

@Composable
fun BottomNavBarNavigation(
    navController: NavHostController,
    logout: () -> Unit,
    movieDescription: () -> Unit
) {
    NavHost(
        navController = navController,
        startDestination = BottomNavItem.Main.route
    ) {

        composable(BottomNavItem.Main.route) {
            MainScreen() { movieDescription() }
        }

        composable(BottomNavItem.Profile.route) {
            ProfileScreen() { logout() }
        }
    }
}
