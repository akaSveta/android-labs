package com.example.lab2.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.lab2.data.MockData
import com.example.lab2.domain.TaskList

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SelectListScreen(
    viewModel: ListViewModel,
    currentListId: String,
    onBackClick: () -> Unit,
    onListSelected: (String, String, Long, String) -> Unit,
    modifier: Modifier = Modifier
) {
    val userLists by viewModel.taskLists.collectAsState(initial = emptyList())
    val allLists = mutableListOf<TaskList>()
    MockData.myLists.firstOrNull { it.id == "6" }?.let { allLists.add(it) }

    userLists.forEach { entity ->
        allLists.add(TaskList(id = entity.id.toString(), title = entity.title, iconColor = entity.color, taskCount = 0, icon = getIconByName(entity.iconName)))
    }

    Scaffold(
        modifier = modifier.fillMaxSize(),
        topBar = {
            TopAppBar(
                title = { Text("Список", fontWeight = FontWeight.Bold, modifier = Modifier.fillMaxWidth(), textAlign = androidx.compose.ui.text.style.TextAlign.Center) },
                navigationIcon = {
                    Row(modifier = Modifier.clickable { onBackClick() }.padding(horizontal = 16.dp), verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = null, tint = MaterialTheme.colorScheme.primary, modifier = Modifier.size(20.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("Назад", color = MaterialTheme.colorScheme.primary, fontSize = 17.sp)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = MaterialTheme.colorScheme.surface)
            )
        }
    ) { paddingValues ->
        Column(modifier = Modifier.fillMaxSize().padding(paddingValues)) {
            Text(
                text = "Напоминание будет создано в списке «${allLists.firstOrNull { it.id == currentListId }?.title ?: "Напоминания"}»",
                modifier = Modifier.padding(16.dp), fontSize = 14.sp, color = MaterialTheme.colorScheme.onSurfaceVariant, textAlign = androidx.compose.ui.text.style.TextAlign.Center
            )
            LazyColumn {
                items(allLists) { list ->
                    val isSelected = list.id == currentListId
                    Row(
                        modifier = Modifier.fillMaxWidth().clickable {
                            val iconName = if (list.id == "6") "Menu" else userLists.firstOrNull { it.id.toString() == list.id }?.iconName ?: "Menu"
                            onListSelected(list.id, list.title, list.iconColor, iconName)
                        }.padding(horizontal = 16.dp, vertical = 12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(modifier = Modifier.size(32.dp).background(Color(list.iconColor), shape = CircleShape), contentAlignment = Alignment.Center) {
                            Icon(imageVector = list.icon ?: Icons.Default.Menu, contentDescription = null, tint = Color.White, modifier = Modifier.size(18.dp))
                        }
                        Spacer(modifier = Modifier.width(12.dp))
                        Text(text = list.title, fontSize = 16.sp, modifier = Modifier.weight(1f))
                        if (isSelected) Icon(Icons.Default.Check, contentDescription = "Selected", tint = MaterialTheme.colorScheme.primary)
                    }
                    HorizontalDivider(modifier = Modifier.padding(start = 60.dp), color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.2f))
                }
            }
        }
    }
}