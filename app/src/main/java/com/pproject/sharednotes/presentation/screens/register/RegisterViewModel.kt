package com.pproject.sharednotes.presentation.screens.register

import android.content.Context
import android.widget.Toast
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.asLiveData
import androidx.lifecycle.createSavedStateHandle
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.CreationExtras
import androidx.navigation.NavController
import com.pproject.sharednotes.R
import com.pproject.sharednotes.app.SharedNotesApplication
import com.pproject.sharednotes.data.db.entity.User
import com.pproject.sharednotes.data.repository.UserRepository
import com.pproject.sharednotes.presentation.navigation.AppScreens
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch

data class RegisterUiState(
    var username: String = "",
    var password: String = "",
    var repeatPassword: String = "",
) {
    fun isNotEmpty(): Boolean {
        return username.isNotEmpty() && password.isNotEmpty() && repeatPassword.isNotEmpty()
    }
}

class RegisterViewModel(
    private val savedStateHandle: SavedStateHandle,
    private val userRepository: UserRepository,
) : ViewModel() {
    var uiState by mutableStateOf(RegisterUiState())
        private set

    fun updateUsername(newUsername: String) {
        uiState = uiState.copy(username = newUsername)
    }

    fun updatePassword(newPassword: String) {
        uiState = uiState.copy(password = newPassword)
    }

    fun updateRepeatPassword(newPassword: String) {
        uiState = uiState.copy(repeatPassword = newPassword)
    }

    private fun emptyCredentials() {
        uiState = uiState.copy(username = "", password = "", repeatPassword = "")
    }

    fun saveUser(navController: NavController) = viewModelScope.launch {
        if (userRepository.getUser(uiState.username).firstOrNull() == null) {
            if (uiState.password == uiState.repeatPassword) { // Correct passwords.
                userRepository.insert(User(uiState.username, uiState.password))
                userRepository.saveUsersOnCloud()
                navController.navigate(AppScreens.LoginScreen.route)
            } else { // Wrong passwords.
                Toast.makeText(
                    navController.context,
                    "Passwords do not match",
                    Toast.LENGTH_SHORT
                )
                    .show()
            }
        } else { // User already registered.
            emptyCredentials()
            Toast.makeText(
                navController.context,
                navController.context.getString(R.string.the_user_is_already_registered),
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
                val application = checkNotNull(extras[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY]) as SharedNotesApplication
                val savedStateHandle = extras.createSavedStateHandle()

                return RegisterViewModel(
                    savedStateHandle,
                    userRepository = application.container.userRepository,
                ) as T
            }
        }
    }
}