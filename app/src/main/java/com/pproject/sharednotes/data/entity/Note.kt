package com.pproject.sharednotes.data.entity

data class Note(
    var id: Int = 0,
    var title: String = "",
    var content: String = "",
    var pinned: Boolean = false,
    var situation: Situation = Situation.ON_USE,
    var folder: Int = 0,
    var users: List<String> = emptyList<String>(),
) {
    enum class Situation {
        ON_USE,
        ARCHIVED,
        DELETED,
    }
}