package com.adama.quicksnap.ui.contacts

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.adama.quicksnap.data.model.Friend
import com.adama.quicksnap.data.model.FriendRequest
import com.adama.quicksnap.data.model.Snap
import com.adama.quicksnap.data.model.User
import com.adama.quicksnap.data.repository.FriendRepository
import com.adama.quicksnap.data.repository.FriendRequestRepository
import com.adama.quicksnap.data.repository.SnapRepository
import com.adama.quicksnap.data.repository.UserRepository
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class ContactsViewModel(
    private val userRepository: UserRepository = UserRepository(),
    private val friendRepository: FriendRepository = FriendRepository(),
    private val friendRequestRepository: FriendRequestRepository = FriendRequestRepository(),
    private val snapRepository: SnapRepository = SnapRepository()
) : ViewModel() {

    private val _pendingRequests = MutableStateFlow<List<User>>(emptyList())
    val pendingRequests: StateFlow<List<User>> = _pendingRequests

    private val _addableUsers = MutableStateFlow<List<User>>(emptyList())
    val addableUsers: StateFlow<List<User>> = _addableUsers

    private val _friends = MutableStateFlow<List<User>>(emptyList())
    val friends: StateFlow<List<User>> = _friends

    private val _receivedSnaps = MutableStateFlow<List<Snap>>(emptyList())
    val receivedSnaps: StateFlow<List<Snap>> = _receivedSnaps

    private val _friendsWithSnaps = MutableStateFlow<Set<String>>(emptySet())
    val friendsWithSnaps: StateFlow<Set<String>> = _friendsWithSnaps

    private val currentUserId = FirebaseAuth.getInstance().currentUser?.uid

    init {
        loadFriends()
    }

    fun loadFriends() {
        currentUserId?.let { uid ->
            viewModelScope.launch {
                _friends.value = friendRepository.getFriendsForUser(uid).map { friend ->
                    userRepository.getUser(friend.friendId) ?: User(uid = friend.friendId, username = "Unknown")
                }
            }
        }
    }

    fun loadPendingRequests() {
        currentUserId?.let { uid ->
            viewModelScope.launch {
                _pendingRequests.value = friendRequestRepository.getRequestsForUser(uid).map { request ->
                        userRepository.getUser(request.fromUserId) ?: User(uid = request.fromUserId, username = "Unknown")
                    }
            }
        }
    }

    fun loadAddableUsers() {
        currentUserId?.let { uid ->
            viewModelScope.launch {
                val users = userRepository.getAllUsers()
                val friendIds = _friends.value.map { it.uid }
                val sentRequestIds = friendRequestRepository.getRequestsSentByUser(uid).map { it.toUserId }
                val receivedRequestIds = _pendingRequests.value.map { it.uid }

                _addableUsers.value = users.filter { user ->
                    user.uid != uid &&
                            !friendIds.contains(user.uid) &&
                            !sentRequestIds.contains(user.uid) &&
                            !receivedRequestIds.contains(user.uid)
                }
            }
        }
    }

    fun loadSnapsFromFriend(friendId: String) {
        currentUserId?.let { uid ->
            viewModelScope.launch {
                _receivedSnaps.value = snapRepository.getSnaps(fromUserId = friendId, toUserId = uid)
            }
        }
    }


    fun loadFriendsWithSnaps() {
        currentUserId?.let { uid ->
            viewModelScope.launch {
                val snaps = snapRepository.getSnapsForUser(uid)
                _friendsWithSnaps.value = snaps.map { it.fromUserId }.toSet()
            }
        }
    }

    fun sendFriendRequest(toUserId: String, onResult: (Boolean, String?) -> Unit) {
        currentUserId?.let { uid ->
            viewModelScope.launch {
                try {
                    friendRequestRepository.sendRequest(FriendRequest(fromUserId = uid, toUserId = toUserId))
                    loadAddableUsers()
                    onResult(true, null)
                } catch (e: Exception) {
                    onResult(false, e.message)
                }
            }
        }
    }

    fun acceptFriendRequest(friendUid: String, onResult: (Boolean, String?) -> Unit) {
        currentUserId?.let { uid ->
            viewModelScope.launch {
                try {
                    // Add each other as friends
                    friendRepository.addFriend(uid, friendUid)
                    friendRepository.addFriend(friendUid, uid)
                    // Remove the request
                    friendRequestRepository.deleteRequest(uid, friendUid)
                    loadFriends()
                    loadPendingRequests()
                    onResult(true, null)
                } catch (e: Exception) {
                    onResult(false, e.message)
                }
            }
        }
    }

    fun declineFriendRequest(friendUid: String, onResult: (Boolean, String?) -> Unit) {
        currentUserId?.let { uid ->
            viewModelScope.launch {
                try {
                    friendRequestRepository.deleteRequest(uid, friendUid)
                    loadPendingRequests()
                    loadAddableUsers()
                    onResult(true, null)
                } catch (e: Exception) {
                    onResult(false, e.message)
                }
            }
        }
    }

    fun deleteSnaps(snapIds: List<String>) {
        viewModelScope.launch {
            snapRepository.deleteSnaps(snapIds)
            _receivedSnaps.value = emptyList()
        }
    }
}
