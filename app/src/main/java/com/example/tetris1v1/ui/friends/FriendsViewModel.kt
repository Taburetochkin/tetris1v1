package com.example.tetris1v1.ui.friends

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tetris1v1.models.FriendRequest
import com.example.tetris1v1.models.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

class FriendsViewModel : ViewModel() {

    private val auth = FirebaseAuth.getInstance()
    private val db = FirebaseFirestore.getInstance()

    private val _friendRequests = MutableStateFlow<List<FriendRequest>>(emptyList())
    val friendRequests: StateFlow<List<FriendRequest>> = _friendRequests

    private val _searchResult = MutableStateFlow<SearchState>(SearchState.Idle)
    val searchResult: StateFlow<SearchState> = _searchResult

    private val _requestState = MutableStateFlow<RequestState>(RequestState.Idle)
    val requestState: StateFlow<RequestState> = _requestState

    private val _friends = MutableStateFlow<List<User>>(emptyList())
    val friends: StateFlow<List<User>> = _friends

    private val _friendsLoading = MutableStateFlow(false)
    val friendsLoading: StateFlow<Boolean> = _friendsLoading

    val currentUserId get() = auth.currentUser?.uid ?: ""

    fun searchUserByUsername(username: String) {
        viewModelScope.launch {
            _searchResult.value = SearchState.Loading
            try {
                val snapshot = db.collection("users")
                    .whereEqualTo("username", username)
                    .get()
                    .await()
                if (snapshot.isEmpty) {
                    _searchResult.value = SearchState.NotFound
                } else {
                    val doc = snapshot.documents.first()
                    val foundUserId = doc.id
                    val foundUsername = doc.getString("username") ?: ""
                    if (foundUserId == currentUserId) {
                        _searchResult.value = SearchState.NotFound
                    } else {
                        _searchResult.value = SearchState.Found(foundUserId, foundUsername)
                    }
                }
            } catch (e: Exception) {
                _searchResult.value = SearchState.Error(e.message ?: "Search failed")
            }
        }
    }

    fun sendFriendRequest(toUserId: String) {
        viewModelScope.launch {
            _requestState.value = RequestState.Loading
            try {
                val fromUserId = currentUserId

                val userDoc = db.collection("users").document(fromUserId).get().await()
                val requesterUsername = userDoc.getString("username") ?: ""

                val existing = db.collection("friends")
                    .whereEqualTo("requesterId", fromUserId)
                    .whereEqualTo("receiverId", toUserId)
                    .get()
                    .await()

                if (!existing.isEmpty) {
                    _requestState.value = RequestState.Error("Friend request already sent!")
                    return@launch
                }

                val request = hashMapOf(
                    "requesterId" to fromUserId,
                    "requesterUsername" to requesterUsername,
                    "receiverId" to toUserId,
                    "status" to "pending",
                    "createdAt" to System.currentTimeMillis()
                )

                db.collection("friends").add(request).await()
                _requestState.value = RequestState.Success
            } catch (e: Exception) {
                _requestState.value = RequestState.Error(e.message ?: "Failed to send request")
            }
        }
    }

    fun loadIncomingRequests() {
        viewModelScope.launch {
            try {
                val snapshot = db.collection("friends")
                    .whereEqualTo("receiverId", currentUserId)
                    .whereEqualTo("status", "pending")
                    .get()
                    .await()

                _friendRequests.value = snapshot.documents.map { doc ->
                    FriendRequest(
                        id = doc.id,
                        requesterId = doc.getString("requesterId") ?: "",
                        requesterUsername = doc.getString("requesterUsername") ?: "Unknown",
                        receiverId = doc.getString("receiverId") ?: "",
                        status = doc.getString("status") ?: "pending"
                    )
                }
            } catch (e: Exception) {
                // handle silently
            }
        }
    }

    fun acceptFriendRequest(requestId: String, requesterId: String) {
        viewModelScope.launch {
            try {
                val currentUser = currentUserId

                db.collection("friends").document(requestId)
                    .update("status", "accepted").await()

                db.collection("users").document(currentUser)
                    .update("friendsId", FieldValue.arrayUnion(requesterId)).await()

                db.collection("users").document(requesterId)
                    .update("friendsId", FieldValue.arrayUnion(currentUser)).await()

                loadIncomingRequests()
            } catch (e: Exception) {
                // handle silently
            }
        }
    }

    fun declineFriendRequest(requestId: String) {
        viewModelScope.launch {
            try {
                db.collection("friends").document(requestId)
                    .delete().await()
                loadIncomingRequests()
            } catch (e: Exception) {
                // handle silently
            }
        }
    }

    fun loadFriends() {
        viewModelScope.launch {
            _friendsLoading.value = true
            try {
                val userDoc = db.collection("users").document(currentUserId).get().await()
                val friendIds = userDoc.get("friendsId") as? List<String> ?: emptyList()

                if (friendIds.isEmpty()) {
                    _friends.value = emptyList()
                    _friendsLoading.value = false
                    return@launch
                }

                val friendsList = friendIds.mapNotNull { friendId ->
                    try {
                        val doc = db.collection("users").document(friendId).get().await()
                        User(
                            id = friendId,
                            userName = doc.getString("username") ?: "",
                            age = (doc.getLong("age") ?: 0).toInt(),
                            country = doc.getString("country") ?: "",
                            highScore = (doc.getLong("highScore") ?: 0).toInt()
                        )
                    } catch (e: Exception) {
                        null
                    }
                }

                _friends.value = friendsList
            } catch (e: Exception) {
                _friends.value = emptyList()
            } finally {
                _friendsLoading.value = false
            }
        }
    }

    fun resetRequestState() {
        _requestState.value = RequestState.Idle
    }

    fun resetSearchState() {
        _searchResult.value = SearchState.Idle
    }
}

sealed class SearchState {
    object Idle : SearchState()
    object Loading : SearchState()
    object NotFound : SearchState()
    data class Found(val userId: String, val username: String) : SearchState()
    data class Error(val message: String) : SearchState()
}

sealed class RequestState {
    object Idle : RequestState()
    object Loading : RequestState()
    object Success : RequestState()
    data class Error(val message: String) : RequestState()
}