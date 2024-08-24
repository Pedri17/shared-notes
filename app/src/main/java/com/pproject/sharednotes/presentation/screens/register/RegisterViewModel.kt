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
import kotlinx.coroutines.launch

data class RegisterUiState(
    var username: String = "",
    var password: String = "",
    var repeatPassword: String = "",
) {
    fun isNotEmpty(): Boolean {
        return username.isNotEmpty() && password.isNotEmpty()
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

    fun emptyCredentials() {
        uiState = uiState.copy(username = "", password = "", repeatPassword = "")
    }

    fun saveUser(navController: NavController) = viewModelScope.launch {
        if (userRepository.getUser(uiState.username).asLiveData().value == null) {
            userRepository.insert(User(uiState.username, uiState.password))
            navController.navigate(AppScreens.LoginScreen.route)
        } else {
            emptyCredentials()
            Toast.makeText(
                navController.context,
                navController.context.getString(R.string.the_user_is_already_registered),
                Toast.LENGTH_SHORT
            )
                .show()
        }
    }

    fun checkCredentials(context: Context): Boolean {
        //val passwordPattern = Regex("^(?=.*[0–9])(?=.*[a-z])(?=.*[A-Z])(?=\\S+$).{8,}$")
        //val userPattern = Regex("^[a-zA-Z0-9]+([._]?[a-zA-Z0-9]+)*\$")
        val passwordPattern = Regex("^[a-zA-Z0-9]+$")
        val userPattern = Regex("^[a-zA-Z0-9]+$")

        return if (false) {
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