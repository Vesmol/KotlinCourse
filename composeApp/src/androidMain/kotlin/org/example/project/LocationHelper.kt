package org.example.project

import android.Manifest
import android.content.pm.PackageManager
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.ContextCompat
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.GoogleApiAvailability
import com.google.android.gms.location.LocationServices

@Composable
actual fun rememberLocationHelper(
    onLocation: (Double, Double) -> Unit
) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val googleApiAvailability = GoogleApiAvailability.getInstance()
    val isPlayServicesAvailable = googleApiAvailability.isGooglePlayServicesAvailable(context) == ConnectionResult.SUCCESS

    if (!isPlayServicesAvailable) {
        onLocation(0.0, 0.0)
        return
    }

    val fusedLocationClient = remember { LocationServices.getFusedLocationProviderClient(context) }

    val launcher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { granted ->
        if (granted) {
            fusedLocationClient.lastLocation
                .addOnSuccessListener { location ->
                    if (location != null) {
                        onLocation(location.latitude, location.longitude)
                    } else {
                        onLocation(0.0, 0.0)
                    }
                }
                .addOnFailureListener {
                    onLocation(0.0, 0.0)
                }
        } else {
            onLocation(0.0, 0.0)
        }
    }

    fun request() {
        when {
            ContextCompat.checkSelfPermission(
                context, Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED -> {
                fusedLocationClient.lastLocation
                    .addOnSuccessListener { location ->
                        if (location != null) {
                            onLocation(location.latitude, location.longitude)
                        } else {
                            onLocation(0.0, 0.0)
                        }
                    }
                    .addOnFailureListener {
                        onLocation(0.0, 0.0)
                    }
            }
            else -> launcher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
        }
    }

    request()
}