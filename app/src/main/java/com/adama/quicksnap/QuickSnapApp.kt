package com.adama.quicksnap

import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.navigation.compose.rememberNavController
import com.adama.quicksnap.ui.navigation.QuickSnapNavGraph
import com.adama.quicksnap.ui.auth.AuthViewModel
import com.adama.quicksnap.ui.navigation.BottomNavBar

@Composable
fun QuickSnapApp(isLoggedIn: Boolean, authViewModel: AuthViewModel) {
    val navController = rememberNavController()
    Scaffold(
        bottomBar = {
            BottomNavBar(navController = navController, isLoggedIn = isLoggedIn)
        }
    ) {
        QuickSnapNavGraph(
            navController = navController,
            isLoggedIn = isLoggedIn,
            authViewModel = authViewModel
        )
    }
}
