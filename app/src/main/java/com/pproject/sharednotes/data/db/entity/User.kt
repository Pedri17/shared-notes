package com.pproject.sharednotes.data.db.entity

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.Junction
import androidx.room.PrimaryKey
import androidx.room.Relation

@Entity
data class User(
    @PrimaryKey val userName: String = "",
    val password: String = "",
)

data class UserWithFolders(
    @Embedded val user: User,
    @Relation(
        parentColumn = "userName",
        entityColumn = "folderId",
    )
    val notes: List<Folder>,
)