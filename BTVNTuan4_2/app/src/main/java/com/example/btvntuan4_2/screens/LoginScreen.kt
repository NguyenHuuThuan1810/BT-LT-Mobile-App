package com.example.btvntuan4_2.screens

import android.app.Activity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.motionEventSpy
import androidx.compose.ui.layout.ModifierLocalBeyondBoundsLayout
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.btvntuan4_2.R
import com.example.btvntuan4_2.navigation.Screen
import com.google.android.gms.auth.api.signin.*
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.firestore.FirebaseFirestore

@Composable
fun LoginScreen(navController: NavController) {
    val context = LocalContext.current
    val activity = context as Activity
    val auth = FirebaseAuth.getInstance()
    val db = FirebaseFirestore.getInstance()

    val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
        .requestIdToken("82477900183-i537rdk4dmucnhnf2l53qv3hk67mn9nq.apps.googleusercontent.com")
        .requestEmail()
        .build()

    val googleSignInClient = GoogleSignIn.getClient(context, gso)
    var loading by remember { mutableStateOf(false) }

    val launcher = rememberLauncherForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)
        try {
            val account = task.getResult(ApiException::class.java)
            if (account != null) {
                firebaseAuthWithGoogle(account, auth, db, navController) { loading = it }
            } else {
                loading = false
            }
        } catch (e: ApiException) {
            e.printStackTrace()
            loading = false
        }
    }

    LaunchedEffect(Unit) {
        val currentUser = FirebaseAuth.getInstance().currentUser
        if (currentUser != null) {
            navController.navigate(Screen.Profile.route) {
                popUpTo(Screen.Login.route) { inclusive = true }
            }
        }
    }

    LaunchedEffect(Unit) {
        auth.addAuthStateListener { firebaseAuth ->
            val user = firebaseAuth.currentUser
            if (user != null) {
                navController.navigate(Screen.Profile.route) {
                    popUpTo(Screen.Login.route) { inclusive = true }
                }
            }
        }
    }


    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,

    ) {
        Spacer(modifier = Modifier.height(100.dp))
        // Logo UTH
        Image(
            painter = painterResource(id = R.drawable.uth_logo),
            contentDescription = "UTH Logo",
            modifier = Modifier
                .size(150.dp)
        )


        // Tên ứng dụng và tagline
        Text(
            text = "SmartTasks",
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF1976D2)
        )
        Text(
            text = "A simple and efficient to-do app",
            fontSize = 13.sp,
            color = Color(0xFF1976D2),
            modifier = Modifier.padding(top = 4.dp, bottom = 24.dp)
        )
        Spacer(modifier = Modifier.height(80.dp))
        // Lời chào
        Text(
            text = "Welcome",
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            color = Color.DarkGray
        )
        Text(
            text = "Ready to explore? Log in to get started.",
            fontSize = 13.sp,
            color = Color.DarkGray,
        )
        Spacer(modifier = Modifier.height(16.dp))
        // Nút đăng nhập Google có icon
        Button(
            onClick = {
                loading = true
                val signInIntent = googleSignInClient.signInIntent
                launcher.launch(signInIntent)
            },
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF308EEF)),
            shape = RoundedCornerShape(8.dp),
            border = BorderStroke(1.dp, Color(0xFF1976D2)),
            modifier = Modifier
                .height(50.dp)
        ) {
            Icon(
                painter = painterResource(id = R.drawable.logo_gg),
                contentDescription = "Google Icon",
                tint = Color.Unspecified,
                modifier = Modifier.size(24.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = "SIGN IN WITH GOOGLE",
                color = Color.Black,
                fontWeight = FontWeight.Bold
            )
        }

        // Loading indicator
        if (loading) {
            Spacer(modifier = Modifier.height(24.dp))
            CircularProgressIndicator(color = Color(0xFF1976D2))
        }

        // Footer
        Spacer(modifier = Modifier.weight(1f))

        Text(
            text = "© UTHSmartTasks",
            fontSize = 12.sp,
            color = Color.Gray
        )
    }
}



// Cập nhật đúng state bằng callback
fun firebaseAuthWithGoogle(
    account: GoogleSignInAccount,
    auth: FirebaseAuth,
    db: FirebaseFirestore,
    navController: NavController,
    onLoadingChange: (Boolean) -> Unit
) {
    val credential = GoogleAuthProvider.getCredential(account.idToken, null)
    onLoadingChange(true)

    auth.signInWithCredential(credential)
        .addOnCompleteListener { task ->
            if (task.isSuccessful) {
                val user = auth.currentUser
                if (user != null) {
                    val userMap = hashMapOf(
                        "uid" to user.uid,
                        "name" to user.displayName,
                        "email" to user.email,
                        "photoUrl" to user.photoUrl?.toString()
                    )

                    db.collection("users").document(user.uid).set(userMap)
                        .addOnSuccessListener {
                            onLoadingChange(false)
                            navController.navigate(Screen.Profile.route) {
                                popUpTo(Screen.Login.route) { inclusive = true }
                            }
                        }
                        .addOnFailureListener {
                            onLoadingChange(false)
                            it.printStackTrace()
                        }
                } else {
                    onLoadingChange(false)
                }
            } else {
                onLoadingChange(false)
                task.exception?.printStackTrace()
            }
        }
}
