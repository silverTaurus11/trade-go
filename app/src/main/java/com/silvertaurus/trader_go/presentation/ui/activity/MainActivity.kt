package com.silvertaurus.trader_go.presentation.ui.activity

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.navigation.compose.rememberNavController
import com.silvertaurus.trader_go.presentation.navigation.CryptoNavGraph
import com.silvertaurus.trader_go.presentation.ui.theme.CryptoTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            CryptoTheme {
                val navController = rememberNavController()
                CryptoNavGraph(navController)
            }
        }
    }
}