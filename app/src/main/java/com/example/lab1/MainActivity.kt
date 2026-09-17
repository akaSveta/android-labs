package com.example.lab1

import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class MainActivity : AppCompatActivity() {
    private val analyzer = ValueFrequencyAnalyzer()
    private var currentList: List<Int> = emptyList()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main))
        { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        val textView = findViewById<TextView>(R.id.textView)
        val button = findViewById<Button>(R.id.button)
        val button2 = findViewById<Button>(R.id.button2)
        val textView2 = findViewById<TextView>(R.id.textView2)
        currentList = analyzer.generate()
        textView.text = "Список: $currentList"
        button.setOnClickListener {
            currentList = analyzer.generate()
            textView.text = "Список: $currentList"
            textView2.text = ""
        }
        button2.setOnClickListener {
            val result = analyzer.analyze(currentList)
            var text = "Уникальные элементы (по возрастанию количества):\n"
            for (pair in result) {
                text += "${pair.first} — ${pair.second} раз(а)\n"
            }
            textView2.text = text
        }
    }
}