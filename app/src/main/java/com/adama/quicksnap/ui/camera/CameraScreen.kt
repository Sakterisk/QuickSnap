package com.adama.quicksnap.ui.camera

import android.Manifest
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.camera.core.CameraSelector
import androidx.compose.runtime.*
import androidx.compose.material3.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import com.adama.quicksnap.data.model.User

@Composable
fun CameraScreen(
    friends: List<User>,
    fromUserId: String,
    viewModel: CameraViewModel = viewModel(),
    onSendComplete: () -> Unit
) {
    val context = LocalContext.current
    val photoUri by viewModel.photoUri.collectAsState()
    val isPreviewVisible by viewModel.isPreviewVisible.collectAsState()
    val selectedFriends by viewModel.selectedFriends.collectAsState()
    val isSending by viewModel.isSending.collectAsState()

    var showSendDialog by rememberSaveable { mutableStateOf(false) }

    var hasCameraPermission by remember { mutableStateOf(false) }
    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { granted -> hasCameraPermission = granted }
    LaunchedEffect(Unit) { permissionLauncher.launch(Manifest.permission.CAMERA) }

    if (isPreviewVisible && photoUri != null) {
        PhotoPreview(
            photoUri = photoUri!!,
            onSend = { showSendDialog = true },
            onSave = { com.adama.quicksnap.utils.ImageUtils.saveImageToGallery(context, photoUri!!) },
            onClose = { viewModel.setPhotoUri(null) }
        )
    } else if (hasCameraPermission) {
        var lensFacing by rememberSaveable { mutableStateOf(CameraSelector.LENS_FACING_BACK) }
        CameraPreview(
            lensFacing = lensFacing,
            onSwitchCamera = {
                lensFacing = if (lensFacing == CameraSelector.LENS_FACING_BACK)
                    CameraSelector.LENS_FACING_FRONT
                else
                    CameraSelector.LENS_FACING_BACK
            },
            onPhotoCaptured = { uri, type -> viewModel.setPhotoUri(uri, type) },
            viewModel = viewModel
        )
    }

    if (showSendDialog && photoUri != null) {
        SendToFriendsDialog(
            friends = friends,
            selectedFriends = selectedFriends,
            onSelectionChange = { viewModel.setSelectedFriends(it) },
            onSend = {
                viewModel.sendSnap(context, fromUserId, selectedFriends) {
                    showSendDialog = false
                    onSendComplete()
                }
            },
            onDismiss = { showSendDialog = false }
        )
    }

    if (isSending) {
        // Optional: show a loading dialog or progress indicator
        CircularProgressIndicator()
    }
}
