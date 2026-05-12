package com.example.taller3.screens

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.os.Looper
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import com.example.taller3.R
import androidx.compose.runtime.*
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.core.content.ContextCompat
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.taller3.MapViewModel
import com.example.taller3.util.ButtonShared
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberPermissionState
import com.google.accompanist.permissions.shouldShowRationale
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.rememberCameraPositionState
import com.google.accompanist.permissions.isGranted

val locationPermission = Manifest.permission.ACCESS_FINE_LOCATION

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun home(controller: NavController, viewModel: MapViewModel = viewModel()) {
    val permission = rememberPermissionState(locationPermission)
    var showButton by remember { mutableStateOf(false) }

    SideEffect {
        if (!permission.status.isGranted) {
            if (permission.status.shouldShowRationale) {
                showButton = true
            } else {
                showButton = false
                permission.launchPermissionRequest()
            }
        }
    }

    if (permission.status.isGranted) {
        LocationWithRequest(controller, viewModel)
    } else {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            if (showButton) {
                Text("Access to GPS is Mandatory for this app.")
                ButtonShared("Request Location Permission"){
                    permission.launchPermissionRequest()
                }
            } else {
                Text("No access to location")
            }
        }
    }
}
@SuppressLint("UnrememberedMutableState")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LocationWithRequest(navController: NavController, viewModel: MapViewModel) {
    val context = LocalContext.current

    // Leemos el estado del ViewModel (Lat, Lng y la lista del JSON)
    val state by viewModel.mapState.collectAsState()

    // Cargamos el JSON una sola vez al entrar
    LaunchedEffect(Unit) {
        viewModel.loadPOIsFromJson(context)
    }

    val locationClient = remember { LocationServices.getFusedLocationProviderClient(context) }
    val locationRequest = createLocationRequest()

    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(LatLng(4.628308, -74.064929), 12f) // Default a Bogotá
    }

    val locationCallback = remember {
        createLocationCallback { result ->
            result.lastLocation?.let { location ->
                // 1. Mandamos al ViewModel la nueva posición (Esto la sube a Firebase!)
                viewModel.updateUserLocation(location.latitude, location.longitude)

                // 2. Movemos la cámara del mapa
                cameraPositionState.position = CameraPosition.fromLatLngZoom(
                    LatLng(location.latitude, location.longitude), 14f
                )
            }
        }

    }

    DisposableEffect(Unit) {
        if (ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            locationClient.requestLocationUpdates(locationRequest, locationCallback, Looper.getMainLooper())
        }
        onDispose {
            locationClient.removeLocationUpdates(locationCallback)
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Home", fontWeight = FontWeight.Bold) },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = colorResource(R.color.RojoOscuro),
                    titleContentColor = Color.White,
                    navigationIconContentColor = Color.White
                )
            )
        }
    ) { padding ->
        Box(modifier = Modifier.fillMaxSize().padding(padding)) {
            GoogleMap(
                modifier = Modifier.fillMaxSize(),
                cameraPositionState = cameraPositionState
            ) {
                if (state.currentLat != 0.0 && state.currentLng != 0.0) {
                    Marker(
                        state = MarkerState(LatLng(state.currentLat, state.currentLng)),
                        title = "Mi posición actual"
                    )
                }

                state.pointsOfInterest.forEach { poi ->
                    Marker(
                        state = MarkerState(LatLng(poi.latitude, poi.longitude)),
                        title = poi.name
                    )
                }
            }
        }
    }
}
fun createLocationRequest() : LocationRequest{
    val locationRequest = LocationRequest.Builder(
        Priority.PRIORITY_HIGH_ACCURACY, 10000)
        .setWaitForAccurateLocation(true)
        .setMinUpdateIntervalMillis(10000)
        .build()
    return locationRequest
}
fun createLocationCallback(onLocationChange : (LocationResult)-> Unit): LocationCallback {
    val callback = object : LocationCallback() {
        override fun onLocationResult(locationResult: LocationResult) {
            super.onLocationResult(locationResult)
            onLocationChange(locationResult)
        }
    }
    return callback
}