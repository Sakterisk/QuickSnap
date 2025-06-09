package com.adama.quicksnap.ui.contacts

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ContactsScreen(viewModel: ContactsViewModel = viewModel()) {
    val friends by viewModel.friends.collectAsState()
    var showAddSheet by rememberSaveable { mutableStateOf(false) }
    LaunchedEffect(Unit) {
        viewModel.loadAddableUsers()
        viewModel.loadFriends()
        viewModel.loadPendingRequests()
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Contacts") },
                actions = {
                    IconButton(onClick = { showAddSheet = true }) {
                        Icon(Icons.Default.Add, contentDescription = "Add Friend")
                    }
                }
            )
        }
    ) { padding ->
        Box(Modifier.padding(padding)) {
            if (friends.isEmpty()) {
                Text("No friends yet.", Modifier.align(Alignment.Center))
            } else {
                LazyColumn {
                    items(friends) { friend ->
                        ListItem(
                            headlineContent = { Text(friend.username) }
                        )
                        Divider()
                    }
                }
            }
        }
        if (showAddSheet) {
            AddFriendSheet(
                viewModel = viewModel,
                onDismiss = { showAddSheet = false }
            )
        }
    }
}

