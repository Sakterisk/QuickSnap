package com.adama.quicksnap.ui.contacts

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.material3.HorizontalDivider
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ContactsScreen(viewModel: ContactsViewModel = viewModel()) {
    val friends by viewModel.friends.collectAsState()
    val friendsWithSnaps by viewModel.friendsWithSnaps.collectAsState()
    val receivedSnaps by viewModel.receivedSnaps.collectAsState()
    var showAddSheet by rememberSaveable { mutableStateOf(false) }
    var showSnapPreview by rememberSaveable { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        viewModel.loadAddableUsers()
        viewModel.loadFriends()
        viewModel.loadPendingRequests()
        viewModel.loadFriendsWithSnaps()
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
                            headlineContent = {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Text(friend.username)
                                    if (friendsWithSnaps.contains(friend.uid)) {
                                        Spacer(Modifier.width(8.dp))
                                        Box(
                                            Modifier
                                                .size(10.dp)
                                                .background(
                                                    color = MaterialTheme.colorScheme.error,
                                                    shape = CircleShape
                                                )
                                        )
                                    }
                                }
                            },
                            modifier = Modifier.clickable {
                                viewModel.loadSnapsFromFriend(friend.uid)
                                showSnapPreview = true
                            }
                        )
                        HorizontalDivider()
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
        if (showSnapPreview && receivedSnaps.isNotEmpty()) {
            SnapPreviewDialog(
                snaps = receivedSnaps,
                onClose = {
                    showSnapPreview = false
                    viewModel.loadFriendsWithSnaps()
                },
                onViewed = { snapIds ->
                    viewModel.deleteSnaps(snapIds)
                }
            )
        }
    }
}
