package com.example.taller3.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.taller3.screens.login
import com.example.taller3.screens.register

enum class AppScreens{
    authentication,
    register,
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
            //AppScreens.home()
        }
    }
}