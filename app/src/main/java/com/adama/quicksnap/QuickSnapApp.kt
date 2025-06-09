package com.adama.quicksnap

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
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
    ) { paddingValues ->
        QuickSnapNavGraph(
            navController = navController,
            isLoggedIn = isLoggedIn,
            authViewModel = authViewModel,
            modifier = Modifier.padding(paddingValues)
        )
    }
}
