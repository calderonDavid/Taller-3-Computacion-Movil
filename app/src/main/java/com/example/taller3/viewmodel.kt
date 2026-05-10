package com.example.taller3

import androidx.lifecycle.ViewModel
import androidx.room.util.copy
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update


data class AuthState(
    val email : String = "",
    val password : String="",
    val emailError:String="",
    val passwordError:String=""
)

class AuthViewModel: ViewModel(){
    private val _authState = MutableStateFlow<AuthState>(AuthState())
    val authState = _authState.asStateFlow()

    fun updateEmail(newValue : String){
        _authState.update { it.copy(email=newValue) }
    }
    fun updatePassword(newValue : String){
        _authState.update { it.copy(password =newValue) }
    }
    fun updateEmailError(newValue : String){
        _authState.update { it.copy(emailError=newValue) }
    }
    fun updatePasswordError(newValue : String){
        _authState.update { it.copy(passwordError= newValue) }
    }
}