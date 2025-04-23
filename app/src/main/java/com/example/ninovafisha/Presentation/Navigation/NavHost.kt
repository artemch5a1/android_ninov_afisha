package com.example.ninovafisha.Presentation.Navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.ninovafisha.Presentation.Screens.CardEventScreen.CardEventScreen
import com.example.ninovafisha.Presentation.Screens.ListEventsScreen.ListEventsScreen
import com.example.ninovafisha.Presentation.Screens.SiginUpScreen.SignUpScreen
import com.example.ninovafisha.Presentation.Screens.SignInScreen.SigninScreen
import com.example.ninovafisha.Presentation.Screens.SplashScreen.SplashScreen
import com.example.ninovafisha.Presentation.Screens.UpdateOrAddScreen.UpdateOrAddScreen

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
            ListEventsScreen(controlNav)
        }
        composable("siginup") {
            SignUpScreen(controlNav)
        }
        composable("eventCard/{eventId}"){
            val eventId = it.arguments?.getString("eventId") ?: ""
            CardEventScreen(controlNav = controlNav, eventId = eventId)
        }
        composable("UpdateOrAdd/{eventId}"){
            val eventId = if (it.arguments?.getString("eventId") != null) {
                it.arguments!!.getString("eventId") // если eventId есть
                UpdateOrAddScreen(controlNav = controlNav, id = it.arguments!!.getString("eventId"))
            } else {
                UpdateOrAddScreen(controlNav = controlNav, id = null)
                null
            }
        }
    }

}