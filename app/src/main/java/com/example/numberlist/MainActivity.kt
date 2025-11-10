package com.example.numberlist

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.numberlist.ui.theme.NumberListTheme
import kotlin.math.sqrt

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            NumberListTheme {
                Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
                    NumberListScreen()
                }
            }
        }
    }
}

@Composable
fun NumberListScreen() {
    var numberInput by remember { mutableStateOf("100") }
    var selectedType by remember { mutableStateOf(NumberType.ODD) }
    val numbers = generateNumbers(numberInput.toIntOrNull() ?: 0, selectedType)

    Column(modifier = Modifier
        .fillMaxSize()
        .padding(16.dp)) {
        OutlinedTextField(
            value = numberInput,
            onValueChange = { numberInput = it },
            label = { Text("Nhập một số") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        NumberType.values().toList().chunked(2).forEach { rowItems ->
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceAround
            ) {
                rowItems.forEach { type ->
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        RadioButton(
                            selected = selectedType == type,
                            onClick = { selectedType = type }
                        )
                        Text(text = type.displayName)
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        if (numbers.isEmpty()) {
            Text(text = "Không có số nào thỏa mãn")
        } else {
            LazyColumn(modifier = Modifier.weight(1f)) {
                items(numbers) { number ->
                    Text(text = number.toString(), modifier = Modifier.padding(8.dp))
                }
            }
        }
    }
}

enum class NumberType(val displayName: String) {
    ODD("Số lẻ"),
    EVEN("Số chẵn"),
    PRIME("Số nguyên tố"),
    PERFECT("Số hoàn hảo"),
    SQUARE("Số chính phương"),
    FIBONACCI("Số Fibonacci")
}

fun generateNumbers(maxNumber: Int, type: NumberType): List<Int> {
    return when (type) {
        NumberType.ODD -> (1 until maxNumber).filter { it % 2 != 0 }
        NumberType.EVEN -> (1 until maxNumber).filter { it % 2 == 0 }
        NumberType.PRIME -> (1 until maxNumber).filter { isPrime(it) }
        NumberType.PERFECT -> (1 until maxNumber).filter { isPerfect(it) }
        NumberType.SQUARE -> (1 until maxNumber).filter { isSquare(it) }
        NumberType.FIBONACCI -> {
            val fibs = mutableListOf(1, 1)
            while (fibs.last() < maxNumber) {
                fibs.add(fibs[fibs.size - 1] + fibs[fibs.size - 2])
            }
            fibs.takeWhile { it < maxNumber }.distinct()
        }
    }
}

fun isPrime(n: Int): Boolean {
    if (n <= 1) return false
    for (i in 2..sqrt(n.toDouble()).toInt()) {
        if (n % i == 0) return false
    }
    return true
}

fun isPerfect(n: Int): Boolean {
    if (n <= 1) return false
    var sum = 1
    for (i in 2..sqrt(n.toDouble()).toInt()) {
        if (n % i == 0) {
            sum += i
            if (i*i != n) {
                sum += n/i
            }
        }
    }
    return sum == n
}

fun isSquare(n: Int): Boolean {
    if (n < 1) return false
    val sqrt = sqrt(n.toDouble()).toInt()
    return sqrt * sqrt == n
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    NumberListTheme {
        NumberListScreen()
    }
}
