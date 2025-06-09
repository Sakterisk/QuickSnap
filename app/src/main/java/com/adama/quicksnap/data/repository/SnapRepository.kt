package com.adama.quicksnap.data.repository

import android.content.Context
import android.net.Uri
import android.util.Log
import com.adama.quicksnap.data.model.Snap
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.tasks.await
import java.io.File
import java.util.UUID

class SnapRepository {
    private val snapsRef = FirebaseFirestore.getInstance().collection("snaps")

    suspend fun uploadImageAndSendSnaps(
        context: Context,
        photoUri: Uri,
        fromUserId: String,
        toUserIds: List<String>,
        type: String
    ) {
        try {
            val inputStream = when (type) {
                "content" -> context.contentResolver.openInputStream(photoUri)
                "file" -> File(photoUri.path!!).inputStream()
                else -> throw IllegalArgumentException("Unsupported URI scheme: ${photoUri.scheme}")
            } ?: throw IllegalArgumentException("Could not open input stream for URI: $photoUri")
            val storageRef = FirebaseStorage.getInstance().reference.child("snaps/${System.currentTimeMillis()}.jpg")
            storageRef.putStream(inputStream).await()
            val downloadUrl = storageRef.downloadUrl.await()
            toUserIds.forEach { toUserId ->
                val snap = Snap(
                    fromUserId = fromUserId,
                    toUserId = toUserId,
                    imageUrl = downloadUrl.toString(),
                    timestamp = System.currentTimeMillis()
                )
                snapsRef.add(snap).await()
            }


        } catch (e: Exception) {
            Log.e("SnapRepository", "Exception in upload/send", e)
        }
    }

    suspend fun getSnapsForUser(userId: String): List<Snap> {
        val snapshot = snapsRef.whereEqualTo("toUserId", userId).get().await()
        return snapshot.toObjects(Snap::class.java)
    }
}
