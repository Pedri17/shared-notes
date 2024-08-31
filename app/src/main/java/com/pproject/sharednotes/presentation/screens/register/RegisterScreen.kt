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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.pproject.sharednotes.R
import com.pproject.sharednotes.presentation.common.authentication.ClickableRouteText
import com.pproject.sharednotes.presentation.common.authentication.DataTextField
import com.pproject.sharednotes.presentation.common.authentication.PasswordField
import com.pproject.sharednotes.presentation.navigation.AppScreens

@Composable
fun RegisterScreen(
    navController: NavController,
    registerViewModel: RegisterViewModel = viewModel(factory = RegisterViewModel.Factory),
) {
    Surface {
        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 30.dp)
        ) {
            Spacer(modifier = Modifier.height(10.dp))
            DataTextField(
                value = registerViewModel.uiState.username,
                onChange = { registerViewModel.updateUsername(it) },
                modifier = Modifier.fillMaxWidth()
            )
            PasswordField(
                value = registerViewModel.uiState.password,
                onChange = { registerViewModel.updatePassword(it) },
                submit = {},
                modifier = Modifier.fillMaxWidth()
            )
            PasswordField(
                value = registerViewModel.uiState.repeatPassword,
                onChange = { registerViewModel.updateRepeatPassword(it) },
                submit = {},
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(20.dp))
            Button(
                onClick = { registerViewModel.saveUser(navController) },
                enabled = registerViewModel.uiState.isNotEmpty(),
                shape = RoundedCornerShape(5.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(stringResource(R.string.register))
            }
            ClickableRouteText(
                navController = navController,
                text = stringResource(R.string.or_sign_in),
                route = AppScreens.LoginScreen.route
            )
        }
    }
}