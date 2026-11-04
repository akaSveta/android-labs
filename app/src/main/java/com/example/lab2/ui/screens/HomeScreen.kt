package com.example.lab2.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Settings
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
import com.example.lab2.ui.components.MyListRow
import com.example.lab2.ui.components.PreviewWrapper
import com.example.lab2.ui.components.SmallListCard

@Composable
fun HomeScreen(
    onListClick: () -> Unit,
    onAddClick: () -> Unit,
    modifier: Modifier = Modifier
) {
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
                    modifier = Modifier.clickable { /* Пока пусто */ }.padding(8.dp)
                )
            }
        },
        floatingActionButtonPosition = FabPosition.Center
    ) { paddingValues ->
        Column(
            modifier = Modifier.fillMaxSize().padding(paddingValues).padding(horizontal = 16.dp).verticalScroll(rememberScrollState())
        ) {
            Row(modifier = Modifier.fillMaxWidth().padding(top = 8.dp, bottom = 8.dp), horizontalArrangement = Arrangement.End) {
                IconButton(onClick = { /* TODO */ }) {
                    Icon(Icons.Default.Settings, contentDescription = null, tint = MaterialTheme.colorScheme.primary, modifier = Modifier.size(28.dp))
                }
            }

            OutlinedTextField(
                value = "", onValueChange = {}, modifier = Modifier.fillMaxWidth().height(48.dp),
                placeholder = { Text("Поиск", fontSize = 14.sp) },
                leadingIcon = { Icon(Icons.Default.Search, contentDescription = null, modifier = Modifier.size(20.dp)) },
                shape = RoundedCornerShape(12.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedContainerColor = MaterialTheme.colorScheme.surfaceVariant,
                    unfocusedContainerColor = MaterialTheme.colorScheme.surfaceVariant,
                    focusedBorderColor = Color.Transparent, unfocusedBorderColor = Color.Transparent
                ),
                singleLine = true
            )

            Spacer(Modifier.height(24.dp))

            MockData.baseLists.chunked(2).forEach { rowItems ->
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    rowItems.forEach { list ->
                        Box(modifier = Modifier.weight(1f)) {
                            SmallListCard(list = list, onClick = onListClick)
                        }
                    }
                    if (rowItems.size == 1) Spacer(modifier = Modifier.weight(1f))
                }
                Spacer(Modifier.height(12.dp))
            }

            Spacer(Modifier.height(24.dp))
            Text("Мои списки", fontSize = 22.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onSurface)
            Spacer(Modifier.height(12.dp))

            MockData.myLists.forEach { list ->
                MyListRow(list, onClick = onListClick)
                Spacer(Modifier.height(8.dp))
            }
            Spacer(Modifier.height(80.dp))
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun HomeScreenPreview() { PreviewWrapper { HomeScreen(onListClick = {}, onAddClick = {}) } }