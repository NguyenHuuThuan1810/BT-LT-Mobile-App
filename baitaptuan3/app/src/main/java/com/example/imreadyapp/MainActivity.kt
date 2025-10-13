package com.example.imreadyapp

import android.icu.text.CaseMap
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.withStyle
import coil.compose.rememberAsyncImagePainter
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.draw.clip
import androidx.compose.foundation.background







import com.example.imreadyapp.ui.theme.ImReadyAppTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ImReadyAppTheme {
                Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background){
                    AppNavigator()
                }
            }
        }
    }
}
@Composable
fun AppNavigator() {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = "Welcome") {
        composable("Welcome") { WelcomeScreen(navController) }
        composable("Components") { ComponentsListScreen(navController) }
        composable("text_detail") { TextDetailScreen(navController) }
        composable("image_detail") { ImageDetailScreen(navController) }
        composable("textfield_detail") { TextFieldScreen(navController) }
        composable("row_detail") { RowLayoutScreen(navController) }

    }
}

@Composable
fun WelcomeScreen(navController: NavController) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Spacer(modifier = Modifier.weight(1f))

        // Logo
        Image(
            painter = painterResource(id = R.drawable.compose_logo),
            contentDescription = "Jetpack Compose Logo",
            modifier = Modifier.size(120.dp)
        )

        Spacer(modifier = Modifier.height(24.dp))
        Text(
            text = "Jetpack Compose",
            fontSize = 22.sp,
            fontWeight = FontWeight.Bold,
            color = Color.Black
        )

        Spacer(modifier = Modifier.height(12.dp))
        Text(
            text = "Jetpack Compose is a modern UI toolkit for building native Android applications using a declarative programming approach.",
            fontSize = 14.sp,
            color = Color.Gray,
            lineHeight = 20.sp,
            textAlign = TextAlign.Center,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp)

        )

        Spacer(modifier = Modifier.weight(1f))
        Button(
            onClick = { navController.navigate("Components") },
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2196F3)),
            shape = MaterialTheme.shapes.large,
            modifier = Modifier
                .fillMaxWidth(0.6f)
                .height(50.dp)
        ) {
            Text(
                text = "I'm ready",
                color = Color.White,
                fontSize = 16.sp,
                fontWeight = FontWeight.SemiBold
            )
        }

        Spacer(modifier = Modifier.height(40.dp))
    }
}

data class ComponentCategory(val title: String,val items: List<ComponentItem>)
data class ComponentItem(val name: String, val description: String, val route: String)

