package com.adama.quicksnap.data.repository

import com.adama.quicksnap.data.model.User
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

class UserRepository {
    private val usersRef = FirebaseFirestore.getInstance().collection("users")

    suspend fun createUser(user: User) {
        usersRef.document(user.uid).set(user).await()
    }

    suspend fun getUser(uid: String): User? {
        val doc = usersRef.document(uid).get().await()
        return doc.toObject(User::class.java)
    }

    suspend fun updateUser(user: User) {
        usersRef.document(user.uid).set(user).await()
    }

    suspend fun getAllUsers(): List<User> {
        val doc = usersRef.get().await()
        return doc.toObjects(User::class.java)
    }
}
