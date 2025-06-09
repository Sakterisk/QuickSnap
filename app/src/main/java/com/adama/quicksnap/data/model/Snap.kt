package com.adama.quicksnap.data.model

data class Snap(
    val fromUserId: String = "",
    val toUserId: String = "",
    val imageUrl: String = "",
    val timestamp: Long = System.currentTimeMillis(),
)
