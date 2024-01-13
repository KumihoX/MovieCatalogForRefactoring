package com.example.emptycomposeactivity.navigation

sealed class Screens(val route: String) {
    object SignInScreen : Screens("sign_in_screen")
    object SignUpScreen : Screens("sign_up_screen")
    object MovieScreen : Screens("movie_screen")
    object NavBarScreen : Screens("nav_screen")
}
