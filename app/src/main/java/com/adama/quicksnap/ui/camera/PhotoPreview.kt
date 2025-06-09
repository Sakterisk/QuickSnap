package com.adama.quicksnap.ui.camera

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Send
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import com.adama.quicksnap.R

@Composable
fun PhotoPreview(
    photoUri: String,
    onSend: () -> Unit,
    onSave: () -> Unit,
    onClose: () -> Unit
) {
    Box(
        Modifier
            .fillMaxSize()
            .background(Color.Black)
    ) {
        Image(
            painter = rememberAsyncImagePainter(photoUri),
            contentDescription = stringResource(R.string.captured_photo),
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.Center)
        )
        IconButton(
            onClick = onClose,
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(16.dp)
                .background(Color.Black.copy(alpha = 0.5f), CircleShape)
        ) {
            Icon(Icons.Default.Close, contentDescription = stringResource(R.string.close), tint = Color.White)
        }
        Row(
            Modifier
                .align(Alignment.BottomCenter)
                .padding(32.dp),
            horizontalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            Button(onClick = onSave) {
                Text(stringResource(R.string.save_to_gallery))
            }
            Button(onClick = onSend) {
                Icon(
                    Icons.Default.Send,
                    contentDescription = stringResource(R.string.send)
                )
            }
        }
    }
}
