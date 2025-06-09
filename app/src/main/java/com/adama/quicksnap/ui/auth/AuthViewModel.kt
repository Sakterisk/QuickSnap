package com.adama.quicksnap.ui.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.adama.quicksnap.data.model.User
import com.adama.quicksnap.data.repository.UserRepository
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class AuthViewModel(
    private val userRepository: UserRepository = UserRepository()
) : ViewModel() {

    private val auth: FirebaseAuth = FirebaseAuth.getInstance()

    val firebaseUser: FirebaseUser?
        get() = auth.currentUser

    private val _isLoggedIn = MutableStateFlow(auth.currentUser != null)
    val isLoggedIn: StateFlow<Boolean> = _isLoggedIn.asStateFlow()

    private val _currentUser = MutableStateFlow<User?>(null)
    val currentUser: StateFlow<User?> = _currentUser

    init {
        auth.addAuthStateListener {
            _isLoggedIn.value = it.currentUser != null
        }
        loadCurrentUser()
    }

    fun login(email: String, password: String, onResult: (Boolean, String?) -> Unit) {
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (!task.isSuccessful) {
                    onResult(false, task.exception?.message ?: "Login failed")
                }
                loadCurrentUser()
            }
    }

    fun register(username: String, email: String, password: String, onResult: (Boolean, String?) -> Unit) {
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (!task.isSuccessful) {
                    onResult(false, task.exception?.message ?: "Login failed")
                }
                val user = auth.currentUser
                if (user != null) {
                    val user = User(
                        uid = user.uid,
                        username = username,
                        email = email,
                        profilePictureUrl = null
                    )
                    viewModelScope.launch {
                        try {
                            userRepository.createUser(user)
                            loadCurrentUser()
                            onResult(true, null)
                        } catch (e: Exception) {
                            onResult(
                                false,
                                "Registration succeeded but failed to save user profile: ${e.message}"
                            )
                        }
                    }
                }
            }
    }

    fun loadCurrentUser() {
        val uid = firebaseUser?.uid
        if (uid != null) {
            viewModelScope.launch {
                _currentUser.value = userRepository.getUser(uid)
            }
        }
    }

    fun logout() {
        auth.signOut()
        _currentUser.value = null
    }
}
