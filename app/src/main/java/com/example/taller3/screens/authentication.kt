package com.example.taller3.screens

import android.provider.CalendarContract
import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.taller3.AuthViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.taller3.navigation.AppScreens
import com.example.taller3.util.ButtonShared
import com.google.firebase.auth.FirebaseAuth

@Composable
fun login(controller : NavController, model : AuthViewModel = viewModel() ){
    val context = LocalContext.current
    val state by model.authState.collectAsState()
    val auth = FirebaseAuth.getInstance()
    LaunchedEffect(Unit) {
        auth.currentUser?.let{
            controller.navigate(AppScreens.home.name)
        }
    }

    Column(verticalArrangement = Arrangement.spacedBy(15.dp, Alignment.CenterVertically),
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.padding(20.dp).fillMaxSize()) {
        Icon(
            imageVector = Icons.Default.AccountCircle,
            contentDescription = "",
            modifier = Modifier.size(130.dp)
        )

        OutlinedTextField(
            value = state.email,
            onValueChange = {},
            label = { Text("email") },
            modifier = Modifier.fillMaxWidth(),
            supportingText = {
                Text(state.emailError)
            }
        )

        OutlinedTextField(
            value = state.password,
            onValueChange = {},
            label = { Text("Password") },
            modifier = Modifier.fillMaxWidth(),
            visualTransformation = PasswordVisualTransformation(),
            supportingText = {
                Text(state.passwordError)
            }
        )
        ButtonShared("Login"){
            if (validateForm(model, state.email, state.password)) {
                auth.signInWithEmailAndPassword(state.email,state.password).addOnCompleteListener {
                    if (it.isSuccessful) {
                        controller.navigate(AppScreens.home.name)
                    } else {
                        Toast.makeText(context,"Authentication failed",Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }

    }
}


fun validateForm(model: AuthViewModel,email:String, password:String):Boolean{
    if (email.isEmpty()){ model.updateEmailError("Email is empty")
        return false
    }else{model.updateEmailError("")}
    if(!validEmailAddress(email)){model.updateEmailError("Not a valid address")
        return false
    }else{model.updateEmailError("")}
    if(password.isEmpty()) {model.updatePasswordError("Password is empty")
        return false
    }else{model.updatePasswordError("")}
    if(password.length < 6) {model.updatePasswordError("Password is too short")
        return false
    }else{model.updatePasswordError("")}
    return true
}

fun validEmailAddress(email:String):Boolean{
    val regex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\$"
    return email.matches(regex.toRegex())
}

@Preview(showBackground = true)
@Composable
fun LoginPreview() {
    val navController = rememberNavController()
    login(navController)
}