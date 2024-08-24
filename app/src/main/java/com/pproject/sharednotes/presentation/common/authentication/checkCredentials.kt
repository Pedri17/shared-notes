package com.pproject.sharednotes.presentation.common.authentication

import android.content.Context
import android.widget.Toast
import com.pproject.sharednotes.R

fun checkCredentials(username: String, password: String, context: Context): Boolean {
    val passwordPattern = Regex("^(?=.*[0–9])(?=.*[a-z])(?=.*[A-Z])(?=\\S+$).{8,}$")
    val userPattern = Regex("^[a-zA-Z0-9]+([._]?[a-zA-Z0-9]+)*\$")

    return if (!(userPattern.matches(username) && passwordPattern.matches(password))) {
        Toast.makeText(
            context,
            context.getString(R.string.wrong_credentials),
            Toast.LENGTH_SHORT
        )
            .show()
        false
    } else {
        true
    }
}