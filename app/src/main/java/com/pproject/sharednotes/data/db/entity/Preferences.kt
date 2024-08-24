package com.pproject.sharednotes.data.db.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Preferences(
    @PrimaryKey val username: String = "",
    val activeSession: Boolean = false,
)