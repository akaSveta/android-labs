package com.example.lab2.ui.navigation

import androidx.compose.runtime.*
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
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
            listViewModel.addTask(
                title = tempTitle,
                notes = tempNotes,
                priority = tempPriority,
                dueDate = tempDate,
                dueTime = tempTime
            )
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
                onListClick = { navController.navigate("list") },
                onAddClick = {
                    tempTitle = ""
                    tempNotes = ""
                    tempDate = ""
                    tempTime = ""
                    navController.navigate("add")
                }
            )
        }

        composable("list") {
            ListScreen(
                viewModel = listViewModel,
                onBackClick = { navController.popBackStack() },
                onAddClick = {
                    tempTitle = ""
                    tempNotes = ""
                    tempDate = ""
                    tempTime = ""
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