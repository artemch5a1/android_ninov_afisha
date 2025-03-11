package com.example.ninovafisha.Presentation.Navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.ninovafisha.Presentation.Screens.MainScreen.MainScreen
import com.example.ninovafisha.Presentation.Screens.SiginUpScreen.SignUpScreen
import com.example.ninovafisha.Presentation.Screens.SignInScreen.SigninScreen
import com.example.ninovafisha.Presentation.Screens.SplashScreen.SplashScreen

@Composable
fun NavHost() {
    val controlNav = rememberNavController()
    NavHost(navController =  controlNav, startDestination = "splash") {

        composable("splash")
        {
            SplashScreen(controlNav)
        }
        composable("sigin")
        {
            SigninScreen(controlNav)
        }
        composable("main") {
            MainScreen(controlNav)
        }
        composable("siginup") {
            SignUpScreen(controlNav)
        }
    }

}