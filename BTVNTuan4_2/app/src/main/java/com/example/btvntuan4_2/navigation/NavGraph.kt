package com.example.btvntuan4_2.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.btvntuan4_2.screens.*

sealed class Screen(val route: String) {
    object Login : Screen("login")
    object Profile : Screen("profile")

    object Forgot : Screen("forgot")

    object Verify : Screen("verify/{email}") {
        fun createRoute(email: String) = "verify/$email"
    }

    object Reset : Screen("reset/{email}/{code}") {
        fun createRoute(email: String, code: String) = "reset/$email/$code"
    }

    object Confirm : Screen("confirm/{email}/{code}/{password}") {
        fun createRoute(email: String, code: String, password: String) =
            "confirm/$email/$code/$password"
    }
}

@Composable
fun NavGraph(navController: NavHostController) {
    NavHost(navController = navController, startDestination = Screen.Login.route) {
        composable(Screen.Login.route) { LoginScreen(navController) }
        composable(Screen.Profile.route) { ProfileScreen(navController) }
        composable(Screen.Forgot.route) {
            ForgotPasswordScreen(navController)
        }

        composable(Screen.Verify.route) { backStackEntry ->
            val email = backStackEntry.arguments?.getString("email") ?: ""
            VerifyCodeScreen(navController, email)
        }

        composable(Screen.Reset.route) { backStackEntry ->
            val email = backStackEntry.arguments?.getString("email") ?: ""
            val code = backStackEntry.arguments?.getString("code") ?: ""
            ResetPasswordScreen(navController, email, code)
        }

        composable(Screen.Confirm.route) { backStackEntry ->
            val email = backStackEntry.arguments?.getString("email") ?: ""
            val code = backStackEntry.arguments?.getString("code") ?: ""
            val password = backStackEntry.arguments?.getString("password") ?: ""
            ConfirmScreen(email, code, password)
        }
    }
}
