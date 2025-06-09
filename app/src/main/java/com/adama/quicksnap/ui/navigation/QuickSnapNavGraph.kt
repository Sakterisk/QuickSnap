package com.adama.quicksnap.ui.navigation

import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
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
import com.adama.quicksnap.ui.contacts.ContactsViewModel

@Composable
fun QuickSnapNavGraph(
    navController: NavHostController,
    isLoggedIn: Boolean,
    authViewModel: AuthViewModel,
    modifier: Modifier
) {
    val startDestination = if (isLoggedIn) "camera" else "login"
    val contactsViewModel: ContactsViewModel = ContactsViewModel()

    NavHost(
        navController = navController,
        startDestination = startDestination,
        modifier = modifier
        )
    {
        composable("login") { LoginScreen(navController, authViewModel) }
        composable("register") { RegisterScreen(navController, authViewModel) }
        composable("camera") {
            val friends = contactsViewModel.friends.collectAsState().value
            val authUser = authViewModel.currentUser.collectAsState().value
            if (authUser != null) {
                CameraScreen(
                    friends = friends,
                    fromUserId = authUser.uid
                )
                {}
            } else {
                // Show a loading indicator or placeholder
                CircularProgressIndicator()
            }
        }
        composable("contacts") { ContactsScreen(contactsViewModel) }
        composable("account") { AccountScreen(navController, authViewModel) }
        //composable("chat") { ChatScreen(navController) }
    }
}
