package com.example.lab2.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.lab2.data.MockData
import com.example.lab2.domain.Task
import com.example.lab2.domain.TaskList
import androidx.compose.material.icons.filled.Check

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchScreen(
    viewModel: ListViewModel,
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    var query by remember { mutableStateOf("") }
    var results by remember { mutableStateOf<List<Task>>(emptyList()) }

    val userLists by viewModel.taskLists.collectAsState(initial = emptyList())

    val allLists = remember(userLists) {
        val lists = mutableListOf<TaskList>()
        MockData.myLists.firstOrNull { it.id == "6" }?.let { lists.add(it) }
        userLists.forEach { entity ->
            lists.add(TaskList(id = entity.id.toString(), title = entity.title, iconColor = entity.color, taskCount = 0, icon = getIconByName(entity.iconName)))
        }
        lists
    }

    LaunchedEffect(query) {
        if (query.isBlank()) {
            results = emptyList()
        } else {
            results = viewModel.searchTasks(query)
        }
    }

    val groupedResults = results.groupBy { it.listId }

    Scaffold(
        modifier = modifier.fillMaxSize(),
        topBar = {
            TopAppBar(
                title = {
                    TextField(
                        value = query,
                        onValueChange = { query = it },
                        placeholder = { Text("Поиск", color = MaterialTheme.colorScheme.onSurfaceVariant) },
                        colors = TextFieldDefaults.colors(
                            focusedContainerColor = Color.Transparent,
                            unfocusedContainerColor = Color.Transparent,
                            focusedIndicatorColor = Color.Transparent,
                            unfocusedIndicatorColor = Color.Transparent
                        ),
                        modifier = Modifier.fillMaxWidth()
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Назад", tint = MaterialTheme.colorScheme.primary)
                    }
                },
                actions = {
                    if (query.isNotEmpty()) {
                        IconButton(onClick = { query = "" }) {
                            Icon(Icons.Default.Close, contentDescription = "Очистить", tint = MaterialTheme.colorScheme.primary)
                        }
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = MaterialTheme.colorScheme.surface)
            )
        }
    ) { paddingValues ->
        Column(modifier = Modifier.fillMaxSize().padding(paddingValues)) {
            if (query.isBlank()) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text("Введите текст для поиска", color = MaterialTheme.colorScheme.onSurfaceVariant)
                }
            } else if (results.isEmpty()) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text("Ничего не найдено", color = MaterialTheme.colorScheme.onSurfaceVariant)
                }
            } else {
                LazyColumn {
                    groupedResults.forEach { (listId, tasksInList) ->
                        val listInfo = allLists.firstOrNull { it.id == listId }
                        val listColor = Color(listInfo?.iconColor ?: 0xFFFF9F0A)
                        val listTitle = listInfo?.title ?: "Напоминания"

                        item {
                            Text(
                                text = listTitle,
                                color = listColor,
                                fontSize = 22.sp,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.padding(start = 16.dp, top = 16.dp, bottom = 8.dp)
                            )
                        }

                        items(tasksInList) { task ->
                            SearchResultRow(task = task, themeColor = listColor)
                            HorizontalDivider(modifier = Modifier.padding(start = 16.dp), color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.2f))
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun SearchResultRow(task: Task, themeColor: Color) {
    Row(
        modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier.size(22.dp).background(
                if (task.isDone) themeColor else Color.Transparent,
                shape = CircleShape
            ).then(
                if (!task.isDone) Modifier.background(Color.Transparent) else Modifier
            ),
            contentAlignment = Alignment.Center
        ) {
            if (!task.isDone) {
                Surface(
                    shape = CircleShape,
                    color = Color.Transparent,
                    border = androidx.compose.foundation.BorderStroke(1.5.dp, MaterialTheme.colorScheme.onSurfaceVariant),
                    modifier = Modifier.fillMaxSize()
                ) {}
            } else {
                Icon(Icons.Default.Check, contentDescription = null, tint = Color.White, modifier = Modifier.padding(3.dp))
            }
        }

        Spacer(modifier = Modifier.width(12.dp))

        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = task.title,
                fontSize = 16.sp,
                color = if (task.isDone) MaterialTheme.colorScheme.onSurfaceVariant else MaterialTheme.colorScheme.onSurface
            )
            if (task.notes.isNotEmpty()) {
                Text(text = task.notes, fontSize = 14.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
            }
            if ((task.dueDate.isNotEmpty() || task.dueTime.isNotEmpty()) && !task.isDone) {
                val dateStr = task.dueDate
                val timeStr = task.dueTime
                val separator = if (dateStr.isNotEmpty() && timeStr.isNotEmpty()) ", " else ""
                Text(
                    text = "$dateStr$separator$timeStr",
                    fontSize = 13.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.padding(top = 4.dp)
                )
            }
        }
    }
}