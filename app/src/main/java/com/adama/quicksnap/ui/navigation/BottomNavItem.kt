package com.adama.quicksnap.ui.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Login
import androidx.compose.material.icons.filled.Person
import androidx.compose.ui.graphics.vector.ImageVector

data class BottomNavItem(
    val route: String,
    val label: String,
    val icon: ImageVector
)

// Helper to get correct icon for login/account
fun getUserNavItem(isLoggedIn: Boolean): BottomNavItem =
    if (isLoggedIn)
        BottomNavItem("account", "Account", Icons.Filled.Person)
    else
        BottomNavItem("login", "Login", Icons.Filled.Login)
