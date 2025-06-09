package com.adama.quicksnap.ui.camera

import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.foundation.layout.*
import androidx.compose.ui.Alignment
import com.adama.quicksnap.data.model.User

@Composable
fun SendToFriendsDialog(
    friends: List<User>,
    selectedFriends: List<String>,
    onSelectionChange: (List<String>) -> Unit,
    onSend: () -> Unit,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Select friends") },
        text = {
            Column {
                friends.forEach { friend ->
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Checkbox(
                            checked = selectedFriends.contains(friend.uid),
                            onCheckedChange = { checked ->
                                val newSelected = if (checked) {
                                    selectedFriends + friend.uid
                                } else {
                                    selectedFriends - friend.uid
                                }
                                onSelectionChange(newSelected)
                            }
                        )
                        Text(friend.username)
                    }
                }
            }
        },
        confirmButton = {
            Button(onClick = onSend) { Text("Send") }
        },
        dismissButton = {
            OutlinedButton(onClick = onDismiss) { Text("Cancel") }
        }
    )
}
