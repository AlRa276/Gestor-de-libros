package com.marianaalra.book.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.marianaalra.book.domain.model.UserDomain
import com.marianaalra.book.domain.usecase.auth.LoginUseCase
import com.marianaalra.book.domain.usecase.auth.LogoutUseCase
import com.marianaalra.book.domain.usecase.auth.RegisterUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class AuthViewModel(
    private val loginUseCase: LoginUseCase,
    private val registerUseCase: RegisterUseCase,
    private val logoutUseCase: LogoutUseCase
) : ViewModel() {

    private val _currentUser = MutableStateFlow<UserDomain?>(null)
    val currentUser: StateFlow<UserDomain?> = _currentUser

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error

    fun login(correo: String, contrasena: String, onSuccess: () -> Unit) {
        viewModelScope.launch {
            loginUseCase(correo, contrasena)
                .onSuccess { user ->
                    _currentUser.value = user
                    _error.value = null // 👈 LIMPIA ERROR
                    onSuccess()
                }
                .onFailure { _error.value = it.message }
        }
    }

    fun register(nombreUsuario: String, correo: String, contrasena: String, onSuccess: () -> Unit) {
        viewModelScope.launch {
            registerUseCase(nombreUsuario, correo, contrasena)
                .onSuccess { user ->
                    _currentUser.value = user
                    _error.value = null // 👈 LIMPIA ERROR
                    onSuccess()
                }
                .onFailure { _error.value = it.message }
        }
    }

    fun logout(onSuccess: () -> Unit) {
        viewModelScope.launch {
            logoutUseCase()
            _currentUser.value = null
            _error.value = null // 👈 LIMPIA errores anteriores
            onSuccess()
        }
    }

    fun clearError() { _error.value = null }
}