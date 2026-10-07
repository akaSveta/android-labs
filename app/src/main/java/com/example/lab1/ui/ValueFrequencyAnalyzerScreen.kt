package com.example.lab1.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.lab1.data.ListGenerator
import com.example.lab1.domain.ValueFrequencyAnalyzer

@Composable
fun ValueFrequencyAnalyzerScreen(modifier: Modifier = Modifier) {
    var currentList by remember { mutableStateOf<List<Int>>(emptyList()) }
    var listText by remember { mutableStateOf("") }
    var resultText by remember { mutableStateOf("") }

    LaunchedEffect(Unit) {
        currentList = ListGenerator.generate()
        listText = "Список: $currentList"
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        OutlinedCard(modifier = Modifier.fillMaxWidth()) {
            Text(
                text = if (listText.isEmpty()) "Нажмите «Сгенерировать»" else listText,
                modifier = Modifier.padding(12.dp)
            )
        }

        Button(
            onClick = {
                currentList = ListGenerator.generate()
                listText = "Список: $currentList"
                resultText = ""
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Сгенерировать")
        }

        Button(
            onClick = {
                val results = ValueFrequencyAnalyzer.analyze(currentList)
                resultText = "Уникальные элементы (по убыванию количества):\n" +
                        ValueFrequencyAnalyzer.formatResults(results)
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Вывести различные элементы с сортировкой (по возрастанию количества)")
        }

        if (resultText.isNotEmpty()) {
            OutlinedCard(modifier = Modifier.fillMaxWidth()) {
                Text(
                    text = resultText,
                    modifier = Modifier.padding(12.dp)
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ValueFrequencyAnalyzerScreenPreview() {
    ValueFrequencyAnalyzerScreen()
}