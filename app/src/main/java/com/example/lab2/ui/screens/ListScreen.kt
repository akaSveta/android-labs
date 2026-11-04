package com.example.lab2.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.lab2.data.MockData
import com.example.lab2.domain.Task
import com.example.lab2.ui.components.PreviewWrapper
import androidx.compose.material.icons.filled.Check

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ListScreen(
    onBackClick: () -> Unit,
    onAddClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val themeColor = Color(0xFFFF9F0A)
    val tasks = MockData.tasks

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
            Row(modifier = Modifier.padding(start = 16.dp, bottom = 16.dp).clickable { onAddClick() }, verticalAlignment = Alignment.CenterVertically) {
                FloatingActionButton(
                    onClick = onAddClick, containerColor = themeColor, contentColor = Color.White,
                    shape = CircleShape, modifier = Modifier.size(40.dp)
                ) { Icon(Icons.Default.Add, contentDescription = null, modifier = Modifier.size(24.dp)) }
                Spacer(modifier = Modifier.width(8.dp))
                Text("Напоминание", color = themeColor, fontSize = 18.sp, fontWeight = FontWeight.Medium)
            }
        },
        floatingActionButtonPosition = FabPosition.Start
    ) { paddingValues ->
        Column(modifier = Modifier.fillMaxSize().padding(paddingValues)) {
            Text("Напоминания", fontSize = 34.sp, fontWeight = FontWeight.Bold, color = themeColor, modifier = Modifier.padding(start = 16.dp, end = 16.dp, bottom = 16.dp))

            LazyColumn(modifier = Modifier.fillMaxSize(), contentPadding = PaddingValues(bottom = 100.dp)) {
                items(tasks) { task ->
                    TaskRow(task = task, themeColor = themeColor)
                    HorizontalDivider(modifier = Modifier.padding(start = 52.dp), color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.2f))
                }
            }
        }
    }
}

@Composable
fun TaskRow(task: Task, themeColor: Color) {
    Row(modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 12.dp), verticalAlignment = Alignment.CenterVertically) {
        Box(modifier = Modifier.size(22.dp).background(Color.Transparent, shape = CircleShape).padding(2.dp)) {
            Surface(
                shape = CircleShape,
                color = if (task.isDone) themeColor else Color.Transparent,
                border = BorderStroke(1.5.dp, if (task.isDone) themeColor else MaterialTheme.colorScheme.onSurfaceVariant),
                modifier = Modifier.fillMaxSize()
            ) {
                if (task.isDone) Icon(Icons.Default.Check, contentDescription = null, tint = Color.White, modifier = Modifier.padding(3.dp))
            }
        }

        Spacer(modifier = Modifier.width(12.dp))

        Column(modifier = Modifier.weight(1f)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                if (task.priority > 0 && !task.isDone) {
                    val text = when (task.priority) { 1 -> "!"; 2 -> "!!"; 3 -> "!!!"; else -> "" }
                    Text(text = text, color = themeColor, fontWeight = FontWeight.Bold, fontSize = 16.sp, modifier = Modifier.padding(end = 4.dp))
                }
                Text(
                    text = task.title, fontSize = 16.sp,
                    color = if (task.isDone) MaterialTheme.colorScheme.onSurfaceVariant else MaterialTheme.colorScheme.onSurface
                )
            }
            if (task.notes.isNotEmpty() && !task.isDone) {
                Text(text = task.notes, fontSize = 14.sp, color = MaterialTheme.colorScheme.onSurfaceVariant, modifier = Modifier.padding(top = 2.dp))
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun ListScreenPreview() { PreviewWrapper { ListScreen(onBackClick = {}, onAddClick = {}) } }