package com.adama.quicksnap.ui.contacts

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.material3.HorizontalDivider
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddFriendSheet(
    viewModel: ContactsViewModel,
    onDismiss: () -> Unit
) {
    val pendingRequests by viewModel.pendingRequests.collectAsState()
    val addableUsers by viewModel.addableUsers.collectAsState()
    val scope = rememberCoroutineScope()

    LaunchedEffect(Unit) {
        viewModel.loadPendingRequests()
        viewModel.loadAddableUsers()
    }

    ModalBottomSheet(onDismissRequest = onDismiss) {
        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .heightIn(max = 500.dp)
                .padding(horizontal = 16.dp)
        ) {
            item {
                Text("Friend Requests", style = MaterialTheme.typography.titleMedium, modifier = Modifier.padding(top = 16.dp))
                if (pendingRequests.isEmpty()) {
                    Text("No pending requests", modifier = Modifier.padding(vertical = 8.dp))
                }
            }
            items(pendingRequests) { request ->
                Row(
                    Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(request.username, Modifier.weight(1f))
                    Button(
                        onClick = {
                            scope.launch {
                                viewModel.acceptFriendRequest(request.uid) { _, _ -> }
                            }
                        },
                        modifier = Modifier.padding(end = 4.dp)
                    ) { Text("Accept") }
                    OutlinedButton(
                        onClick = {
                            scope.launch {
                                viewModel.declineFriendRequest(request.uid) { _, _ -> }
                            }
                        }
                    ) { Text("Decline") }
                }
            }
            item {
                Spacer(Modifier.height(16.dp))
                HorizontalDivider()
                Spacer(Modifier.height(16.dp))
                Text("Add New Friend", style = MaterialTheme.typography.titleMedium)
            }
            items(addableUsers) { user ->
                Row(
                    Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(user.username, Modifier.weight(1f))
                    Button(
                        onClick = {
                            scope.launch {
                                viewModel.sendFriendRequest(user.uid) { _, _ -> }
                            }
                        }
                    ) { Text("Add") }
                }
            }
            item { Spacer(Modifier.height(16.dp)) }
        }
    }
}
