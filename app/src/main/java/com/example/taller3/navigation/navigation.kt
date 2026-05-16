package com.example.taller3.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.taller3.screens.login
import com.example.taller3.screens.register
import com.example.taller3.screens.home
import com.example.taller3.screens.mapTracker
import com.example.taller3.screens.userlist
import com.google.firebase.auth.FirebaseUser


enum class AppScreens{
    authentication,
    register,
    userList,
    tracking,
    home

}

@Composable
fun Navigation(currentUser: FirebaseUser?, trackUserId: String?){
    val navController = rememberNavController()
    val startDestination = when {
        currentUser == null -> AppScreens.authentication.name
        trackUserId != null -> "${AppScreens.tracking.name}/$trackUserId"
        else -> AppScreens.home.name
    }
    NavHost(navController = navController, startDestination = startDestination){
        composable(route = AppScreens.authentication.name){
            login(navController)
        }
        composable(route = AppScreens.register.name){
            register(navController)
        }
        composable(route = AppScreens.home.name){
            home(navController)
        }
        composable(route = AppScreens.userList.name){
            userlist(navController)
        }
        composable(route = "${AppScreens.tracking.name}/{id}") {
            backStackEntry ->
            val userId = backStackEntry.arguments?.getString("id") ?: ""
            mapTracker(navController, userId)
        }
    }
}