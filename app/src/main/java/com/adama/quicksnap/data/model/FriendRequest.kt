package com.adama.quicksnap.data.model

data class FriendRequest(
    val fromUserId: String = "",
    val toUserId: String = "",
    val timestamp: Long = System.currentTimeMillis()
)
