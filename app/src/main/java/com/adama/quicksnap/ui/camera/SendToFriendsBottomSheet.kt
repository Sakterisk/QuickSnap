package com.adama.quicksnap.ui.camera

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.material3.HorizontalDivider
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.adama.quicksnap.R
import com.adama.quicksnap.data.model.User

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SendToFriendsBottomSheet(
    friends: List<User>,
    selectedFriends: List<String>,
    onSelectionChange: (List<String>) -> Unit,
    onSend: () -> Unit,
    onDismiss: () -> Unit
) {
    ModalBottomSheet(
        onDismissRequest = onDismiss
    ) {
        Text(
            stringResource(R.string.select_friends),
            style = MaterialTheme.typography.titleLarge,
            modifier = Modifier.padding(16.dp)
        )
        HorizontalDivider()
        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .heightIn(max = 400.dp)
                .padding(horizontal = 16.dp)
        ) {
            items(friends) { friend ->
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp)
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
                    Text(friend.username, modifier = Modifier.weight(1f))
                }
            }
        }
        Spacer(Modifier.height(16.dp))
        Row(
            Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.End
        ) {
            OutlinedButton(onClick = onDismiss) { Text(stringResource(R.string.cancel)) }
            Spacer(Modifier.width(8.dp))
            Button(onClick = onSend, enabled = selectedFriends.isNotEmpty()) { Text(stringResource(R.string.send)) }
        }
    }
}
