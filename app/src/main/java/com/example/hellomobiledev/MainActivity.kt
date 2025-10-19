package com.example.hellomobiledev

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import com.example.hellomobiledev.ui.theme.HelloMobileDevTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge() // Ensure this is called
        setContent {
            HelloMobileDevTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    // Display the CalculatorScreen
                    CalculatorScreen(modifier = Modifier.padding(innerPadding))
                }
            }
        }
    }
}
