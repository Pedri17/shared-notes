package com.pproject.sharednotes.data.entity

data class Note(
    var id: Int = 0,
    var title: String = "",
    var content: String = "",
    var pinned: Boolean = false,
    var state: Situation = Situation.ON_USE,
    var users: Array<User> = emptyArray(),
) {
    enum class Situation {
        ON_USE,
        ARCHIVED,
        DELETED,
    }
}