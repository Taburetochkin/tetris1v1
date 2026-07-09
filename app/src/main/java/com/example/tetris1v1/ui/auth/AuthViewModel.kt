package com.example.tetris1v1.ui.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tetris1v1.models.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

class AuthViewModel : ViewModel() {

    private val auth = FirebaseAuth.getInstance()
    private val db = FirebaseFirestore.getInstance()

    init {
        auth.currentUser?.uid?.let { uid ->
            viewModelScope.launch {
                fetchAndSetUser(uid)
            }
        }
    }

    private val _authState = MutableStateFlow<AuthState>(AuthState.Idle)
    val authState: StateFlow<AuthState> = _authState

    private val _currentUser = MutableStateFlow<User?>(null)
    val currentUser: StateFlow<User?> = _currentUser

    fun login(email: String, password: String) {
        viewModelScope.launch {
            _authState.value = AuthState.Loading
            try {
                val result = auth.signInWithEmailAndPassword(email, password).await()
                val uid = result.user?.uid ?: throw Exception("User ID not found")
                fetchAndSetUser(uid)
                _authState.value = AuthState.Success
            } catch (e: Exception) {
                _authState.value = AuthState.Error(e.message ?: "Login failed")
            }
        }
    }

    fun register(username: String, email: String, password: String, age: Int, country: String) {
        viewModelScope.launch {
            _authState.value = AuthState.Loading
            try {
                val result = auth.createUserWithEmailAndPassword(email, password).await()
                val userId = result.user?.uid ?: throw Exception("User ID not found")

                val userMap = hashMapOf(
                    "userId" to userId,
                    "username" to username,
                    "email" to email,
                    "age" to age,
                    "profilePicUrl" to "",
                    "country" to country,
                    "highScore" to 0,
                    "createdAt" to System.currentTimeMillis()
                )

                db.collection("users").document(userId).set(userMap).await()
                fetchAndSetUser(userId)
                _authState.value = AuthState.Success
            } catch (e: Exception) {
                _authState.value = AuthState.Error(e.message ?: "Registration failed")
            }
        }
    }

    fun logout() {
        auth.signOut()
        _currentUser.value = null
        _authState.value = AuthState.LoggedOut
    }

    fun resetState() {
        _authState.value = AuthState.Idle
    }

    private suspend fun fetchAndSetUser(uid: String) {
        val snapshot = db.collection("users").document(uid).get().await()
        _currentUser.value = User(
            id = uid,
            userName = snapshot.getString("username") ?: "",
            age = (snapshot.getLong("age") ?: 0).toInt(),
            country = snapshot.getString("country") ?: "",
            highScore = (snapshot.getLong("highScore") ?: 0).toInt(),
            friendsId = (snapshot.get("friendsId") as? List<String>)?.toTypedArray() ?: emptyArray()
        )
    }

    fun updateHighScore(newScore: Int) {
        val user = _currentUser.value ?: return
        if (newScore <= user.highScore) return
        viewModelScope.launch {
            try {
                db.collection("users").document(user.id)
                    .update("highScore", newScore).await()
                _currentUser.value = user.copy(highScore = newScore)
            } catch (e: Exception) {
                // handle silently or emit an error state
            }
        }
    }

    fun updateUserProfile(age: Int, country: String) {
        val user = _currentUser.value ?: return
        viewModelScope.launch {
            _authState.value = AuthState.Loading
            try {
                db.collection("users").document(user.id)
                    .update(
                        mapOf(
                            "age" to age,
                            "country" to country
                        )
                    ).await()
                _currentUser.value = user.copy(age = age, country = country)
                _authState.value = AuthState.Success
            } catch (e: Exception) {
                _authState.value = AuthState.Error(e.message ?: "Update failed")
            }
        }
    }
}

sealed class AuthState {
    object Idle : AuthState()
    object Loading : AuthState()
    object Success : AuthState()
    object LoggedOut : AuthState()
    data class Error(val message: String) : AuthState()
}