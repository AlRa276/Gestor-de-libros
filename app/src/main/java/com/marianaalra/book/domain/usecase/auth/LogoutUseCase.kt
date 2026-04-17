package com.marianaalra.book.domain.usecase.auth

import com.marianaalra.book.domain.repository.AuthRepository

class LogoutUseCase(private val repository: AuthRepository) {
    suspend operator fun invoke() {
        repository.logout()
    }
}