@Composable
fun ComponentsListScreen(navController: NavController) {
    val categories = listOf(
        ComponentCategory(
            "Display", listOf(
                ComponentItem("Text", "Displays text", "text_detail"),
                ComponentItem("Image", "Displays an image", "image_detail")
            )
        ),
        ComponentCategory(
            "Input", listOf(
                ComponentItem("TextField", "Input field for text", "textfield_detail"),
                ComponentItem("PasswordField", "Input field for passwords", "")
            )
        ),
        ComponentCategory(
            "Layout", listOf(
                ComponentItem("Column", "Arranges elements vertically", ""),
                ComponentItem("Row", "Arranges elements horizontally", "row_detail")
            )
        )
    )

    Column(
        modifier = Modifier.fillMaxSize().padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            "UI Components List",
            fontSize = 20.sp,
            color = Color(0xFF64B5F6),
            fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.height(16.dp))
        LazyColumn {
            categories.forEach { category ->
                item {

                    Text(
                        text = category.title,
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp,
                        color = Color.Black,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 8.dp)
                    )
                }
                items(category.items) { item ->
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 6.dp)
                            .clickable {
                                if (item.route.isNotEmpty())
                                    navController.navigate(item.route)
                            },
                        colors = CardDefaults.cardColors(containerColor = Color(0xFFBBDEFB))
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Text(item.name, fontWeight = FontWeight.Bold)
                            Text(item.description, fontSize = 13.sp, color = Color.DarkGray)
                        }
                    }
                }
            }
        }
    }
}
//Màn hình chi tiết Text
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TextDetailScreen(navController: NavController) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        "Text Detail",
                        color = Color(0xFF64B5F6),
                        modifier = Modifier.fillMaxWidth(),
                        textAlign = TextAlign.Center
                    )
                },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Text("<", fontSize = 20.sp, color = Color(0xFF64B5F6), modifier = Modifier.padding(start = 4.dp))
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.White
                )
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(24.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {


            Row(verticalAlignment = Alignment.CenterVertically) {
                Text("The ", fontSize = 25.sp)
                Text(
                    "quick ",
                    fontSize = 25.sp,
                    color = Color.Black,
                    textDecoration = TextDecoration.LineThrough
                )


                Text(
                    buildAnnotatedString {
                        withStyle(
                            style = SpanStyle(
                                fontSize = 35.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFFB87333)
                            )
                        ) {
                            append("B")
                        }
                        withStyle(
                            style = SpanStyle(
                                fontSize = 25.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFFB87333)
                            )
                        ) {
                            append("rown")
                        }
                    }
                )
            }


            Row(verticalAlignment = Alignment.CenterVertically) {
                Text("fox j u m p s ", fontSize = 20.sp)
                Text(
                    "over",
                    fontSize = 25.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black
                )
            }


            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    "the ",
                    fontSize = 25.sp,
                    textDecoration = TextDecoration.Underline
                )
                Text(
                    "lazy ",
                    fontSize = 20.sp,
                    fontStyle = FontStyle.Italic,
                    color = Color.Black,
                )
                Text("dog.", fontSize = 20.sp)
            }


            Divider(
                color = Color.LightGray,
                thickness = 1.dp,
                modifier = Modifier.padding(top = 12.dp)
            )
        }
    }
}
//Màn hình ảnh
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ImageDetailScreen(navController: NavController) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        "Image Detail",
                        color = Color(0xFF64B5F6),
                        modifier = Modifier.fillMaxWidth(),
                        textAlign = TextAlign.Center
                    )
                },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Text("<", fontSize = 20.sp, color = Color(0xFF64B5F6))
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.White)
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top
        ) {
            val img1 = "https://developer.android.com/images/brand/Android_Robot.png"
            Image(
                painter = rememberAsyncImagePainter(img1),
                contentDescription = "Android Image",
                modifier = Modifier
                    .size(200.dp)
                    .padding(8.dp)
            )
            Text(
                img1,
                fontSize = 14.sp,
                color = Color.Gray,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(bottom = 16.dp)
            )

            val img2 = "https://upload.wikimedia.org/wikipedia/commons/7/74/Jetpack_Compose_Logo.png"
            Image(
                painter = rememberAsyncImagePainter(img2),
                contentDescription = "Compose Logo",
                modifier = Modifier
                    .size(200.dp)
                    .padding(8.dp)
            )
//            Text(
//                img2,
//                fontSize = 14.sp,
//                color = Color.Gray,
//                textAlign = TextAlign.Center,
//                modifier = Modifier.padding(bottom = 16.dp)
//            )

            Spacer(modifier = Modifier.height(8.dp))
            Text(
                "In app",
                fontSize = 16.sp,
                fontWeight = FontWeight.Medium,
                color = Color.Black
            )
        }
    }
}
//Màn hình nhập dữ liệu
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TextFieldScreen(navController: NavController) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        "TextField",
                        color = Color(0xFF64B5F6),
                        modifier = Modifier.fillMaxWidth(),
                        textAlign = TextAlign.Center
                    )
                },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Text("<", fontSize = 20.sp, color = Color(0xFF64B5F6))
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.White)
            )
        }
    ) { innerPadding ->
        var textValue by remember { mutableStateOf("") }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
//            Text("Nhập thông tin của bạn", fontSize = 20.sp, fontWeight = FontWeight.Bold)
//            Spacer(modifier = Modifier.height(20.dp))

            OutlinedTextField(
                value = textValue,
                onValueChange = { textValue = it },
                label = { Text("Nhập thông tin") },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(24.dp))
            Text(
                text = "Tự động cập nhật dữ liệu theo TextField",
                fontSize = 16.sp,
                color = Color.Red
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Hiển thị dữ liệu đã nhập
//            Text(
//                text = "Hiển thị thông tin đã lưu:",
//                fontSize = 18.sp,
//                fontWeight = FontWeight.Bold
//            )
//
//            Spacer(modifier = Modifier.height(8.dp))
//
//            Text(
//                text = textValue.ifEmpty { "(Chưa có dữ liệu được nhập)" },
//                fontSize = 16.sp,
//                color = Color(0xFF03A9F4)
//            )
        }
    }
}
//Màn hình hàng (row)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RowLayoutScreen(navController: NavController) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        "Row Layout",
                        color = Color(0xFF64B5F6),
                        modifier = Modifier.fillMaxWidth(),
                        textAlign = TextAlign.Center
                    )
                },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Text("<", fontSize = 20.sp, color = Color(0xFF64B5F6))
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.White)
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 16.dp, vertical = 24.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            repeat(5) {
                Box( // Nền màu xám
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(70.dp)
                        .background(Color(0xFFE0E0E0), shape = RoundedCornerShape(12.dp))
                        .padding(horizontal = 12.dp, vertical = 8.dp)
                ){Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    repeat(3) { index ->
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .aspectRatio(2f)
                                .clip(RoundedCornerShape(12.dp))
                                .background(
                                    color = if (index == 1) Color(0xFF1976D2) else Color(0xFF64B5F6)
                                ),
                            contentAlignment = Alignment.Center
                        ) {

                        }
                    }
                }
                }

            }
        }
    }
}





