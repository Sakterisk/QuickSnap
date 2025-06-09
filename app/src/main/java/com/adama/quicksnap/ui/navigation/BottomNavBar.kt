package com.adama.quicksnap.ui.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.Groups
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.adama.quicksnap.R

@Composable
fun BottomNavBar(
    navController: NavController,
    isLoggedIn: Boolean
) {
    val navItems = listOf(
        BottomNavItem(stringResource(R.string.contacts), stringResource(R.string.contacts), Icons.Filled.Groups),
        BottomNavItem(stringResource(R.string.camera),
            stringResource(R.string.camera), Icons.Filled.CameraAlt),
        getUserNavItem(isLoggedIn, stringResource(R.string.account), stringResource(R.string.login))
    )
    NavigationBar {
        val navBackStackEntry by navController.currentBackStackEntryAsState()
        val currentRoute = navBackStackEntry?.destination?.route
        navItems.forEach { item ->
            val isItemEnabled = isLoggedIn || item.route == "login"
            NavigationBarItem(
                selected = currentRoute == item.route,
                onClick = {
                    if (currentRoute != item.route) {
                        navController.navigate(item.route) {
                            popUpTo(navController.graph.startDestinationId) { saveState = true }
                            launchSingleTop = true
                            restoreState = true
                        }
                    }
                },
                icon = { Icon(item.icon, contentDescription = item.label) },
                label = { Text(item.label) },
                enabled = isItemEnabled,
            )
        }
    }
}
