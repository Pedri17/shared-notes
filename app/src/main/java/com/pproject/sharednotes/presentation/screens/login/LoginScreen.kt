package com.pproject.sharednotes.presentation.screens.login

import android.content.Context
import android.widget.Toast
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
import com.pproject.sharednotes.presentation.common.ClickableRouteText
import com.pproject.sharednotes.presentation.common.LabeledCheckbox
import com.pproject.sharednotes.presentation.common.DataTextField
import com.pproject.sharednotes.presentation.common.PasswordField
import com.pproject.sharednotes.presentation.navigation.AppScreens

data class LoginCredentials(
    var login: String = "",
    var password: String = "",
    var remember: Boolean = false
) {
    fun isNotEmpty(): Boolean {
        return login.isNotEmpty() && password.isNotEmpty()
    }
}

@Composable
fun LoginScreen(navController: NavController) {
    Surface {
        var credentials by remember { mutableStateOf(LoginCredentials()) }

        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 30.dp)
        ) {
            Spacer(modifier = Modifier.height(10.dp))
            DataTextField(
                value = credentials.login,
                onChange = { data -> credentials = credentials.copy(login = data) },
                modifier = Modifier.fillMaxWidth()
            )
            PasswordField(
                value = credentials.password,
                onChange = { data -> credentials = credentials.copy(password = data) },
                submit = {
                    if (!checkCredentials(credentials, navController.context)) {
                        credentials = LoginCredentials();
                    }
                },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(10.dp))
            ClickableRouteText(
                navController = navController,
                text = stringResource(R.string.login),
                route = AppScreens.SplashScreen.route
            )
            Spacer(modifier = Modifier.height(10.dp))
            LabeledCheckbox(
                label = stringResource(R.string.remember_me),
                onCheckChanged = {
                    credentials = credentials.copy(remember = !credentials.remember)
                },
                isChecked = credentials.remember
            )
            Spacer(modifier = Modifier.height(20.dp))
            Button(
                onClick = {
                    if (!checkCredentials(credentials, navController.context)){
                        credentials = LoginCredentials()
                        navController.navigate(AppScreens.NoteScreen.route)
                    }
                },
                enabled = credentials.isNotEmpty(),
                shape = RoundedCornerShape(5.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(stringResource(R.string.login))
            }
            Spacer(modifier = Modifier.height(10.dp))
            ClickableRouteText(
                navController = navController,
                text = stringResource(R.string.or_sign_up),
                route = AppScreens.RegisterScreen.route
            )
        }
    }
}

private fun checkCredentials(cred: LoginCredentials, context: Context): Boolean {
    val emailPattern: Regex = Regex("^[^.\\s][\\w\\-.{2,}]+@([\\w-]+\\.)+[\\w-]{2,}\$")
    val passwordPattern: Regex = Regex("^(?=.*[0–9])(?=.*[a-z])(?=.*[A-Z])(?=\\S+$).{8,}$")
    val userPattern: Regex = Regex("^[a-zA-Z0-9]+([._]?[a-zA-Z0-9]+)*\$")

    // Test email or username
    var validId = false
    if (cred.isNotEmpty()){
        validId = if (cred.login.contains('@')) {
            emailPattern.matches(cred.login)
        } else {
            userPattern.matches(cred.login)
        }
    }

    return if (!(validId && passwordPattern.matches(cred.password))) {
        Toast.makeText(context, context.getString(R.string.wrong_credentials), Toast.LENGTH_SHORT).show()
        false
    } else {
        true
    }
}