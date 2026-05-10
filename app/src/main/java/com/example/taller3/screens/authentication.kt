package com.example.taller3.screens

import android.provider.CalendarContract
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
import androidx.compose.material3.SegmentedButtonDefaults.Icon
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


@Composable
fun login(controller : NavController, model : AuthViewModel = viweModel() ){
    val context = LocalContext.current
    val state by model.authState.collectAsState()
    LaunchedEffect(Unit) {

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
            value = "ejemplo@gmail.com",//state.email,
            onValueChange = {},//model.updateEmail(newValue = it)},
            label = { Text("email") },
            modifier = Modifier.fillMaxWidth(),
            supportingText = {
                Text("I")
            }
        )


        OutlinedTextField(
            value = "",
            onValueChange = {},
            label = { Text("Password") },
            modifier = Modifier.fillMaxWidth(),
            visualTransformation = PasswordVisualTransformation(),
            supportingText = {
                Text("P")
            }
        )
        /*

        MyButton("Login") {
            if (validateForm(model, state.email, state.password)){
                //Firebase
                auth.signInWithEmailAndPassword(state.email, state.password).addOnCompleteListener {
                    if(it.isSuccessful){
                        controller.navigate(AppScreens.home.name)
                    }else{
                        makeText(context, "Authentication failed", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }

    }

    }

*/
    }
}
/*
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
}*/



@Preview(showBackground = true)
@Composable
fun LoginPreview() {
    val navController = rememberNavController()
    login(navController)
}