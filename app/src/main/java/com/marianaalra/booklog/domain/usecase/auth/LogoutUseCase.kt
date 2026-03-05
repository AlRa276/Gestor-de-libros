package com.marianaalra.booklog.domain.usecase.auth

import com.marianaalra.booklog.domain.repository.AuthRepository

class LogoutUseCase(private val repository: AuthRepository) {
    suspend operator fun invoke() {
        repository.logout()
    }
}