package com.adama.quicksnap.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.adama.quicksnap.ui.account.AccountScreen
import com.adama.quicksnap.ui.auth.AuthViewModel
import com.adama.quicksnap.ui.auth.LoginScreen
import com.adama.quicksnap.ui.auth.RegisterScreen
import com.adama.quicksnap.ui.camera.CameraScreen
import com.adama.quicksnap.ui.contacts.ContactsScreen

@Composable
fun QuickSnapNavGraph(
    navController: NavHostController,
    isLoggedIn: Boolean,
    authViewModel: AuthViewModel
) {
    val startDestination = if (isLoggedIn) "camera" else "login"

    NavHost(navController = navController, startDestination = startDestination) {
        composable("login") { LoginScreen(navController, authViewModel) }
        composable("register") { RegisterScreen(navController, authViewModel) }
        composable("camera") { CameraScreen(navController) }
        composable("contacts") { ContactsScreen(navController) }
        composable("account") { AccountScreen(navController, authViewModel) }
        //composable("chat") { ChatScreen(navController) }
    }
}
