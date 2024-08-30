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
)

@Entity(primaryKeys = ["noteId", "userName"])
data class NoteUserCrossRef(
    val noteId: Int = 0,
    val userName: String = "",
    var pinned: Boolean = false,
)

data class NoteWithFolders(
    @Embedded val note: Note,
    @Relation(
        parentColumn = "noteId",
        entityColumn = "folderId",
        associateBy = Junction(FolderNoteCrossRef::class)
    )
    val folders: List<Folder>
) {
    fun getSelectedFolderId(username: String): Int? {
        return folders.firstOrNull { folder ->
            folder.userName == username
        }?.folderId
    }
}