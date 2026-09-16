package com.example.lab1
import kotlin.random.Random
class ValueFrequencyAnalyzer
{
    fun generate(): List<Int>
    {
        return List(10)
        {
            Random.nextInt(1, 100)
        }
    }
    fun analyze(list: List<Int>): List<Pair<Int, Int>>
    {
        return list
            .groupingBy { it }
            .eachCount()
            .map { Pair(it.key, it.value) }
            .sortedBy { it.second }
    }
}