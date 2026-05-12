package com.example.taller3.util


import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import com.example.taller3.AuthViewModel
import com.example.taller3.R

@Composable
fun ButtonShared(text: String, action : ()->Unit ) {
    Button(
        onClick = action, modifier = Modifier.fillMaxWidth(),
        colors = ButtonColors(
            contentColor = Color.White,
            containerColor = colorResource(R.color.Rojo),
            disabledContainerColor = Color.Gray,
            disabledContentColor = Color.Gray
        )
    ) {
        Text(text)
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