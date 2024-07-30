package com.pproject.sharednotes.presentation.screens.register

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.pproject.sharednotes.R
import com.pproject.sharednotes.presentation.common.DataTextField
import com.pproject.sharednotes.presentation.common.PasswordField

data class RegisterCredentials(
    var user: String = "",
    var email: String = "",
    var password: String = "",
    var repeatPassword: String = ""
) {
    fun isNotEmpty(): Boolean {
        return user.isNotEmpty() && email.isNotEmpty() && password.isNotEmpty() && repeatPassword.isNotEmpty()
    }
}

@Composable
fun RegisterScreen(navController: NavController) {
    Surface {
        var credentials by remember { mutableStateOf(RegisterCredentials()) }

        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 30.dp)
        ){
            Spacer(modifier = Modifier.height(10.dp))
            DataTextField(
                value = credentials.user,
                onChange = { data -> credentials = credentials.copy(user = data) },
                modifier = Modifier.fillMaxWidth()
            )
            DataTextField(
                value = credentials.email,
                onChange = { data -> credentials = credentials.copy(email = data) },
                modifier = Modifier.fillMaxWidth()
            )
            PasswordField(
                value = credentials.password,
                onChange = { data -> credentials = credentials.copy(password = data) },
                submit = {},
                modifier = Modifier.fillMaxWidth()
            )
            PasswordField(
                value = credentials.password,
                onChange = { data -> credentials = credentials.copy(repeatPassword = data) },
                submit = {},
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(20.dp))
            Button(
                onClick = {},
                enabled = credentials.isNotEmpty(),
                shape = RoundedCornerShape(5.dp),
                modifier = Modifier.fillMaxWidth()
            ){
                Text(stringResource(R.string.register))
            }
        }
    }
}