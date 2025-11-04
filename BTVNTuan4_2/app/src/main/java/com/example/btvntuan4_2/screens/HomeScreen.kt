@file:OptIn(ExperimentalMaterial3Api::class)

package com.example.btvntuan4_2.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Home
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.btvntuan4_2.R
import com.example.btvntuan4_2.navigation.Screen
import kotlinx.coroutines.launch

@Composable
fun HomeScreen(navController: NavController) {
    var tasks by remember { mutableStateOf<List<Task>>(emptyList()) }
    var loading by remember { mutableStateOf(true) }
    var error by remember { mutableStateOf<String?>(null) }
    val scope = rememberCoroutineScope()

    LaunchedEffect(Unit) {
        loading = true
        scope.launch {
            val res = ApiClient.fetchTasks()
            when {
                res.isSuccess -> {
                    tasks = res.getOrNull() ?: emptyList()
                    loading = false
                }
                res.isFailure -> {
                    loading = false
                    error = res.exceptionOrNull()?.message ?: "Lỗi tải dữ liệu"
                }
            }
        }
    }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            text = "SmartTasks",
                            fontWeight = FontWeight.Bold,
                            fontSize = 20.sp,
                            color = Color(0xFF1976D2),
                            lineHeight = 18.sp
                        )
                        Text(
                            text = "A simple and efficient to-do app",
                            fontSize = 12.sp,
                            color = Color.Gray,
                            lineHeight = 14.sp
                        )
                    }
                },
                navigationIcon = {
                    Box(
                        modifier = Modifier
                            .padding(start = 12.dp)
                            .size(60.dp)
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.uth_logo),
                            contentDescription = "UTH Logo"
                        )
                    }
                },
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = { /* TODO: tạo task */ }) {
                Icon(Icons.Default.Add, contentDescription = "Add Task")
            }
        }
    ) { padding ->
        Box(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
                .background(Color(0xFFF9F9F9))
        ) {
            when {
                loading -> CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
                error != null -> Text(
                    text = error ?: "Lỗi",
                    color = Color.Red,
                    modifier = Modifier.align(Alignment.Center)
                )

                tasks.isEmpty() -> EmptyView()

                else -> {
                    val previewTasks = tasks.take(5) // chỉ lấy 5 task đầu tiên

                    LazyColumn(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(horizontal = 12.dp, vertical = 8.dp)
                    ) {
                        items(previewTasks) { task ->
                            TaskCard(task = task) {
                                navController.navigate(Screen.Detail.createRoute(task.id))
                            }
                        }

                        item {
                            Spacer(modifier = Modifier.height(12.dp))
                            Button(
                                onClick = { navController.navigate(Screen.List.route) },
                                modifier = Modifier
                                    .align(Alignment.Center)
                                    .fillMaxWidth(0.6f)
                            ) {
                                Text("See All")
                            }
                        }
                    }
                }
            }
        }
    }
}


@Composable
private fun EmptyView() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text("No Tasks Yet!", fontWeight = FontWeight.Bold, fontSize = 18.sp)
        Spacer(modifier = Modifier.height(8.dp))
        Text("Stay productive — add something to do", color = Color.Gray)
        Spacer(modifier = Modifier.height(16.dp))
        Button(onClick = { /* TODO: create task */ }) {
            Text("Create Task")
        }
    }
}

@Composable
private fun TaskCard(task: Task, onClick: () -> Unit) {
    val backgroundColor = when (task.status?.lowercase()) {
        "completed" -> Color(0xFFADF199)
        "in progress" -> Color(0xFFF87B7B)
        "pending" -> Color(0xFF7ED2E8)
        else -> Color(0xFFC2C2C2)
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp)
            .clickable { onClick() },
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = backgroundColor),
        elevation = CardDefaults.cardElevation(defaultElevation = 3.dp)
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            Text(
                text = task.title,
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            task.description?.let {
                Text(
                    text = it,
                    style = MaterialTheme.typography.bodySmall,
                    color = Color.Black,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
            }
            Spacer(modifier = Modifier.height(6.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "Status: ${task.status ?: "Unknown"}",
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp,
                    color = Color.Black
                )
                task.dueTime?.let {
                    Text(
                        text = it,
                        fontSize = 12.sp,
                        color = Color.DarkGray
                    )
                }
            }
        }
    }
}


