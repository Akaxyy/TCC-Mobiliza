package com.example.aps.services

import com.example.aps.Model.UserProfile
import com.google.firebase.auth.FirebaseAuth


class CurrentUserService {
    private val auth: FirebaseAuth = FirebaseAuth.getInstance()

    val userId: String
        get() = auth.currentUser?.uid ?: "" // Retorna o UID ou uma string vazia se deslogado

    val userProfile: UserProfile
        get() {
            val firebaseUser = auth.currentUser
            return UserProfile(
                displayName = firebaseUser?.displayName,
                email = firebaseUser?.email,
                photoUrl = firebaseUser?.photoUrl?.toString()
            )
        }
}