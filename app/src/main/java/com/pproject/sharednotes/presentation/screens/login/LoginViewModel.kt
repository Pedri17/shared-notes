package com.pproject.sharednotes.presentation.screens.login

import android.widget.Toast
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.CreationExtras
import androidx.navigation.NavController
import com.pproject.sharednotes.R
import com.pproject.sharednotes.app.SharedNotesApplication
import com.pproject.sharednotes.data.local.entity.Preferences
import com.pproject.sharednotes.data.repository.PreferencesRepository
import com.pproject.sharednotes.data.repository.UserRepository
import com.pproject.sharednotes.presentation.navigation.AppScreens
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch

data class LoginUiState(
    val username: String = "",
    val password: String = "",
    var remember: Boolean = false,
) {
    fun isNotEmpty(): Boolean {
        return username.isNotEmpty() && password.isNotEmpty()
    }
}

class LoginViewModel(
    private val userRepository: UserRepository,
    private val preferencesRepository: PreferencesRepository,
) : ViewModel() {
    init {
        viewModelScope.launch {
            userRepository.loadFromCloud()
        }
    }

    // Screen state.
    var uiState by mutableStateOf(LoginUiState())
        private set

    // Form functions.
    fun updateUsername(newUsername: String) {
        uiState = uiState.copy(username = newUsername)
    }

    fun updatePassword(newPassword: String) {
        uiState = uiState.copy(password = newPassword)
    }

    fun updateRemember(newRemember: Boolean) {
        uiState = uiState.copy(remember = newRemember)
    }

    // Login functions.
    fun tryLogOnActiveUser(navController: NavController) = viewModelScope.launch {
        preferencesRepository.getActivePreference().firstOrNull()?.let {
            navController.navigate("${AppScreens.HomeScreen.route}/${it.username}")
        }
    }

    fun loginUser(navController: NavController) = viewModelScope.launch {
        val user = userRepository.getUser(uiState.username).firstOrNull()
        if (user != null) {
            if (user.password == uiState.password) { // Correct password.
                navController.navigate("${AppScreens.HomeScreen.route}/${uiState.username}")
                preferencesRepository.insert(
                    Preferences(
                        username = uiState.username,
                        activeSession = uiState.remember
                    )
                )
            } else { // Wrong password.
                Toast.makeText(
                    navController.context,
                    navController.context.getString(R.string.wrong_password),
                    Toast.LENGTH_SHORT
                )
                    .show()
            }
        } else { // User not registered.
            Toast.makeText(
                navController.context,
                navController.context.getString(R.string.the_user_is_not_registered),
                Toast.LENGTH_SHORT
            )
                .show()
        }
    }

    companion object {
        val Factory: ViewModelProvider.Factory = object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(
                modelClass: Class<T>,
                extras: CreationExtras
            ): T {
                val application =
                    checkNotNull(extras[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY])
                            as SharedNotesApplication

                return LoginViewModel(
                    userRepository = application.container.userRepository,
                    preferencesRepository = application.container.preferencesRepository,
                ) as T
            }
        }
    }
}