package com.adama.quicksnap.ui.camera

import android.content.Context
import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.adama.quicksnap.data.model.User
import com.adama.quicksnap.data.repository.SnapRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import androidx.core.net.toUri

class CameraViewModel(
    private val snapRepository: SnapRepository = SnapRepository()
) : ViewModel() {
    private val _photoUri = MutableStateFlow<String?>(null)
    val photoUri: StateFlow<String?> = _photoUri

    private val _isPreviewVisible = MutableStateFlow(false)
    val isPreviewVisible: StateFlow<Boolean> = _isPreviewVisible

    private val _selectedFriends = MutableStateFlow<List<String>>(emptyList())
    val selectedFriends: StateFlow<List<String>> = _selectedFriends

    private val _isSending = MutableStateFlow(false)
    val isSending: StateFlow<Boolean> = _isSending

    private val _type = MutableStateFlow("file")
    val type: StateFlow<String> = _type

    fun setPhotoUri(uri: String?, type: String = "file") {
        _photoUri.value = uri
        _isPreviewVisible.value = uri != null
        _type.value = type
    }

    fun setSelectedFriends(friends: List<String>) {
        _selectedFriends.value = friends
    }

    fun sendSnap(context: Context, fromUserId: String, friends: List<String>, onComplete: () -> Unit) {
        val uri = _photoUri.value ?: return
        _isSending.value = true
        viewModelScope.launch {
            snapRepository.uploadImageAndSendSnaps(context, uri.toUri(), fromUserId, friends, _type.value)
            _isSending.value = false
            _photoUri.value = null
            _isPreviewVisible.value = false
            _selectedFriends.value = emptyList()
            onComplete()
        }
    }
}
