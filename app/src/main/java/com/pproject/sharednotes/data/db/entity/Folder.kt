package com.pproject.sharednotes.data.db.entity

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Ignore
import androidx.room.Junction
import androidx.room.PrimaryKey
import androidx.room.Relation

@Entity
data class Folder(
    @PrimaryKey(autoGenerate = true) var folderId: Int = 0,
    var user: String = "",
    var title: String = "New folder",
)

@Entity(primaryKeys = ["folderId", "noteId"])
data class FolderNoteCrossRef(
    var noteId: Int,
    var folderId: Int,
)

@Entity(primaryKeys = ["folderId", "noteId"])
data class FolderPinnedNoteCrossRef(
    var folderId: Int,
    var noteId: Int,
)

data class FolderWithNotes(
    @Embedded val folder: Folder,
    @Relation(
        parentColumn = "folderId",
        entityColumn = "noteId",
        associateBy = Junction(FolderNoteCrossRef::class)
    )
    val notes: List<Note>
)