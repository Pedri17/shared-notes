package com.pproject.sharednotes.data.local.entity

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.Index
import androidx.room.Junction
import androidx.room.PrimaryKey
import androidx.room.Relation

@Entity
data class Folder(
    @PrimaryKey(autoGenerate = true) var folderId: Int = 0,
    var userName: String = "",
    var title: String = "New folder",
)

@Entity(
    primaryKeys = ["folderId", "noteId"],
    indices = [Index(value = ["noteId"])],
)
data class FolderNoteCrossRef(
    val noteId: Int = 0,
    val folderId: Int = 0,
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
