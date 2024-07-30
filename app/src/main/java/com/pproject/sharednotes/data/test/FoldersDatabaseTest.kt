package com.pproject.sharednotes.data.test

import androidx.compose.ui.text.input.TextFieldValue
import com.pproject.sharednotes.data.entity.Folder

var testfolders = listOf(
    Folder(0, TextFieldValue("Folder 0"), listOf(3,1,5,2,6,9,4,7,8), listOf(2,3,5)),
    Folder(1, TextFieldValue("Folder 1"), listOf(11,19,12,10,13,14,15,16,17,18), listOf(12,11)),
    Folder(2, TextFieldValue("Folder 2"), listOf(21,27,24,22,28,26,23,20,25,29), listOf(20,21,22)),
    Folder(3, TextFieldValue("Folder 3"), listOf(39,38,37,36,35,34,33,32,31,30), listOf(30,34,35))
)

fun getAllFolders(): List<Folder> {
    return testfolders
}

fun getAllFolderIDs(): List<Int> {
    val ids: MutableList<Int> = mutableListOf()
    for (folder in getAllFolders()){
        ids.add(folder.id)
    }
    return ids.toList()
}

fun getAllFolderPairNames(): List<Pair<Int, String>> {
    val pairs: MutableList<Pair<Int, String>> = mutableListOf()
    for (folder in getAllFolders()) {
        pairs.add(Pair(folder.id, folder.title.text))
    }
    return pairs.toList()
}

fun getFolder(id: Int): Folder? {
    for (folder in getAllFolders()) {
        if (folder.id == id) {
            return folder
        }
    }
    return null
}