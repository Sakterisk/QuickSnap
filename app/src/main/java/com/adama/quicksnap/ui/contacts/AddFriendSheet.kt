package com.adama.quicksnap.ui.contacts

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
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
        Column(Modifier.padding(16.dp)) {
            Text("Friend Requests", style = MaterialTheme.typography.titleMedium)
            if (pendingRequests.isEmpty()) {
                Text("No pending requests")
            } else {
                pendingRequests.forEach { request ->
                    Row(
                        Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(request.username, Modifier.weight(1f)) // Replace with username if available
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
            }

            Spacer(Modifier.height(16.dp))
            Divider()
            Spacer(Modifier.height(16.dp))

            Text("Add New Friend", style = MaterialTheme.typography.titleMedium)
            addableUsers.forEach { user ->
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
        }
    }
}
