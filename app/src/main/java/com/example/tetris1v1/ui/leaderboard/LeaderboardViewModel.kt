package com.example.tetris1v1.ui.leaderboard

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tetris1v1.models.LeaderboardEntry
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FieldPath
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

class LeaderboardViewModel : ViewModel() {

    private val auth = FirebaseAuth.getInstance()
    private val db = FirebaseFirestore.getInstance()

    private val _globalEntries = MutableStateFlow<List<LeaderboardEntry>>(emptyList())
    val globalEntries: StateFlow<List<LeaderboardEntry>> = _globalEntries

    private val _friendsEntries = MutableStateFlow<List<LeaderboardEntry>>(emptyList())
    val friendsEntries: StateFlow<List<LeaderboardEntry>> = _friendsEntries

    val currentUserId get() = auth.currentUser?.uid ?: ""

    fun loadGlobalLeaderboard() {
        viewModelScope.launch {
            try {
                val snapshot = db.collection("users")
                    .orderBy("highScore", Query.Direction.DESCENDING)
                    .limit(50)
                    .get()
                    .await()

                _globalEntries.value = snapshot.documents.mapNotNull { doc ->
                    val highScore = doc.getLong("highScore")?.toInt() ?: 0
                    if (highScore == 0) return@mapNotNull null

                    LeaderboardEntry(
                        userId = doc.id,
                        username = doc.getString("username") ?: "Unknown",
                        highScore = highScore,
                        country = doc.getString("country") ?: ""
                    )
                }
            } catch (e: Exception) {
            }
        }
    }

    fun loadFriendsLeaderboard() {
        viewModelScope.launch {
            try {
                val userDoc = db.collection("users").document(currentUserId).get().await()

                @Suppress("UNCHECKED_CAST")
                val friendIds = (userDoc.get("friendsId") as? List<String>) ?: emptyList()
                val allIds = (friendIds + currentUserId).distinct()

                if (allIds.isEmpty()) {
                    _friendsEntries.value = emptyList()
                    return@launch
                }

                val entries = mutableListOf<LeaderboardEntry>()
                allIds.chunked(10).forEach { chunk ->
                    val snapshot = db.collection("users")
                        .whereIn(FieldPath.documentId(), chunk)
                        .get()
                        .await()

                    entries.addAll(snapshot.documents.map { doc ->
                        LeaderboardEntry(
                            userId = doc.id,
                            username = doc.getString("username") ?: "Unknown",
                            highScore = doc.getLong("highScore")?.toInt() ?: 0,
                            country = doc.getString("country") ?: ""
                        )
                    })
                }

                _friendsEntries.value = entries.sortedByDescending { it.highScore }
            } catch (e: Exception) {
            }
        }
    }
}