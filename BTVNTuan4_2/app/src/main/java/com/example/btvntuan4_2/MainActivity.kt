package com.example.btvntuan4_2

import android.os.Bundle

import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.*
import androidx.navigation.compose.rememberNavController
import com.example.btvntuan4_2.navigation.NavGraph
import com.example.btvntuan4_2.ui.theme.BTVNTuan4_2Theme


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            BTVNTuan4_2Theme {
                val navController = rememberNavController()
                Surface {
                    NavGraph(navController = navController)
                }
            }
        }
    }
}
