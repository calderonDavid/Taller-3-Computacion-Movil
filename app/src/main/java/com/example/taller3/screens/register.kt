package com.example.taller3.screens

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.taller3.AuthViewModel
import com.example.taller3.navigation.AppScreens
import com.example.taller3.util.ButtonShared

@Composable
fun register(controller: NavController, viewModel: AuthViewModel = viewModel()) {

    val state by viewModel.authState.collectAsState()

    val imagePicker = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri ->
        viewModel.updateImageUri(uri)
    }

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        item {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalAlignment = Alignment.Start
            ) {
                Text(text = "Profile Picture", fontSize = 20.sp, fontWeight = FontWeight.Bold)
                Button(
                    onClick = { imagePicker.launch("image/*") },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(if (state.imageUri == null) "Select Image" else "Image Selected")
                }

                Text(text = "First Name", fontSize = 20.sp, fontWeight = FontWeight.Bold)
                OutlinedTextField(
                    value = state.name,
                    onValueChange = { viewModel.updateName(it) },
                    modifier = Modifier.fillMaxWidth(),
                    label = { Text("Enter your first name") }
                )
                Spacer(modifier = Modifier.height(16.dp))

                Text(text = "Last Name", fontSize = 20.sp, fontWeight = FontWeight.Bold)
                OutlinedTextField(
                    value = state.lastname,
                    onValueChange = { viewModel.updateLastname(it) },
                    modifier = Modifier.fillMaxWidth(),
                    label = { Text("Enter your last name") }
                )
                Spacer(modifier = Modifier.height(16.dp))

                Text(text = "ID Number", fontSize = 20.sp, fontWeight = FontWeight.Bold)
                OutlinedTextField(
                    value = state.idNumber,
                    onValueChange = { viewModel.updateIdNumber(it) },
                    modifier = Modifier.fillMaxWidth(),
                    label = { Text("Enter your ID number") }
                )
                Spacer(modifier = Modifier.height(16.dp))

                Text(text = "Email", fontSize = 20.sp, fontWeight = FontWeight.Bold)
                OutlinedTextField(
                    value = state.email,
                    onValueChange = { viewModel.updateEmail(it) },
                    modifier = Modifier.fillMaxWidth(),
                    label = { Text("Enter your email") }
                )
                Spacer(modifier = Modifier.height(16.dp))

                Text(text = "Password", fontSize = 20.sp, fontWeight = FontWeight.Bold)
                OutlinedTextField(
                    value = state.password,
                    onValueChange = { viewModel.updatePassword(it) },
                    modifier = Modifier.fillMaxWidth(),
                    label = { Text("Enter your password") },
                    visualTransformation = PasswordVisualTransformation()
                )
                Spacer(modifier = Modifier.height(32.dp))

                ButtonShared(text = "Register") {
                    viewModel.register(
                        onSuccess = {
                            controller.navigate(AppScreens.home.name)
                        }
                    )
                }
            }
        }
    }
}
@Preview(showBackground = true, showSystemUi = true)
@Composable
fun RegisterPreview() {
    Register(
        controller = rememberNavController()
    )
}