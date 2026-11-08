package com.example.lab2.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddListScreen(
    onCancelClick: () -> Unit,
    onSaveClick: (String, Long, String) -> Unit,
    modifier: Modifier = Modifier
) {
    var title by remember { mutableStateOf("") }

    val colors = listOf(
        0xFFFF3B30, 0xFFFF9500, 0xFFFFCC00, 0xFF34C759, 0xFF5AC8FA, 0xFF007AFF,
        0xFF5856D6, 0xFFFF2D55, 0xFFAF52DE, 0xFFD2B48C, 0xFF8E8E93, 0xFFFFB6C1
    )
    var selectedColor by remember { mutableLongStateOf(colors[5]) }

    val icons = listOf(
        Icons.Default.Menu to "Menu",
        Icons.Default.List to "List",
        Icons.Default.Star to "Star",
        Icons.Default.Favorite to "Favorite",
        Icons.Default.Home to "Home",
        Icons.Default.Person to "Person",
        Icons.Default.ShoppingCart to "ShoppingCart",
        Icons.Default.DateRange to "DateRange",
        Icons.Default.Email to "Email",
        Icons.Default.Phone to "Phone",
        Icons.Default.Settings to "Settings"
    )
    var selectedIcon by remember { mutableStateOf(icons[0]) }

    Scaffold(
        modifier = modifier.fillMaxSize(),
        topBar = {
            TopAppBar(
                title = { Text("Новый список", fontWeight = FontWeight.Bold) },
                navigationIcon = { TextButton(onClick = onCancelClick) { Text("Отмена", fontSize = 16.sp) } },
                actions = {
                    TextButton(
                        onClick = { if (title.isNotBlank()) onSaveClick(title, selectedColor, selectedIcon.second) },
                        enabled = title.isNotBlank()
                    ) {
                        Text("Готово", fontSize = 16.sp, fontWeight = FontWeight.Bold)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = MaterialTheme.colorScheme.surface)
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Box(
                modifier = Modifier
                    .size(120.dp)
                    .background(Color(selectedColor), shape = CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = selectedIcon.first,
                    contentDescription = null,
                    tint = Color.White,
                    modifier = Modifier.size(60.dp)
                )
            }

            Spacer(Modifier.height(24.dp))

            Card(
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
                modifier = Modifier.fillMaxWidth()
            ) {
                TextField(
                    value = title,
                    onValueChange = { title = it },
                    placeholder = { Text("Название списка", modifier = Modifier.fillMaxWidth(), textAlign = androidx.compose.ui.text.style.TextAlign.Center) },
                    colors = TextFieldDefaults.colors(
                        focusedContainerColor = Color.Transparent, unfocusedContainerColor = Color.Transparent,
                        focusedIndicatorColor = Color.Transparent, unfocusedIndicatorColor = Color.Transparent
                    ),
                    textStyle = androidx.compose.ui.text.TextStyle(fontSize = 18.sp, textAlign = androidx.compose.ui.text.style.TextAlign.Center),
                    modifier = Modifier.fillMaxWidth()
                )
            }

            Spacer(Modifier.height(24.dp))

            Text("Цвет", fontSize = 16.sp, fontWeight = FontWeight.Medium, modifier = Modifier.align(Alignment.Start))
            Spacer(Modifier.height(8.dp))

            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                colors.chunked(6).forEach { rowColors ->
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp), modifier = Modifier.fillMaxWidth()) {
                        rowColors.forEach { colorVal ->
                            Box(
                                modifier = Modifier
                                    .weight(1f)
                                    .aspectRatio(1f)
                                    .background(Color(colorVal), shape = CircleShape)
                                    .border(
                                        width = if (selectedColor == colorVal) 3.dp else 0.dp,
                                        color = if (selectedColor == colorVal) MaterialTheme.colorScheme.onSurface else Color.Transparent,
                                        shape = CircleShape
                                    )
                                    .clickable { selectedColor = colorVal }
                            )
                        }
                    }
                }
            }

            Spacer(Modifier.height(24.dp))

            Text("Иконка", fontSize = 16.sp, fontWeight = FontWeight.Medium, modifier = Modifier.align(Alignment.Start))
            Spacer(Modifier.height(8.dp))

            LazyVerticalGrid(
                columns = GridCells.Fixed(6),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.weight(1f)
            ) {
                items(icons) { iconPair ->
                    val isSelected = selectedIcon.second == iconPair.second
                    Box(
                        modifier = Modifier
                            .aspectRatio(1f)
                            .background(
                                if (isSelected) Color(selectedColor).copy(alpha = 0.2f) else MaterialTheme.colorScheme.surfaceVariant,
                                shape = CircleShape
                            )
                            .border(
                                width = if (isSelected) 2.dp else 0.dp,
                                color = if (isSelected) Color(selectedColor) else Color.Transparent,
                                shape = CircleShape
                            )
                            .clickable { selectedIcon = iconPair },
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = iconPair.first,
                            contentDescription = null,
                            tint = if (isSelected) Color(selectedColor) else MaterialTheme.colorScheme.onSurfaceVariant,
                            modifier = Modifier.size(24.dp)
                        )
                    }
                }
            }
        }
    }
}