package com.example.lab2.ui.screens

import android.Manifest
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.pm.PackageManager
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import com.example.lab2.R
import com.example.lab2.utils.addEventToCalendarSilently
import java.util.Calendar

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailScreen(
    title: String,
    initialDate: String,
    initialTime: String,
    initialFlagged: Boolean,
    onBackClick: () -> Unit,
    onDateSelected: (String) -> Unit,
    onTimeSelected: (String) -> Unit,
    onSaveClick: (Int, Boolean) -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current

    var isDateEnabled by remember { mutableStateOf(initialDate.isNotEmpty()) }
    var isTimeEnabled by remember { mutableStateOf(initialTime.isNotEmpty()) }
    var isFlagged by remember { mutableStateOf(initialFlagged) }
    var priority by remember { mutableIntStateOf(0) }
    var isMenuExpanded by remember { mutableStateOf(false) }

    val permissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        val allGranted = permissions.values.all { it }
        if (allGranted && initialDate.isNotEmpty()) {
            addEventToCalendarSilently(context, title, initialDate, initialTime)
        }
    }

    val datePickerDialog = remember(initialDate) {
        val calendar = Calendar.getInstance()
        if (initialDate.isNotEmpty()) {
            try {
                val parts = initialDate.split(".")
                if (parts.size == 3) calendar.set(parts[2].toInt(), parts[1].toInt() - 1, parts[0].toInt())
            } catch (e: Exception) { }
        }
        DatePickerDialog(context, { _, year, month, dayOfMonth ->
            val newDate = "$dayOfMonth.${month + 1}.$year"
            onDateSelected(newDate); isDateEnabled = true
        }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH))
    }

    val timePickerDialog = remember(initialTime) {
        val calendar = Calendar.getInstance()
        if (initialTime.isNotEmpty()) {
            try {
                val parts = initialTime.split(":")
                if (parts.size == 2) {
                    calendar.set(Calendar.HOUR_OF_DAY, parts[0].toInt())
                    calendar.set(Calendar.MINUTE, parts[1].toInt())
                }
            } catch (e: Exception) { }
        }
        TimePickerDialog(context, { _, hourOfDay, minute ->
            val newTime = String.format("%02d:%02d", hourOfDay, minute)
            onTimeSelected(newTime); isTimeEnabled = true
        }, calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), true)
    }

    Scaffold(
        modifier = modifier.fillMaxSize(),
        topBar = {
            TopAppBar(
                title = { Text(stringResource(R.string.details), fontWeight = FontWeight.Bold, modifier = Modifier.fillMaxWidth(), textAlign = androidx.compose.ui.text.style.TextAlign.Center) },
                navigationIcon = {
                    Row(modifier = Modifier.clickable { onBackClick() }.padding(horizontal = 16.dp), verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = null, tint = MaterialTheme.colorScheme.primary, modifier = Modifier.size(20.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(stringResource(R.string.back), color = MaterialTheme.colorScheme.primary, fontSize = 17.sp)
                    }
                },
                actions = {
                    TextButton(onClick = {
                        val hasRead = ContextCompat.checkSelfPermission(context, Manifest.permission.READ_CALENDAR) == PackageManager.PERMISSION_GRANTED
                        val hasWrite = ContextCompat.checkSelfPermission(context, Manifest.permission.WRITE_CALENDAR) == PackageManager.PERMISSION_GRANTED
                        if (hasRead && hasWrite) {
                            if (initialDate.isNotEmpty()) addEventToCalendarSilently(context, title, initialDate, initialTime)
                        } else {
                            permissionLauncher.launch(arrayOf(Manifest.permission.READ_CALENDAR, Manifest.permission.WRITE_CALENDAR))
                        }
                        onSaveClick(priority, isFlagged)
                    }) {
                        Text(stringResource(R.string.save), fontSize = 16.sp, fontWeight = FontWeight.Bold)
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
                        Spacer(Modifier.width(12.dp)); Text(stringResource(R.string.date), modifier = Modifier.weight(1f))
                        Text(if (initialDate.isEmpty()) stringResource(R.string.not_set) else initialDate, color = MaterialTheme.colorScheme.onSurfaceVariant, fontSize = 14.sp)
                        Spacer(Modifier.width(8.dp)); Switch(checked = isDateEnabled, onCheckedChange = { isDateEnabled = it; if (it) datePickerDialog.show() })
                    }
                    HorizontalDivider(color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.2f))
                    Row(modifier = Modifier.fillMaxWidth().clickable { timePickerDialog.show() }.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
                        Box(modifier = Modifier.size(28.dp).background(Color(0xFF0A84FF), shape = RoundedCornerShape(6.dp)), contentAlignment = Alignment.Center) {
                            Icon(Icons.Outlined.Schedule, contentDescription = null, tint = Color.White, modifier = Modifier.size(20.dp))
                        }
                        Spacer(Modifier.width(12.dp)); Text(stringResource(R.string.time), modifier = Modifier.weight(1f))
                        Text(if (initialTime.isEmpty()) stringResource(R.string.not_set) else initialTime, color = MaterialTheme.colorScheme.onSurfaceVariant, fontSize = 14.sp)
                        Spacer(Modifier.width(8.dp)); Switch(checked = isTimeEnabled, onCheckedChange = { isTimeEnabled = it; if (it) timePickerDialog.show() })
                    }
                }
            }

            Spacer(Modifier.height(16.dp))

            Card(shape = RoundedCornerShape(12.dp), colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant), modifier = Modifier.fillMaxWidth()) {
                Row(modifier = Modifier.fillMaxWidth().padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
                    Box(modifier = Modifier.size(28.dp).background(Color(0xFFFF9F0A), shape = RoundedCornerShape(6.dp)), contentAlignment = Alignment.Center) {
                        Icon(Icons.Default.Flag, contentDescription = null, tint = Color.White, modifier = Modifier.size(18.dp))
                    }
                    Spacer(Modifier.width(12.dp)); Text(stringResource(R.string.flag), modifier = Modifier.weight(1f))
                    Switch(checked = isFlagged, onCheckedChange = { isFlagged = it })
                }
            }

            Spacer(Modifier.height(16.dp))

            Card(shape = RoundedCornerShape(12.dp), colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant), modifier = Modifier.fillMaxWidth()) {
                Row(modifier = Modifier.fillMaxWidth().clickable { isMenuExpanded = true }.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
                    Box(modifier = Modifier.size(28.dp).background(Color(0xFFFF3B30), shape = RoundedCornerShape(6.dp)), contentAlignment = Alignment.Center) {
                        Text("!", color = Color.White, fontWeight = FontWeight.Bold)
                    }
                    Spacer(Modifier.width(12.dp)); Text(stringResource(R.string.priority), modifier = Modifier.weight(1f))
                    val priorityText = when (priority) {
                        1 -> stringResource(R.string.low)
                        2 -> stringResource(R.string.medium)
                        3 -> stringResource(R.string.high)
                        else -> stringResource(R.string.none)
                    }
                    Text(priorityText, color = MaterialTheme.colorScheme.onSurfaceVariant)

                    DropdownMenu(expanded = isMenuExpanded, onDismissRequest = { isMenuExpanded = false }) {
                        DropdownMenuItem(text = { Text(stringResource(R.string.none)) }, onClick = { priority = 0; isMenuExpanded = false })
                        DropdownMenuItem(text = { Text(stringResource(R.string.low) + " (!)") }, onClick = { priority = 1; isMenuExpanded = false })
                        DropdownMenuItem(text = { Text(stringResource(R.string.medium) + " (!!)") }, onClick = { priority = 2; isMenuExpanded = false })
                        DropdownMenuItem(text = { Text(stringResource(R.string.high) + " (!!!)") }, onClick = { priority = 3; isMenuExpanded = false })
                    }
                }
            }
        }
    }
}