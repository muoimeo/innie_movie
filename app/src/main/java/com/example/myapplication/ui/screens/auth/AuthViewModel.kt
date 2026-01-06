package com.example.myapplication.ui.screens.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapplication.data.local.entities.User
import com.example.myapplication.data.repository.AuthRepository
import com.example.myapplication.data.repository.AuthResult
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

data class AuthUiState(
    val isLoading: Boolean = false,
    val currentUser: User? = null,
    val error: String? = null,
    val isSignUpSuccess: Boolean = false
)

class AuthViewModel(
    private val repository: AuthRepository
) : ViewModel() {

    private val _state = MutableStateFlow(AuthUiState())
    val state: StateFlow<AuthUiState> = _state

    fun login(identifier: String, password: String) {
        viewModelScope.launch {
            _state.value = _state.value.copy(isLoading = true, error = null)
            when (val result = repository.login(identifier, password)) {
                is AuthResult.Success -> {
                    // Connect auth to session manager for database tracking
                    com.example.myapplication.data.session.UserSessionManager.login(result.user.userId)
                    
                    _state.value = AuthUiState(
                        isLoading = false,
                        currentUser = result.user,
                        error = null
                    )
                }
                is AuthResult.Error -> {
                    _state.value = _state.value.copy(
                        isLoading = false,
                        error = result.error.toMessage()
                    )
                }
            }
        }
    }

    fun signUp(username: String, email: String, password: String) {
        viewModelScope.launch {
            _state.value = _state.value.copy(isLoading = true, error = null)
            when (val result = repository.signUp(username, email, password)) {
                is AuthResult.Success -> {
                    _state.value = AuthUiState(
                        isLoading = false,
                        currentUser = null, // Do not auto-login
                        error = null,
                        isSignUpSuccess = true
                    )
                }
                is AuthResult.Error -> {
                    _state.value = _state.value.copy(
                        isLoading = false,
                        error = result.error.toMessage(),
                        isSignUpSuccess = false
                    )
                }
            }
        }
    }

    fun clearError() {
        _state.value = _state.value.copy(error = null)
    }

    fun resetSignUpSuccess() {
        _state.value = _state.value.copy(isSignUpSuccess = false)
    }
}
