package com.adama.quicksnap.data.repository

import com.adama.quicksnap.data.model.FriendRequest
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

class FriendRequestRepository {
    private val requestsRef = FirebaseFirestore.getInstance().collection("friend_requests")

    suspend fun sendRequest(request: FriendRequest) {
        requestsRef.add(request).await()
    }

    suspend fun getRequestsForUser(userId: String): List<FriendRequest> {
        val snapshot = requestsRef.whereEqualTo("toUserId", userId).get().await()
        return snapshot.toObjects(FriendRequest::class.java)
    }

    suspend fun getRequestsSentByUser(userId: String): List<FriendRequest> {
        val snapshot = requestsRef.whereEqualTo("fromUserId", userId).get().await()
        return snapshot.toObjects(FriendRequest::class.java)
    }

    suspend fun deleteRequest(toUserId: String, fromUserId: String) {
        val snapshot = requestsRef
            .whereEqualTo("fromUserId", fromUserId)
            .whereEqualTo("toUserId", toUserId)
            .get()
            .await()

        snapshot.documents.forEach { doc ->
            doc.reference.delete()
        }
    }
}
