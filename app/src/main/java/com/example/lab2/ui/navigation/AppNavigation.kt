package com.example.lab2.ui.navigation

import androidx.compose.runtime.*
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.lab2.ui.screens.*

@Composable
fun AppNavigation() {
    val navController = rememberNavController()
    val listViewModel: ListViewModel = viewModel()

    var tempTitle by remember { mutableStateOf("") }
    var tempNotes by remember { mutableStateOf("") }
    var tempDate by remember { mutableStateOf("") }
    var tempTime by remember { mutableStateOf("") }
    var tempPriority by remember { mutableIntStateOf(0) }

    val saveAndGoBack = {
        if (tempTitle.isNotBlank()) {
            listViewModel.addTask(tempTitle, tempNotes, tempPriority, tempDate, tempTime)
            tempTitle = ""
            tempNotes = ""
            tempPriority = 0
            tempDate = ""
            tempTime = ""
            navController.popBackStack("home", inclusive = false)
        }
    }

    NavHost(navController = navController, startDestination = "home") {
        composable("home") {
            HomeScreen(
                viewModel = listViewModel,
                onListClick = { listId, title, color ->
                    navController.navigate("list/$listId?title=$title&color=$color")
                },
                onAddClick = {
                    tempTitle = ""
                    tempNotes = ""
                    tempDate = ""
                    tempTime = ""
                    tempPriority = 0
                    navController.navigate("add")
                }
            )
        }

        composable(
            route = "list/{listId}?title={title}&color={color}",
            arguments = listOf(
                navArgument("listId") { type = NavType.StringType },
                navArgument("title") { type = NavType.StringType; defaultValue = "Список" },
                navArgument("color") { type = NavType.LongType; defaultValue = 0xFFFF9F0AL }
            )
        ) { backStackEntry ->
            val listId = backStackEntry.arguments?.getString("listId") ?: "3"
            val title = backStackEntry.arguments?.getString("title") ?: "Список"
            val color = backStackEntry.arguments?.getLong("color") ?: 0xFFFF9F0AL

            ListScreen(
                viewModel = listViewModel,
                listId = listId,
                listTitle = title,
                listColor = color,
                onBackClick = { navController.popBackStack() },
                onAddClick = {
                    tempTitle = ""
                    tempNotes = ""
                    tempDate = ""
                    tempTime = ""
                    tempPriority = 0
                    navController.navigate("add")
                }
            )
        }

        composable("add") {
            AddEditReminderScreen(
                initialTitle = tempTitle,
                initialNotes = tempNotes,
                initialDate = tempDate,
                initialTime = tempTime,
                onTitleChange = { tempTitle = it },
                onNotesChange = { tempNotes = it },
                onCancelClick = { navController.popBackStack() },
                onSaveClick = { saveAndGoBack() },
                onDetailsClick = { navController.navigate("details") }
            )
        }

        composable("details") {
            DetailScreen(
                title = tempTitle,
                initialDate = tempDate,
                initialTime = tempTime,
                onBackClick = { navController.popBackStack() },
                onDateSelected = { tempDate = it },
                onTimeSelected = { tempTime = it },
                onSaveClick = { priority ->
                    tempPriority = priority
                    saveAndGoBack()
                }
            )
        }
    }
}