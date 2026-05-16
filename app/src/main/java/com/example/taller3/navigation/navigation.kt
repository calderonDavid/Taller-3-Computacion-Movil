package com.example.taller3.navigation

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.taller3.AuthViewModel
import com.example.taller3.MapViewModel
import com.example.taller3.screens.login
import com.example.taller3.screens.register
import com.example.taller3.screens.home
import com.example.taller3.screens.mapTracker
import com.example.taller3.screens.userlist


enum class AppScreens{
    authentication,
    register,
    userList,
    tracking,
    home

}

@Composable
fun Navigation(){
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = AppScreens.authentication.name){
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
        composable(route = AppScreens.tracking.name){
            mapTracker()
        }
    }
}