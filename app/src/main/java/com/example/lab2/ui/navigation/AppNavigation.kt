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

    var tempListId by remember { mutableStateOf("6") }
    var tempListTitle by remember { mutableStateOf("Напоминания") }
    var tempListColor by remember { mutableLongStateOf(0xFFFF9F0A) }
    var tempListIcon by remember { mutableStateOf("Menu") }

    val saveAndGoBack = {
        if (tempTitle.isNotBlank()) {
            listViewModel.addTask(tempTitle, tempNotes, tempPriority, tempDate, tempTime, tempListId)

            tempTitle = ""
            tempNotes = ""
            tempPriority = 0
            tempDate = ""
            tempTime = ""

            tempListId = "6"
            tempListTitle = "Напоминания"
            tempListColor = 0xFFFF9F0A
            tempListIcon = "Menu"

            navController.popBackStack("home", inclusive = false)
        }
    }

    NavHost(navController = navController, startDestination = "home") {
        composable("home") {
            HomeScreen(
                viewModel = listViewModel,
                onListClick = { listId, title, color -> navController.navigate("list/$listId?title=$title&color=$color") },
                onAddClick = {
                    tempListId = "6"; tempListTitle = "Напоминания"; tempListColor = 0xFFFF9F0A; tempListIcon = "Menu"
                    navController.navigate("add")
                },
                onAddListClick = { navController.navigate("add_list") }
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
            val listId = backStackEntry.arguments?.getString("listId") ?: ""
            val title = backStackEntry.arguments?.getString("title") ?: "Список"
            val color = backStackEntry.arguments?.getLong("color") ?: 0xFFFF9F0AL

            ListScreen(
                viewModel = listViewModel,
                listId = listId,
                listTitle = title,
                listColor = color,
                onBackClick = { navController.popBackStack() },
                onAddClick = {
                    tempListId = listId; tempListTitle = title; tempListColor = color; tempListIcon = "Menu"
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
                selectedListTitle = tempListTitle,
                selectedListColor = tempListColor,
                selectedListIcon = tempListIcon,
                onTitleChange = { tempTitle = it },
                onNotesChange = { tempNotes = it },
                onCancelClick = { navController.popBackStack() },
                onSaveClick = { saveAndGoBack() },
                onDetailsClick = { navController.navigate("details") },
                onListClick = { navController.navigate("select_list") }
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

        composable("add_list") {
            AddListScreen(
                onCancelClick = { navController.popBackStack() },
                onSaveClick = { title, color, iconName ->
                    listViewModel.addTaskList(title, color, iconName)
                    navController.popBackStack()
                }
            )
        }

        composable("select_list") {
            SelectListScreen(
                viewModel = listViewModel,
                currentListId = tempListId,
                onBackClick = { navController.popBackStack() },
                onListSelected = { id, title, color, iconName ->
                    tempListId = id; tempListTitle = title; tempListColor = color; tempListIcon = iconName
                    navController.popBackStack()
                }
            )
        }
    }
}