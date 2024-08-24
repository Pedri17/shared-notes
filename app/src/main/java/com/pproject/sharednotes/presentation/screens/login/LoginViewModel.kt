package com.pproject.sharednotes.presentation.screens.login

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.compose.runtime.collectAsState
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
import com.pproject.sharednotes.data.db.entity.Preferences
import com.pproject.sharednotes.data.db.entity.User
import com.pproject.sharednotes.data.repository.PreferencesRepository
import com.pproject.sharednotes.data.repository.UserRepository
import com.pproject.sharednotes.presentation.navigation.AppScreens
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.count
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.toList
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
    private val savedStateHandle: SavedStateHandle,
    private val userRepository: UserRepository,
    private val preferencesRepository: PreferencesRepository,
) : ViewModel() {
    var uiState by mutableStateOf(LoginUiState())
        private set

    fun updateUsername(newUsername: String) {
        uiState = uiState.copy(username = newUsername)
    }

    fun updatePassword(newPassword: String) {
        uiState = uiState.copy(password = newPassword)
    }

    fun emptyCredentials() {
        uiState = uiState.copy(username = "", password = "")
    }

    fun updateRemember(newRemember: Boolean) {
        uiState = uiState.copy(remember = newRemember)
    }

    fun tryLogOnActiveUser(navController: NavController) = viewModelScope.launch {
        preferencesRepository.getActivePreference().firstOrNull()?.let {
            navController.navigate("${AppScreens.HomeScreen.route}/${it.username}")
        }
    }

    fun loginUser(navController: NavController) = viewModelScope.launch {
        if (userRepository.getUser(uiState.username).firstOrNull() != null) {
            navController.navigate("${AppScreens.HomeScreen.route}/${uiState.username}")
            preferencesRepository.insert(
                Preferences(
                    username = uiState.username,
                    activeSession = uiState.remember
                )
            )
        } else {
            Toast.makeText(
                navController.context,
                navController.context.getString(R.string.the_user_is_not_registered),
                Toast.LENGTH_SHORT
            )
                .show()
        }
    }

    fun checkCredentials(context: Context): Boolean {
        val passwordPattern = Regex("^(?=.*[0–9])(?=.*[a-z])(?=.*[A-Z])(?=\\S+$).{8,}$")
        val userPattern = Regex("^[a-zA-Z0-9]+([._]?[a-zA-Z0-9]+)*\$")

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

                return LoginViewModel(
                    savedStateHandle,
                    userRepository = application.container.userRepository,
                    preferencesRepository = application.container.preferencesRepository,
                ) as T
            }
        }
    }
}