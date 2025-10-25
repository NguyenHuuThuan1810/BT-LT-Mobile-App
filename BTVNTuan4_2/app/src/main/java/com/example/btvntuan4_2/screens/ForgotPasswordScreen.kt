package com.example.btvntuan4_2.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.btvntuan4_2.R
import com.example.btvntuan4_2.navigation.Screen

@Composable
fun ForgotPasswordScreen(navController: NavController) {
    var email by remember { mutableStateOf(TextFieldValue("")) }
    var isEmailValid by remember { mutableStateOf(true) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(Modifier.height(50.dp))

        // Logo UTH
        Image(
            painter = painterResource(id = R.drawable.uth_logo),
            contentDescription = "UTH Logo",
            modifier = Modifier.height(80.dp)
        )

        Spacer(Modifier.height(16.dp))

        Text(
            text = "SmartTasks",
            fontSize = 24.sp,
            color = Color(0xFF1976D2),
            style = MaterialTheme.typography.headlineSmall
        )

        Spacer(Modifier.height(24.dp))

        Text(
            text = "Forget Password?",
            fontSize = 20.sp,
            style = MaterialTheme.typography.titleMedium
        )

        Spacer(Modifier.height(8.dp))

        Text(
            text = "Enter your Email, we will send you a verification code.",
            fontSize = 14.sp,
            color = Color.Gray,
            textAlign = TextAlign.Center,
            modifier = Modifier.align(Alignment.CenterHorizontally)
        )

        Spacer(Modifier.height(24.dp))

        OutlinedTextField(
            value = email,
            onValueChange = {
                email = it
                isEmailValid = validateEmail(it.text)
            },
            label = { Text("Your Email") },
            leadingIcon = {
                Icon(imageVector = Icons.Default.Email, contentDescription = "Email Icon")
            },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
            modifier = Modifier.fillMaxWidth(),
            isError = !isEmailValid
        )

        // Thông báo lỗi nếu email không hợp lệ
        if (!isEmailValid) {
            Text(
                text = "Email không hợp lệ",
                color = Color.Red,
                fontSize = 12.sp,
                modifier = Modifier
                    .align(Alignment.Start)
                    .padding(top = 4.dp)
            )
        }

        Spacer(Modifier.height(24.dp))

        Button(
            onClick = {
                if (email.text.isNotBlank() && validateEmail(email.text)) {
                    navController.navigate(Screen.Verify.createRoute(email.text))
                } else {
                    isEmailValid = false
                }
            },
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF1976D2))
        ) {
            Text("Next", color = Color.White)
        }
    }
}

// Hàm để kiểm tra email hợp lệ
fun validateEmail(email: String): Boolean {
    val atIndex = email.indexOf("@")
    val dotIndex = email.lastIndexOf(".")
    val prefix = email.substringBefore("@")

    return email.contains("@") &&
            email.contains(".") &&
            atIndex > 3 &&
            dotIndex > atIndex + 1 &&
            prefix.length >= 4
}
