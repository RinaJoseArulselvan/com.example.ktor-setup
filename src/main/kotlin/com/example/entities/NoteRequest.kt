package com.example.entities

import kotlinx.serialization.Serializable

@Serializable
data class NoteRequest(
    val note:String
)
