package com.example.lab2.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.lab2.domain.Task

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ListScreen(
    viewModel: ListViewModel,
    listId: String,
    listTitle: String,
    listColor: Long,
    onBackClick: () -> Unit,
    onAddClick: () -> Unit,
    onTaskClick: (Task) -> Unit,
    modifier: Modifier = Modifier
) {
    val themeColor = Color(listColor)
    val displayTasks = remember { mutableStateListOf<Task>() }
    var isLoading by remember { mutableStateOf(true) }

    LaunchedEffect(listId) {
        val activeTasks = viewModel.getFilteredTasks(listId)
        displayTasks.clear()
        displayTasks.addAll(activeTasks)
        isLoading = false
    }

    val showFab = listId != "2" && listId != "3" && listId != "5"

    Scaffold(
        modifier = modifier.fillMaxSize(),
        containerColor = MaterialTheme.colorScheme.surface,
        topBar = {
            TopAppBar(
                title = { },
                navigationIcon = {
                    Row(modifier = Modifier.clickable { onBackClick() }.padding(horizontal = 16.dp), verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = null, tint = MaterialTheme.colorScheme.primary, modifier = Modifier.size(20.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("Списки", color = MaterialTheme.colorScheme.primary, fontSize = 17.sp)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = MaterialTheme.colorScheme.surface)
            )
        },
        floatingActionButton = {
            if (showFab) {
                Row(modifier = Modifier.padding(start = 16.dp, bottom = 16.dp).clickable { onAddClick() }, verticalAlignment = Alignment.CenterVertically) {
                    FloatingActionButton(
                        onClick = onAddClick, containerColor = themeColor, contentColor = Color.White,
                        shape = CircleShape, modifier = Modifier.size(40.dp)
                    ) { Icon(Icons.Default.Add, contentDescription = null, modifier = Modifier.size(24.dp)) }
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Напоминание", color = themeColor, fontSize = 18.sp, fontWeight = FontWeight.Medium)
                }
            }
        },
        floatingActionButtonPosition = FabPosition.Start
    ) { paddingValues ->
        Column(modifier = Modifier.fillMaxSize().padding(paddingValues)) {
            Text(
                text = listTitle,
                fontSize = 34.sp,
                fontWeight = FontWeight.Bold,
                color = themeColor,
                modifier = Modifier.padding(start = 16.dp, end = 16.dp, bottom = 16.dp)
            )

            if (isLoading) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator(color = themeColor)
                }
            } else if (displayTasks.isEmpty()) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text("Нет напоминаний", color = MaterialTheme.colorScheme.onSurfaceVariant)
                }
            } else {
                LazyColumn(modifier = Modifier.fillMaxSize(), contentPadding = PaddingValues(bottom = 100.dp)) {
                    itemsIndexed(displayTasks) { index, task ->
                        TaskRow(
                            task = task,
                            isDone = task.isDone,
                            themeColor = themeColor,
                            onTaskClick = { onTaskClick(task) },
                            onCheckboxClick = {
                                if (listId != "5") {
                                    val updatedTask = task.copy(isDone = !task.isDone)
                                    displayTasks[index] = updatedTask
                                    viewModel.toggleTaskDone(updatedTask)
                                }
                            }
                        )
                        HorizontalDivider(modifier = Modifier.padding(start = 52.dp), color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.2f))
                    }
                }
            }
        }
    }
}

@Composable
fun TaskRow(task: Task, isDone: Boolean, themeColor: Color, onTaskClick: () -> Unit, onCheckboxClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onTaskClick() }
            .padding(horizontal = 16.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(22.dp)
                .background(Color.Transparent, shape = CircleShape)
                .clickable { onCheckboxClick() }
                .padding(2.dp)
        ) {
            Surface(
                shape = CircleShape,
                color = if (isDone) themeColor else Color.Transparent,
                border = BorderStroke(1.5.dp, if (isDone) themeColor else MaterialTheme.colorScheme.onSurfaceVariant),
                modifier = Modifier.fillMaxSize()
            ) {
                if (isDone) Icon(Icons.Default.Check, contentDescription = null, tint = Color.White, modifier = Modifier.padding(3.dp))
            }
        }

        Spacer(modifier = Modifier.width(12.dp))

        if (task.priority > 0 && !isDone) {
            val text = when (task.priority) { 1 -> "!"; 2 -> "!!"; 3 -> "!!!"; else -> "" }
            Text(text = text, color = themeColor, fontWeight = FontWeight.Bold, fontSize = 16.sp, modifier = Modifier.padding(end = 6.dp))
        }

        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = task.title,
                fontSize = 16.sp,
                color = if (isDone) MaterialTheme.colorScheme.onSurfaceVariant else MaterialTheme.colorScheme.onSurface
            )
            if (task.notes.isNotEmpty() && !isDone) {
                Text(text = task.notes, fontSize = 14.sp, color = MaterialTheme.colorScheme.onSurfaceVariant, modifier = Modifier.padding(top = 2.dp))
            }
            if ((task.dueDate.isNotEmpty() || task.dueTime.isNotEmpty()) && !isDone) {
                val separator = if (task.dueDate.isNotEmpty() && task.dueTime.isNotEmpty()) ", " else ""
                Text(text = "${task.dueDate}$separator${task.dueTime}", fontSize = 13.sp, color = MaterialTheme.colorScheme.onSurfaceVariant, fontWeight = FontWeight.Medium, modifier = Modifier.padding(top = 4.dp))
            }
        }
    }
}