package com.example.lab2.ui.navigation

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.lab2.ui.screens.*

@Composable
fun AppNavigation() {
    val navController = rememberNavController()
    val listViewModel: ListViewModel = viewModel()

    NavHost(navController = navController, startDestination = "home") {
        composable("home") {
            HomeScreen(
                onListClick = { navController.navigate("list") },
                onAddClick = { navController.navigate("add") }
            )
        }
        composable("list") {
            ListScreen(
                viewModel = listViewModel,
                onBackClick = { navController.popBackStack() },
                onAddClick = { navController.navigate("add") }
            )
        }
        composable("add") {
            AddEditReminderScreen(
                onCancelClick = { navController.popBackStack() },
                onSaveClick = { title, notes ->
                    listViewModel.addTask(title, notes, priority = 0)
                    navController.popBackStack()
                },
                onDetailsClick = { navController.navigate("details") }
            )
        }
        composable("details") {
            DetailScreen(
                onBackClick = { navController.popBackStack() },
                onSaveClick = { navController.popBackStack("home", inclusive = false) }
            )
        }
    }
}