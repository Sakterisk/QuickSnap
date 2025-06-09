package com.adama.quicksnap.data.repository

import com.adama.quicksnap.data.model.Friend
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

class FriendRepository {
    private val friendsRef = FirebaseFirestore.getInstance().collection("friends")

    suspend fun addFriend(friend: Friend) {
        friendsRef.add(friend).await()
    }

    suspend fun addFriend(userId: String, friendId: String) {
        val friend = Friend(userId = userId, friendId = friendId)
        friendsRef.add(friend).await()
    }

    suspend fun getFriendsForUser(userId: String): List<Friend> {
        val snapshot = friendsRef.whereEqualTo("userId", userId).get().await()
        return snapshot.toObjects(Friend::class.java)
    }
}
