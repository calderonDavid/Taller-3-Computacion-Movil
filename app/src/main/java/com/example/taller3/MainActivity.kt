package com.example.taller3

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.example.taller3.navigation.Navigation
import com.google.firebase.auth.FirebaseAuth

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        val trackUserId = intent.getStringExtra("TRACK_USER_ID")
        val currentUser = FirebaseAuth.getInstance().currentUser
        setContent {
            Navigation(currentUser,trackUserId)
        }
    }
}
