package com.wolfpackdigital.cashli.domain.abstractions.repositories

import com.wolfpackdigital.cashli.domain.entities.response.UserProfile

interface UserRepository {
    suspend fun getUserProfile(): UserProfile
}
