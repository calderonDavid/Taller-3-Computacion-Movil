package com.example.taller3.screens

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.os.Looper
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.taller3.view.MapViewModel
import com.example.taller3.R
import com.example.taller3.TrackingViewModel
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.*

@SuppressLint("MissingPermission")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun mapTracker(
    navController: NavController,
    targetUserId: String,
    mapViewModel: MapViewModel = viewModel(), // Reutilizamos este para la lógica de ubicación
    trackingViewModel: TrackingViewModel = viewModel()
) {
    val context = LocalContext.current
    val state by trackingViewModel.trackingState.collectAsState()

    LaunchedEffect(Unit) {
        trackingViewModel.initialize(context)
        trackingViewModel.startTracking(targetUserId)
    }
    val locationClient = remember { LocationServices.getFusedLocationProviderClient(context) }
    val locationRequest = mapViewModel.createLocationRequest()
    val locationCallback = remember {
        mapViewModel.createLocationCallback { result ->
            result.lastLocation?.let { loc ->
                trackingViewModel.updateMyPosition(loc.latitude, loc.longitude)
                mapViewModel.updateUserLocation(loc.latitude, loc.longitude) // También actualizamos nuestra propia pos en FB
            }
        }
    }

    DisposableEffect(Unit) {
        if (ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            locationClient.requestLocationUpdates(locationRequest, locationCallback, Looper.getMainLooper())
        }
        onDispose { locationClient.removeLocationUpdates(locationCallback) }
    }

    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(LatLng(4.6097, -74.0817), 12f)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = if (state.targetUser != null) "Siguiendo a ${state.targetUser!!.name}" else "Cargando...",
                        color = Color.White,
                        fontWeight = FontWeight.Bold
                    )
                },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Volver", tint = Color.White)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = colorResource(R.color.Rojo)
                )
            )
        }
    ) { padding ->
        Box(modifier = Modifier.fillMaxSize().padding(padding)) {
            val myMarkerState = rememberMarkerState()
            val targetMarkerState = rememberMarkerState()

            GoogleMap(modifier = Modifier.fillMaxSize(), cameraPositionState = cameraPositionState) {
                state.myLocation?.let {
                    myMarkerState.position = it
                    Marker(state = myMarkerState, title = "Yo", icon = BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE))
                }
                state.targetUser?.let { user ->
                    if(user.latitude != 0.0) {
                        targetMarkerState.position = LatLng(user.latitude, user.longitude)
                        Marker(state = targetMarkerState, title = user.name, snippet = "A ${state.distanceInKm} km")
                    }
                }
                if (state.routePoints.isNotEmpty()) {
                    Polyline(points = state.routePoints, color = colorResource(R.color.Rojo), width = 12f)
                }
            }

            if (state.targetUser != null && state.distanceInKm > 0.0) {
                ElevatedCard(
                    modifier = Modifier.align(Alignment.BottomCenter).fillMaxWidth().padding(16.dp),
                    elevation = CardDefaults.elevatedCardElevation(8.dp),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Column(
                        modifier = Modifier.fillMaxWidth().background(Color.White).padding(16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(text = "Distancia hacia ${state.targetUser!!.name}", fontSize = 16.sp, color = Color.Gray)
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(text = "${state.distanceInKm} km", fontSize = 24.sp, fontWeight = FontWeight.Bold, color = colorResource(R.color.Rojo))
                    }
                }
            }
        }
    }
}