package com.pproject.sharednotes.presentation.screens.home

import androidx.lifecycle.ViewModel
import com.pproject.sharednotes.data.entity.Folder
import com.pproject.sharednotes.data.entity.Note
import com.pproject.sharednotes.data.test.createFolder
import com.pproject.sharednotes.data.test.getAllFolderIDs
import com.pproject.sharednotes.data.test.getFolder
import com.pproject.sharednotes.data.test.getNote

class HomeViewModel : ViewModel() {
    fun getFolders(): List<Int> {
        return getAllFolderIDs()
    }

    fun getFolderFromID(id: Int): Folder {
        return getFolder(id) ?: Folder()
    }

    fun getNoteFromID(id: Int): Note {
        return getNote(id) ?: Note()
    }

    fun createNewFolder(): Int {
        return createFolder()
    }
}