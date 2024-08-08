package com.pproject.sharednotes.data.db.entity

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.Junction
import androidx.room.PrimaryKey
import androidx.room.Relation

@Entity
data class Note(
    @PrimaryKey(autoGenerate = true) var noteId: Int = 0,
    var title: String = "New note",
    var content: String = "",
    var pinned: Boolean = false,
    var situation: Situation = Situation.ON_USE,
    @Ignore var folder: Int? = null,
) {
    enum class Situation {
        ON_USE,
        ARCHIVED,
        DELETED,
    }
}

@Entity(primaryKeys = ["noteId", "userName"])
data class NoteUserCrossRef(
    val noteId: Int,
    val userName: String,
)