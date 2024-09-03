package com.pproject.sharednotes.presentation.screens.login

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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.pproject.sharednotes.R
import com.pproject.sharednotes.presentation.common.LoadingScreen
import com.pproject.sharednotes.presentation.common.authentication.ClickableRouteText
import com.pproject.sharednotes.presentation.common.authentication.DataTextField
import com.pproject.sharednotes.presentation.common.authentication.PasswordField
import com.pproject.sharednotes.presentation.navigation.AppScreens
import com.pproject.sharednotes.presentation.screens.login.components.LabeledCheckbox

@Composable
fun LoginScreen(
    navController: NavController,
    loginViewModel: LoginViewModel = viewModel(factory = LoginViewModel.Factory),
) {
    LaunchedEffect(navController) {
        loginViewModel.tryLogOnActiveUser(navController)
    }
    if (!loginViewModel.uiState.loading) {
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
                    value = loginViewModel.uiState.username,
                    onChange = { loginViewModel.updateUsername(it) },
                    modifier = Modifier.fillMaxWidth()
                )
                PasswordField(
                    value = loginViewModel.uiState.password,
                    onChange = { loginViewModel.updatePassword(it) },
                    submit = {},
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(10.dp))
                LabeledCheckbox(
                    label = stringResource(R.string.remember_me),
                    onCheckChanged = {
                        loginViewModel.updateRemember(!loginViewModel.uiState.remember)
                    },
                    isChecked = loginViewModel.uiState.remember
                )
                Spacer(modifier = Modifier.height(20.dp))
                Button(
                    onClick = { loginViewModel.loginUser(navController) },
                    enabled = loginViewModel.uiState.isNotEmpty(),
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
    } else {
        LoadingScreen()
    }
}