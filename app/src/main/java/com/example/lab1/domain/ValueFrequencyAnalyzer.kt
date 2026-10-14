package com.example.lab1.domain

data class ValueFrequencyResult(
    val number: Int,
    val count: Int
)

object ValueFrequencyAnalyzer {

    fun analyze(list: List<Int>): List<ValueFrequencyResult> {
        return list
            .groupingBy { it }
            .eachCount()
            .map { ValueFrequencyResult(it.key, it.value) }
            .sortedByDescending { it.count }
    }

    fun formatResults(results: List<ValueFrequencyResult>): String {
        if (results.isEmpty()) return "Список пуст"
        return results.joinToString("\n") { r ->
            "${r.number} — ${r.count} раз(а)"
        }
    }
}