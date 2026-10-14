package com.example.lab1.data

import kotlin.random.Random

object ListGenerator
{
    fun generate(): List<Int>
    {
        return List(10)
        {
            Random.nextInt(0, 10)
        }
    }
}