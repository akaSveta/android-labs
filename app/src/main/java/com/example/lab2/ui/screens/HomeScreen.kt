package com.example.lab2.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.lab2.data.MockData
import com.example.lab2.domain.Task
import com.example.lab2.domain.TaskList
import com.example.lab2.ui.components.MyListRow
import com.example.lab2.ui.components.SmallListCard
import java.util.Calendar
import java.util.Locale

fun getIconByName(name: String): ImageVector {
    return when (name) {
        "Menu" -> Icons.Default.Menu
        "List" -> Icons.Default.List
        "Star" -> Icons.Default.Star
        "Favorite" -> Icons.Default.Favorite
        "Home" -> Icons.Default.Home
        "Person" -> Icons.Default.Person
        "ShoppingCart" -> Icons.Default.ShoppingCart
        "DateRange" -> Icons.Default.DateRange
        "Email" -> Icons.Default.Email
        "Phone" -> Icons.Default.Phone
        "Settings" -> Icons.Default.Settings
        else -> Icons.Default.Menu
    }
}

@Composable
fun HomeScreen(
    viewModel: ListViewModel,
    onListClick: (String, String, Long) -> Unit,
    onAddClick: () -> Unit,
    onAddListClick: () -> Unit,
    onSearchClick: () -> Unit,
    onDeleteList: (Long) -> Unit,
    modifier: Modifier = Modifier
) {
    var allTasks by remember { mutableStateOf<List<Task>>(emptyList()) }
    var isLoaded by remember { mutableStateOf(false) }

    var listToDelete by remember { mutableStateOf<Long?>(null) }

    val userLists by viewModel.taskLists.collectAsState(initial = emptyList())

    LaunchedEffect(Unit) {
        allTasks = viewModel.getAllTasksForCount()
        isLoaded = true
    }

    fun getCount(listId: String): Int {
        if (!isLoaded) return 0

        val todayCalendar = Calendar.getInstance()
        val todayStr = String.format(Locale.getDefault(), "%02d.%02d.%d", todayCalendar.get(Calendar.DAY_OF_MONTH), todayCalendar.get(Calendar.MONTH) + 1, todayCalendar.get(Calendar.YEAR))
        val todayStrNoZero = "${todayCalendar.get(Calendar.DAY_OF_MONTH)}.${todayCalendar.get(Calendar.MONTH) + 1}.${todayCalendar.get(Calendar.YEAR)}"

        return when (listId) {
            "1" -> allTasks.count { !it.isDone && (it.dueDate == todayStr || it.dueDate == todayStrNoZero) }
            "2" -> allTasks.count { !it.isDone && it.dueDate.isNotEmpty() }
            "3" -> allTasks.count { !it.isDone }
            "4" -> allTasks.count { !it.isDone && it.isFlagged }
            "5" -> allTasks.count { it.isDone }
            "6" -> allTasks.count { !it.isDone && it.listId == "6" }
            else -> allTasks.count { !it.isDone && it.listId == listId }
        }
    }

    Scaffold(
        modifier = modifier.fillMaxSize(),
        containerColor = MaterialTheme.colorScheme.surface,
        floatingActionButton = {
            Row(
                modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(modifier = Modifier.clickable { onAddClick() }, verticalAlignment = Alignment.CenterVertically) {
                    FloatingActionButton(
                        onClick = onAddClick, containerColor = MaterialTheme.colorScheme.primary,
                        contentColor = Color.White, shape = CircleShape, modifier = Modifier.size(40.dp)
                    ) {
                        Icon(Icons.Default.Add, contentDescription = null, modifier = Modifier.size(24.dp))
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Напоминание", color = MaterialTheme.colorScheme.primary, fontSize = 18.sp, fontWeight = FontWeight.Medium)
                }
                Text(
                    "Добавить", color = MaterialTheme.colorScheme.primary, fontSize = 18.sp, fontWeight = FontWeight.Medium,
                    modifier = Modifier.clickable { onAddListClick() }.padding(8.dp)
                )
            }
        },
        floatingActionButtonPosition = FabPosition.Center
    ) { paddingValues ->
        Column(
            modifier = Modifier.fillMaxSize().padding(paddingValues).padding(horizontal = 16.dp).verticalScroll(rememberScrollState())
        ) {
            Row(modifier = Modifier.fillMaxWidth().padding(top = 8.dp, bottom = 8.dp), horizontalArrangement = Arrangement.End) {
                IconButton(onClick = { /* TODO: Настройки */ }) {
                    Icon(Icons.Default.Settings, contentDescription = null, tint = MaterialTheme.colorScheme.primary, modifier = Modifier.size(28.dp))
                }
            }

            Card(
                onClick = onSearchClick,
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
                modifier = Modifier.fillMaxWidth().height(48.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxSize().padding(horizontal = 16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(Icons.Default.Search, contentDescription = null, tint = MaterialTheme.colorScheme.onSurfaceVariant, modifier = Modifier.size(20.dp))
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Поиск", color = MaterialTheme.colorScheme.onSurfaceVariant, fontSize = 14.sp)
                }
            }

            Spacer(Modifier.height(24.dp))

            MockData.baseLists.chunked(2).forEach { rowItems ->
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    rowItems.forEach { list ->
                        Box(modifier = Modifier.weight(1f)) {
                            val realCount = getCount(list.id)
                            val updatedList = list.copy(taskCount = realCount)

                            SmallListCard(list = updatedList, onClick = { onListClick(list.id, list.title, list.iconColor) })
                        }
                    }
                    if (rowItems.size == 1) Spacer(modifier = Modifier.weight(1f))
                }
                Spacer(Modifier.height(12.dp))
            }

            Spacer(Modifier.height(24.dp))
            Text("Мои списки", fontSize = 22.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onSurface)
            Spacer(Modifier.height(12.dp))

            val baseMyList = MockData.myLists.firstOrNull { it.id == "6" }
            if (baseMyList != null) {
                val realCount = getCount("6")
                val updatedList = baseMyList.copy(taskCount = realCount)
                MyListRow(list = updatedList, onClick = { onListClick(updatedList.id, updatedList.title, updatedList.iconColor) })
                Spacer(Modifier.height(8.dp))
            }

            userLists.forEach { listEntity ->
                val list = TaskList(
                    id = listEntity.id.toString(),
                    title = listEntity.title,
                    iconColor = listEntity.color,
                    taskCount = getCount(listEntity.id.toString()),
                    icon = getIconByName(listEntity.iconName)
                )

                val dismissState = rememberSwipeToDismissBoxState(
                    confirmValueChange = {
                        if (it == SwipeToDismissBoxValue.EndToStart) {
                            listToDelete = listEntity.id
                            false
                        } else {
                            false
                        }
                    }
                )

                SwipeToDismissBox(
                    state = dismissState,
                    backgroundContent = {
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .background(Color.Red, shape = RoundedCornerShape(12.dp))
                                .padding(horizontal = 20.dp),
                            contentAlignment = Alignment.CenterEnd
                        ) {
                            Icon(Icons.Default.Delete, contentDescription = "Удалить", tint = Color.White)
                        }
                    },
                    enableDismissFromStartToEnd = false
                ) {
                    MyListRow(list = list, onClick = { onListClick(list.id, list.title, list.iconColor) })
                }
                Spacer(Modifier.height(8.dp))
            }

            Spacer(Modifier.height(80.dp))
        }

        if (listToDelete != null) {
            val listName = userLists.firstOrNull { it.id == listToDelete }?.title ?: "список"

            AlertDialog(
                onDismissRequest = { listToDelete = null },
                title = {
                    Text(
                        text = "Удалить список «$listName»?",
                        fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.fillMaxWidth()
                    )
                },
                text = {
                    Text(
                        text = "Данное действие приведет к удалению всех напоминаний в этом списке.",
                        textAlign = TextAlign.Center,
                        modifier = Modifier.fillMaxWidth()
                    )
                },
                confirmButton = {
                    TextButton(
                        onClick = {
                            onDeleteList(listToDelete!!)
                            listToDelete = null
                        }
                    ) {
                        Text("Удалить", color = MaterialTheme.colorScheme.error)
                    }
                },
                dismissButton = {
                    TextButton(onClick = { listToDelete = null }) {
                        Text("Отменить", color = MaterialTheme.colorScheme.primary)
                    }
                }
            )
        }
    }
}