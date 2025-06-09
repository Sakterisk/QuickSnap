package com.adama.quicksnap

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.collectAsState
import androidx.lifecycle.viewmodel.compose.viewModel
import com.adama.quicksnap.ui.auth.AuthViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val authViewModel: AuthViewModel = AuthViewModel()
            val isLoggedIn = authViewModel.isLoggedIn.collectAsState().value
            QuickSnapApp(isLoggedIn = isLoggedIn, authViewModel = authViewModel)
        }
    }
}
