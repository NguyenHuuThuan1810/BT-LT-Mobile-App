package com.example.btvntuan4_2.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.btvntuan4_2.R
import com.example.btvntuan4_2.navigation.Screen


@Composable
fun VerifyCodeScreen(navController: NavController, email: String) {
    val codeLength = 6
    val code = remember { mutableStateListOf("", "", "", "", "", "") }
    val focusRequesters = List(codeLength) { remember { FocusRequester() } }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
    ) {
        // Nút Back bo góc
        Box(
            modifier = Modifier
                .wrapContentWidth()
                .background(Color(0xFF1976D2), shape = RoundedCornerShape(12.dp))
                .padding(3.dp),
            contentAlignment = Alignment.CenterStart
        ) {
            IconButton(onClick = { navController.popBackStack() }) {
                Icon(Icons.Default.ArrowBack,
                    contentDescription = "Back", tint = Color.White)
            }
        }

        Spacer(Modifier.height(16.dp))

        Image(
            painter = painterResource(id = R.drawable.uth_logo),
            contentDescription = "UTH Logo",
            modifier = Modifier
                .height(80.dp)
                .align(Alignment.CenterHorizontally)
        )

        Spacer(Modifier.height(16.dp))

        Text(
            text = "SmartTasks",
            fontSize = 24.sp,
            color = Color(0xFF1976D2),
            style = MaterialTheme.typography.headlineSmall,
            modifier = Modifier.align(Alignment.CenterHorizontally)
        )

        Spacer(Modifier.height(24.dp))

        Text(
            text = "Verify Code",
            fontSize = 20.sp,
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier.align(Alignment.CenterHorizontally)
        )

        Spacer(Modifier.height(8.dp))

        Text(
            text = "Enter the code we just sent you on your registered Email.",
            fontSize = 14.sp,
            color = Color.Gray,
            textAlign = TextAlign.Center,
            modifier = Modifier.align(Alignment.CenterHorizontally)
        )

        Spacer(Modifier.height(24.dp))

        // 6 ô nhập mã xác thực
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            for (i in 0 until codeLength) {
                OutlinedTextField(
                    value = code[i],
                    onValueChange = { input ->
                        if (input.length <= 1 && input.all { it.isDigit() }) {
                            code[i] = input
                            if (input.isNotEmpty() && i < codeLength - 1) {
                                focusRequesters[i + 1].requestFocus()
                            }
                        } else if (input.isEmpty() && code[i].isNotEmpty() && i > 0) {
                            code[i] = ""
                            focusRequesters[i - 1].requestFocus()
                        }
                    },
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    modifier = Modifier
                        .width(48.dp)
                        .height(56.dp)
                        .focusRequester(focusRequesters[i])
                )
            }
        }

        Spacer(Modifier.height(24.dp))

        Button(
            onClick = {
                val fullCode = code.joinToString("")
                if (fullCode.length == codeLength) {
                    navController.navigate(Screen.Reset.createRoute(email, fullCode))
                }
            },
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF1976D2))
        ) {
            Text("Next", color = Color.White)
        }
    }
}






