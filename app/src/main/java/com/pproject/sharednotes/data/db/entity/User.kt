package com.pproject.sharednotes.data.db.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
class User(
    @PrimaryKey var userName: String = "",
    var password: String = "",
)