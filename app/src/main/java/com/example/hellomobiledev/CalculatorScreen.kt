package com.example.hellomobiledev

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.hellomobiledev.ui.theme.HelloMobileDevTheme

@Composable
fun CalculatorScreen(modifier: Modifier = Modifier) {
    var operand1 by remember { mutableStateOf<Double?>(null) }
    var operator by remember { mutableStateOf<String?>(null) }
    var displayResult by remember { mutableStateOf("0") } // To show on screen

    // --- Helper functions for logic ---
    fun onNumberClick(number: String) {
        if (displayResult == "0" && number != ".") {
            displayResult = number
        } else if (displayResult == "Error") {
            displayResult = number
        } else if (!(number == "." && displayResult.contains("."))) { // Allow only one decimal
            displayResult += number
        }
    }

    // This is the ONLY definition of performCalculation
    fun performCalculation(op1: Double?, op2: Double?, oper: String): Double? {
        if (op1 == null || op2 == null) return null
        return when (oper) {
            "+" -> op1 + op2
            "-" -> op1 - op2
            "*" -> op1 * op2
            "/" -> if (op2 != 0.0) op1 / op2 else Double.NaN // Handle division by zero
            else -> null
        }
    }

    // This is the ONLY definition of formatResult
    fun formatResult(result: Double?): String {
        if (result == null) return "Error"
        if (result.isNaN()) return "Error (Div by 0)" // More specific error
        // Show as Long if it's a whole number, otherwise show decimal
        return if (result % 1 == 0.0) {
            result.toLong().toString()
        } else {
            // Basic formatting, consider String.format for more control over decimal places
            result.toString()
        }
    }

    fun onOperatorClick(op: String) {
        if (displayResult == "Error") return // Don't process if display is Error

        val currentDisplayNumber = displayResult.toDoubleOrNull()

        if (operand1 == null) {
            operand1 = currentDisplayNumber
            operator = op
            displayResult = "0" // Clear display for next input
        } else if (operator != null && currentDisplayNumber != null) {
            // If an operator is already set, calculate the intermediate result
            val result = performCalculation(operand1, currentDisplayNumber, operator!!) // Calling the correct function
            displayResult = formatResult(result) // Calling the correct function
            operand1 = result // Store result as new operand1
            operator = op     // Set the new operator
        } else if (currentDisplayNumber != null) { // If only operand1 was set, now set operator
            operator = op
            // displayResult = "0" // Potentially keep current display for clarity e.g. 5, then +, display still 5
        }
    }

    fun onEqualsClick() {
        if (displayResult == "Error") return

        if (operand1 != null && operator != null) {
            val operand2 = displayResult.toDoubleOrNull()
            if (operand2 != null) {
                val result = performCalculation(operand1, operand2, operator!!) // Calling the correct function
                displayResult = formatResult(result) // Calling the correct function
                operand1 = null // Reset for next calculation
                operator = null
            }
        }
    }

    fun onClearClick() {
        operand1 = null
        operator = null
        displayResult = "0"
    }


    // --- UI Layout ---
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(8.dp, Alignment.Bottom) // Space buttons towards bottom
    ) {
        Text(
            text = displayResult,
            style = MaterialTheme.typography.displayMedium,
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 32.dp, horizontal = 8.dp) // More vertical padding for display
                .wrapContentHeight(align = Alignment.Bottom), // Align text to bottom of its space
            textAlign = TextAlign.End // Right align text
        )

        val buttonModifier = Modifier.weight(1f) // Base modifier for equal weight distribution

        // Calculator Buttons
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            CalculatorButton("7", buttonModifier) { onNumberClick("7") }
            CalculatorButton("8", buttonModifier) { onNumberClick("8") }
            CalculatorButton("9", buttonModifier) { onNumberClick("9") }
            CalculatorButton("/", buttonModifier, MaterialTheme.colorScheme.tertiaryContainer) { onOperatorClick("/") }
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            CalculatorButton("4", buttonModifier) { onNumberClick("4") }
            CalculatorButton("5", buttonModifier) { onNumberClick("5") }
            CalculatorButton("6", buttonModifier) { onNumberClick("6") }
            CalculatorButton("*", buttonModifier, MaterialTheme.colorScheme.tertiaryContainer) { onOperatorClick("*") }
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            CalculatorButton("1", buttonModifier) { onNumberClick("1") }
            CalculatorButton("2", buttonModifier) { onNumberClick("2") }
            CalculatorButton("3", buttonModifier) { onNumberClick("3") }
            CalculatorButton("-", buttonModifier, MaterialTheme.colorScheme.tertiaryContainer) { onOperatorClick("-") }
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            CalculatorButton("0", buttonModifier.weight(2.1f)) { onNumberClick("0") }
            CalculatorButton("C", buttonModifier, MaterialTheme.colorScheme.errorContainer) { onClearClick() }
            CalculatorButton("+", buttonModifier, MaterialTheme.colorScheme.tertiaryContainer) { onOperatorClick("+") }
        }
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            CalculatorButton(".", buttonModifier) { onNumberClick(".") }
            CalculatorButton("=", buttonModifier.weight(2.1f), MaterialTheme.colorScheme.primary) { onEqualsClick() }
        }
    }
}

@Composable
fun CalculatorButton(
    text: String,
    modifier: Modifier = Modifier,
    backgroundColor: Color = MaterialTheme.colorScheme.surfaceVariant,
    onClick: () -> Unit
) {
    Button(
        onClick = onClick,
        modifier = modifier
            .aspectRatio(1.3f)
            .padding(4.dp),
        shape = MaterialTheme.shapes.medium,
        colors = ButtonDefaults.buttonColors(containerColor = backgroundColor)
    ) {
        Text(text, style = MaterialTheme.typography.headlineSmall)
    }
}

@Preview(showBackground = true)
@Composable
fun CalculatorScreenPreview() {
    HelloMobileDevTheme {
        CalculatorScreen()
    }
}

