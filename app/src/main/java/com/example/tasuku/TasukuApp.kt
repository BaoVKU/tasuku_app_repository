package com.example.tasuku


import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.tasuku.ui.navigation.NavigationGraph


@Composable
fun TasukuApp(
    navController: NavHostController = rememberNavController()
) {
    NavigationGraph(
        navController = navController
    )
}