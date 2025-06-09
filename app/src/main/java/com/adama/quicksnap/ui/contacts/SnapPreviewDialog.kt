package com.adama.quicksnap.ui.contacts

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import coil.compose.AsyncImage
import com.adama.quicksnap.data.model.Snap

@Composable
fun SnapPreviewDialog(
    snaps: List<Snap>,
    onClose: () -> Unit,
    onViewed: (List<String>) -> Unit
) {
    var currentIndex by rememberSaveable { mutableIntStateOf(0) }
    if (snaps.isNotEmpty()) {
        Dialog(
            onDismissRequest = {
                onViewed(snaps.map { it.id })
                onClose()
            },
            properties = DialogProperties(
                usePlatformDefaultWidth = false,
                decorFitsSystemWindows = false
            )
        ) {
            Surface(modifier = Modifier.fillMaxSize()) {
                Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    AsyncImage(
                        model = snaps[currentIndex].imageUrl,
                        contentDescription = "Snap",
                        modifier = Modifier.fillMaxSize()
                    )
                    Row(
                        Modifier
                            .align(Alignment.BottomCenter)
                            .padding(bottom = 16.dp),
                        horizontalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        if (currentIndex > 0) {
                            OutlinedButton(onClick = { currentIndex-- }) { Text("Back") }
                        }
                        if (currentIndex < snaps.lastIndex) {
                            Button(onClick = { currentIndex++ }) { Text("Next") }
                        } else {
                            Button(onClick = {
                                onViewed(snaps.map { it.id })
                                onClose()
                            }) { Text("Close") }
                        }
                    }
                }
            }
        }
    }
}
