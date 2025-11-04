@file:OptIn(ExperimentalMaterial3Api::class)
package com.example.btvntuan4_2.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.AttachFile
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material.icons.filled.GridView
import androidx.compose.material.icons.filled.List
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import kotlinx.coroutines.launch

@Composable
fun DetailScreen(navController: NavController, taskId: String) {
    var task by remember { mutableStateOf<Task?>(null) }
    var loading by remember { mutableStateOf(true) }
    var error by remember { mutableStateOf<String?>(null) }
    val scope = rememberCoroutineScope()

    LaunchedEffect(taskId) {
        loading = true
        scope.launch {
            val res = ApiClient.fetchTask(taskId)
            if (res.isSuccess) {
                task = res.getOrNull()
                loading = false
            } else {
                error = res.exceptionOrNull()?.message ?: "Lỗi tải chi tiết"
                loading = false
            }
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                        Text(
                            text = "Detail",
                            color = Color(0xFF1976D2),
                            fontWeight = FontWeight.Bold,
                            fontSize = 18.sp
                        )
                    }
                },
                navigationIcon = {
                    Box(
                        modifier = Modifier
                            .padding(start = 12.dp)
                            .size(36.dp)
                            .background(Color(0xFF299AEF), shape = RoundedCornerShape(8.dp))
                            .clickable { navController.popBackStack() },
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back",
                            tint = Color(0xFFFFFFFF)
                        )
                    }
                },
                actions = {
                    IconButton(onClick = {
                        scope.launch {
                            val res = ApiClient.deleteTask(taskId)
                            if (res.isSuccess) navController.popBackStack()
                            else error = res.exceptionOrNull()?.message
                        }
                    }) {
                        Icon(
                            imageVector = Icons.Default.Delete,
                            contentDescription = "Delete",
                            tint = Color(0xFFFF9800)
                        )
                    }
                },
            )
        }
    ) { padding ->
        Box(modifier = Modifier
            .padding(padding)
            .fillMaxSize()
        ) {
            when {
                loading -> CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
                error != null -> Text(error ?: "Lỗi", color = Color.Red, modifier = Modifier.align(Alignment.Center))
                else -> task?.let { t ->
                    Column(modifier = Modifier
                        .fillMaxSize()
                        .verticalScroll(rememberScrollState())
                        .padding(16.dp)
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(bottom = 12.dp)
                        ) {
                            Text(
                                text = t.title,
                                fontWeight = FontWeight.Bold,
                                fontSize = 18.sp
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = t.description ?: "-",
                                color = Color.DarkGray,
                                fontSize = 14.sp

                            )
                        }

                        Card(
                            colors = CardDefaults.cardColors(containerColor = Color(0xFFFFEBEE)),
                            shape = RoundedCornerShape(12.dp),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(16.dp),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                InfoItem("Category", t.category ?: "-", Icons.Default.GridView)
                                InfoItem("Status", t.status ?: "-", Icons.Default.List)
                                InfoItem("Priority", t.priority ?: "-", Icons.Default.EmojiEvents)
                            }
                        }


                        Spacer(modifier = Modifier.height(20.dp))

                        // Subtasks
                        Text("Subtasks", fontWeight = FontWeight.Bold, fontSize = 16.sp)
                        Spacer(modifier = Modifier.height(8.dp))
                        val subs = t.subtasks ?: emptyList()
                        if (subs.isEmpty()) {
                            Text("No subtasks", color = Color.Gray)
                        } else {
                            subs.forEach { st ->
                                Card(
                                    shape = RoundedCornerShape(10.dp),
                                    colors = CardDefaults.cardColors(containerColor = Color(0xFFF5F5F5)),
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(vertical = 4.dp)
                                ) {
                                    Row(
                                        verticalAlignment = Alignment.CenterVertically,
                                        modifier = Modifier.padding(12.dp)
                                    ) {
                                        Checkbox(
                                            checked = st.isCompleted,
                                            onCheckedChange = null,
                                            colors = CheckboxDefaults.colors(checkedColor = Color.Black)
                                        )
                                        Spacer(modifier = Modifier.width(8.dp))
                                        Text(st.title, color = Color.DarkGray)
                                    }
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(20.dp))

                        // Attachments
                        val attaches = t.attachments ?: emptyList()
                        if (attaches.isNotEmpty()) {
                            Text("Attachments", fontWeight = FontWeight.Bold, fontSize = 16.sp)
                            Spacer(modifier = Modifier.height(8.dp))
                            attaches.forEach { a ->
                                Card(
                                    shape = RoundedCornerShape(10.dp),
                                    colors = CardDefaults.cardColors(containerColor = Color(0xFFF5F5F5)),
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(vertical = 4.dp)
                                ) {
                                    Row(
                                        verticalAlignment = Alignment.CenterVertically,
                                        modifier = Modifier
                                            .padding(12.dp)
                                            .clickable { /* TODO: open file */ }
                                    ) {
                                        Icon(Icons.Default.AttachFile, contentDescription = null)
                                        Spacer(modifier = Modifier.width(8.dp))
                                        Text(a.fileName, color = Color.DarkGray)
                                    }
                                }
                            }
                        }
                    }
                } ?: Text("Task not found", modifier = Modifier.align(Alignment.Center))
            }
        }
    }
}

@Composable
fun InfoItem(label: String, value: String, icon: ImageVector) {
    Column(
        modifier = Modifier.padding(4.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
                icon,
                contentDescription = null,
                tint = Color.Black,
                modifier = Modifier.size(25.dp)
            )
            Spacer(modifier = Modifier.width(6.dp))
            Column {
                Text(label, fontSize = 12.sp, color = Color.Black, lineHeight = 16.sp)
                Text(value, fontSize = 14.sp, fontWeight = FontWeight.Medium,lineHeight = 18.sp)
            }
        }
    }
}



