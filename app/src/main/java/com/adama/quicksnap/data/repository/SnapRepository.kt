package com.adama.quicksnap.data.repository

import com.adama.quicksnap.data.model.Snap
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

class SnapRepository {
    private val snapsRef = FirebaseFirestore.getInstance().collection("snaps")

    suspend fun sendSnap(snap: Snap) {
        snapsRef.add(snap).await()
    }

    suspend fun getSnapsForUser(userId: String): List<Snap> {
        val snapshot = snapsRef.whereEqualTo("toUserId", userId).get().await()
        return snapshot.toObjects(Snap::class.java)
    }
}
