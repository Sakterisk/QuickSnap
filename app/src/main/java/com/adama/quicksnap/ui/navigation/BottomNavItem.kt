package com.adama.quicksnap.ui.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Login
import androidx.compose.material.icons.filled.Person
import androidx.compose.ui.graphics.vector.ImageVector
import com.adama.quicksnap.R

data class BottomNavItem(
    val route: String,
    val label: String,
    val icon: ImageVector
)

// Helper to get correct icon for login/account
fun getUserNavItem(isLoggedIn: Boolean, account: String, login: String): BottomNavItem {
    if (isLoggedIn)
        return BottomNavItem(account, account, Icons.Filled.Person)
    else
        return BottomNavItem(login, login, Icons.Filled.Login)
}
