package com.example.lab2.ui.screens

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Flag
import androidx.compose.material.icons.outlined.Schedule
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import java.util.Calendar

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailScreen(
    initialDate: String,
    initialTime: String,
    onBackClick: () -> Unit,
    onDateSelected: (String) -> Unit,
    onTimeSelected: (String) -> Unit,
    onSaveClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current

    var isDateEnabled by remember { mutableStateOf(initialDate.isNotEmpty()) }
    var isTimeEnabled by remember { mutableStateOf(initialTime.isNotEmpty()) }
    var isFlagged by remember { mutableStateOf(false) }
    var priority by remember { mutableIntStateOf(0) }
    var isMenuExpanded by remember { mutableStateOf(false) }

    val datePickerDialog = remember {
        val calendar = Calendar.getInstance()
        DatePickerDialog(
            context,
            { _, year, month, dayOfMonth ->
                val newDate = "$dayOfMonth.${month + 1}.$year"
                onDateSelected(newDate)
                isDateEnabled = true
            },
            calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH)
        )
    }

    val timePickerDialog = remember {
        val calendar = Calendar.getInstance()
        TimePickerDialog(
            context,
            { _, hourOfDay, minute ->
                val newTime = String.format("%02d:%02d", hourOfDay, minute)
                onTimeSelected(newTime)
                isTimeEnabled = true
            },
            calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), true
        )
    }

    Scaffold(
        modifier = modifier.fillMaxSize(),
        topBar = {
            TopAppBar(
                title = { Text("Подробно", fontWeight = FontWeight.Bold, modifier = Modifier.fillMaxWidth(), textAlign = androidx.compose.ui.text.style.TextAlign.Center) },
                navigationIcon = {
                    Row(modifier = Modifier.clickable { onBackClick() }.padding(horizontal = 16.dp), verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = null, tint = MaterialTheme.colorScheme.primary, modifier = Modifier.size(20.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("Назад", color = MaterialTheme.colorScheme.primary, fontSize = 17.sp)
                    }
                },
                actions = {
                    TextButton(onClick = onSaveClick) {
                        Text("Добавить", fontSize = 16.sp, fontWeight = FontWeight.Bold)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = MaterialTheme.colorScheme.surface)
            )
        }
    ) { paddingValues ->
        Column(modifier = Modifier.fillMaxSize().padding(paddingValues).padding(16.dp).verticalScroll(rememberScrollState())) {

            Card(shape = RoundedCornerShape(12.dp), colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant), modifier = Modifier.fillMaxWidth()) {
                Column {
                    Row(modifier = Modifier.fillMaxWidth().clickable { datePickerDialog.show() }.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
                        Box(modifier = Modifier.size(28.dp).background(Color(0xFFFF453A), shape = RoundedCornerShape(6.dp)), contentAlignment = Alignment.Center) {
                            Icon(Icons.Default.DateRange, contentDescription = null, tint = Color.White, modifier = Modifier.size(18.dp))
                        }
                        Spacer(Modifier.width(12.dp))
                        Text("Дата", modifier = Modifier.weight(1f))
                        Text(if (initialDate.isEmpty()) "Не задано" else initialDate, color = MaterialTheme.colorScheme.onSurfaceVariant, fontSize = 14.sp)
                        Spacer(Modifier.width(8.dp))
                        Switch(checked = isDateEnabled, onCheckedChange = { isDateEnabled = it; if (it) datePickerDialog.show() })
                    }
                    HorizontalDivider(color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.2f))
                    Row(modifier = Modifier.fillMaxWidth().clickable { timePickerDialog.show() }.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
                        Box(modifier = Modifier.size(28.dp).background(Color(0xFF0A84FF), shape = RoundedCornerShape(6.dp)), contentAlignment = Alignment.Center) {
                            Icon(Icons.Outlined.Schedule, contentDescription = null, tint = Color.White, modifier = Modifier.size(20.dp))
                        }
                        Spacer(Modifier.width(12.dp))
                        Text("Время", modifier = Modifier.weight(1f))
                        Text(if (initialTime.isEmpty()) "Не задано" else initialTime, color = MaterialTheme.colorScheme.onSurfaceVariant, fontSize = 14.sp)
                        Spacer(Modifier.width(8.dp))
                        Switch(checked = isTimeEnabled, onCheckedChange = { isTimeEnabled = it; if (it) timePickerDialog.show() })
                    }
                }
            }

            Spacer(Modifier.height(16.dp))

            Card(shape = RoundedCornerShape(12.dp), colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant), modifier = Modifier.fillMaxWidth()) {
                Row(modifier = Modifier.fillMaxWidth().padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
                    Box(modifier = Modifier.size(28.dp).background(Color(0xFFFF9F0A), shape = RoundedCornerShape(6.dp)), contentAlignment = Alignment.Center) {
                        Icon(Icons.Default.Flag, contentDescription = null, tint = Color.White, modifier = Modifier.size(18.dp))
                    }
                    Spacer(Modifier.width(12.dp))
                    Text("Флажок", modifier = Modifier.weight(1f))
                    Switch(checked = isFlagged, onCheckedChange = { isFlagged = it })
                }
            }

            Spacer(Modifier.height(16.dp))

            Card(shape = RoundedCornerShape(12.dp), colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant), modifier = Modifier.fillMaxWidth()) {
                Row(modifier = Modifier.fillMaxWidth().clickable { isMenuExpanded = true }.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
                    Box(modifier = Modifier.size(28.dp).background(Color(0xFFFF3B30), shape = RoundedCornerShape(6.dp)), contentAlignment = Alignment.Center) {
                        Text("!", color = Color.White, fontWeight = FontWeight.Bold)
                    }
                    Spacer(Modifier.width(12.dp))
                    Text("Приоритет", modifier = Modifier.weight(1f))
                    val priorityText = when (priority) { 1 -> "Низкий"; 2 -> "Средний"; 3 -> "Высокий"; else -> "Нет" }
                    Text(priorityText, color = MaterialTheme.colorScheme.onSurfaceVariant)

                    DropdownMenu(expanded = isMenuExpanded, onDismissRequest = { isMenuExpanded = false }) {
                        DropdownMenuItem(text = { Text("Нет") }, onClick = { priority = 0; isMenuExpanded = false })
                        DropdownMenuItem(text = { Text("Низкий (!)") }, onClick = { priority = 1; isMenuExpanded = false })
                        DropdownMenuItem(text = { Text("Средний (!!)") }, onClick = { priority = 2; isMenuExpanded = false })
                        DropdownMenuItem(text = { Text("Высокий (!!!)") }, onClick = { priority = 3; isMenuExpanded = false })
                    }
                }
            }
        }
    }
}