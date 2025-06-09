package com.adama.quicksnap.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
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
    authViewModel: AuthViewModel,
    modifier: Modifier
) {
    val startDestination = if (isLoggedIn) "camera" else "login"


    NavHost(
        navController = navController,
        startDestination = startDestination,
        modifier = modifier
        )
    {
        composable("login") { LoginScreen(navController, authViewModel) }
        composable("register") { RegisterScreen(navController, authViewModel) }
        composable("camera") { CameraScreen(
            onSend = { uri -> /* TODO: handle send/upload */ }
        ) }
        composable("contacts") { ContactsScreen() }
        composable("account") { AccountScreen(navController, authViewModel) }
        //composable("chat") { ChatScreen(navController) }
    }
}
