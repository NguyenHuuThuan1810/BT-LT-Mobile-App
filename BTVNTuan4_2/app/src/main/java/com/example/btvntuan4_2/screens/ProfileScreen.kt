package com.example.btvntuan4_2.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.example.btvntuan4_2.navigation.Screen
import com.google.firebase.auth.FirebaseAuth
import com.example.btvntuan4_2.R
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions

@Composable
fun ProfileScreen(navController: NavController) {
    val user = FirebaseAuth.getInstance().currentUser
    val context = LocalContext.current

    val googleSignInClient = remember {
        GoogleSignIn.getClient(
            context,
            GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build()
        )
    }

    LaunchedEffect(user) {
        if (user == null) {
            navController.navigate(Screen.Login.route) {
                popUpTo(Screen.Profile.route) { inclusive = true }
            }
        }
    }

    @Composable
    fun ProfileField(label: String, value: String, icon: ImageVector, hasDropdown: Boolean = false) {
        Column(modifier = Modifier.fillMaxWidth()) {
            Text(label, fontSize = 14.sp, fontWeight = FontWeight.Bold, color = Color.DarkGray)
            Spacer(modifier = Modifier.height(4.dp))
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .border(1.dp, Color.LightGray)
                    .padding(horizontal = 12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = Color.Gray,
                    modifier = Modifier.size(20.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(value, fontSize = 16.sp, color = Color.Black)
                Spacer(modifier = Modifier.weight(1f))
                if (hasDropdown) {
                    Icon(
                        imageVector = Icons.Default.ArrowDropDown,
                        contentDescription = "Dropdown",
                        tint = Color.Gray,
                        modifier = Modifier.size(20.dp)
                    )
                }
            }
        }
    }



    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Header
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 16.dp),
            contentAlignment = Alignment.Center
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .wrapContentWidth()
                        .background(Color(0xFF1976D2), shape = RoundedCornerShape(12.dp))
                        .padding(3.dp),
                    contentAlignment = Alignment.CenterStart
                ) {
                    IconButton(onClick = {  }) {
                        Icon(Icons.Default.ArrowBack,
                            contentDescription = "Back", tint = Color.White)
                    }
                }
            }

            Text(
                text = "Profile",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF1976D2)
            )
        }



        Spacer(modifier = Modifier.height(32.dp))

        // Ảnh đại diện
        Box(
            modifier = Modifier.size(120.dp),
            contentAlignment = Alignment.BottomEnd
        ) {
            Image(
                painter = rememberAsyncImagePainter(model = user?.photoUrl ?: R.drawable.uth_logo),
                contentDescription = "User photo",
                modifier = Modifier
                    .size(120.dp)
                    .clip(CircleShape),
                contentScale = ContentScale.Crop
            )
            Icon(
                imageVector = Icons.Default.Camera,
                contentDescription = "Change photo",
                tint = Color.Gray,
                modifier = Modifier
                    .size(28.dp)
                    .offset(x = (-4).dp, y = (-4).dp)
            )

        }

        Spacer(modifier = Modifier.height(32.dp))

        // Trường thông tin
        ProfileField(label = "Name", value = user?.displayName ?: "Unknown", icon = Icons.Default.Person)
        Spacer(modifier = Modifier.height(16.dp))
        ProfileField(label = "Email", value = user?.email ?: "Unknown", icon = Icons.Default.Email)
        Spacer(modifier = Modifier.height(16.dp))
        ProfileField(label = "Date of Birth", value = "Unknown", icon = Icons.Default.DateRange, hasDropdown = true)


        Spacer(modifier = Modifier.weight(1f))

        // Nút quay lại
        Button(
            onClick = {
                FirebaseAuth.getInstance().signOut()
                googleSignInClient.signOut()
                googleSignInClient.revokeAccess().addOnCompleteListener {
                    navController.navigate(Screen.Login.route) {
                        popUpTo(Screen.Profile.route) { inclusive = true }
                    }
                }
            },
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF1976D2)),
            shape = RoundedCornerShape(8.dp),
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp)
        ) {
            Text("Back", color = Color.White, fontWeight = FontWeight.Bold)
        }
    }
}

