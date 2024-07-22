package com.pproject.sharednotes.data.test

import com.pproject.sharednotes.data.entity.User

var testusers: List<User> = arrayListOf(
    User("user1", "ps1"),
    User("user2", "ps2"),
    User("user3", "ps3"),
    User("user4", "ps4"),
    User("user5", "ps5"),
    User("user6", "ps6"),
)

fun getAllUsers(): List<User> {
    return testusers
}

fun getAllUserNames(): List<String> {
    var result: List<String> = emptyList()
    for (user in getAllUsers()) {
        result = result.plus(user.name)
    }
    return result
}

fun getUser(name: String): User? {
    for (user in getAllUsers()) {
        if (user.name == name) {
            return user
        }
    }
    return null
}