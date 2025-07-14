package com.dksystem.crazyclick

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.dksystem.crazyclick.ui.theme.CrazyClickTheme
import kotlinx.coroutines.launch
import kotlin.random.Random

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            CrazyClickTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    CounterApp(modifier = Modifier.padding(innerPadding))
                }
            }
        }
    }
}

@Composable
fun CounterApp(modifier: Modifier = Modifier) {
    var counter by remember { mutableIntStateOf(0) }
    var addRandomNumber by remember { mutableStateOf(Random.nextInt(1, 10)) }
    var subtractRandomNumber by remember { mutableStateOf(Random.nextInt(1, 10)) }
    var multiplyRandomNumber by remember { mutableStateOf(Random.nextInt(1, 10)) }
    var divideRandomNumber by remember { mutableStateOf(Random.nextInt(1, 10)) }
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()
    
    val errorMessage = "Ops, parece que ocorreu um erro, vamos recomeçar"
    
    fun updateAllRandomNumbers() {
        addRandomNumber = Random.nextInt(1, 10)
        subtractRandomNumber = Random.nextInt(1, 10)
        multiplyRandomNumber = Random.nextInt(1, 10)
        divideRandomNumber = Random.nextInt(1, 10)
    }
    
    fun performOperation(operation: (Int) -> Int) {
        try {
            counter = operation(counter)
            updateAllRandomNumbers()
        } catch (e: Exception) {
            counter = 0
            updateAllRandomNumbers()
            scope.launch {
                snackbarHostState.showSnackbar(errorMessage)
            }
        }
    }
    
    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { paddingValues ->
        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(paddingValues),
            verticalArrangement = Arrangement.SpaceBetween,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Counter display centered vertically and horizontally
            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = counter.toString(),
                    fontSize = when {
                        counter.toString().length > 6 -> 60.sp
                        counter.toString().length > 4 -> 80.sp
                        else -> 120.sp
                    },
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center,
                    maxLines = 1,
                    modifier = Modifier.padding(horizontal = 16.dp)
                )
            }
            
            // First row of buttons
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 32.dp, vertical = 12.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                // Red button (subtract)
                Button(
                    onClick = { 
                        performOperation { it - subtractRandomNumber }
                    },
                    modifier = Modifier.size(80.dp),
                    shape = CircleShape,
                    colors = ButtonDefaults.buttonColors(containerColor = Color.Red)
                ) {
                    Text(
                        text = "-$subtractRandomNumber",
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
                
                // Green button (add)
                Button(
                    onClick = { 
                        performOperation { it + addRandomNumber }
                    },
                    modifier = Modifier.size(80.dp),
                    shape = CircleShape,
                    colors = ButtonDefaults.buttonColors(containerColor = Color.Green)
                ) {
                    Text(
                        text = "+$addRandomNumber",
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
            
            // Second row of buttons for multiplication and division
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 32.dp, vertical = 12.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                // Blue button (multiply)
                Button(
                    onClick = { 
                        performOperation { it * multiplyRandomNumber }
                    },
                    modifier = Modifier.size(80.dp),
                    shape = CircleShape,
                    colors = ButtonDefaults.buttonColors(containerColor = Color.Blue)
                ) {
                    Text(
                        text = "×$multiplyRandomNumber",
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                }
                
                // Orange button (divide)
                Button(
                    onClick = { 
                        performOperation { 
                            if (divideRandomNumber == 0) throw ArithmeticException("Division by zero")
                            it / divideRandomNumber 
                        }
                    },
                    modifier = Modifier.size(80.dp),
                    shape = CircleShape,
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFF8800))
                ) {
                    Text(
                        text = "÷$divideRandomNumber",
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
            
            // Reset button at the bottom
            Button(
                onClick = { 
                    counter = 0
                    updateAllRandomNumbers()
                },
                modifier = Modifier
                    .padding(bottom = 32.dp, top = 12.dp)
            ) {
                Text(
                    text = "Reset",
                    fontSize = 18.sp
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun CounterAppPreview() {
    CrazyClickTheme {
        CounterApp()
    }
}