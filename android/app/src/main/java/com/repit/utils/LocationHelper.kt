package com.repit.v2.utils

import android.annotation.SuppressLint
import android.content.Context
import android.location.Location
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import com.google.android.gms.tasks.CancellationTokenSource
import kotlinx.coroutines.tasks.await

class LocationHelper(private val context: Context) {

    private val fusedLocationClient = LocationServices.getFusedLocationProviderClient(context)

    /**
     * Captures the current location with high accuracy.
     * Requires ACCESS_FINE_LOCATION permission to be granted before calling.
     */
    @SuppressLint("MissingPermission") // Permission must be checked by caller
    suspend fun getCurrentLocation(): Location? {
        val cancellationTokenSource = CancellationTokenSource()
        return try {
            fusedLocationClient.getCurrentLocation(
                Priority.PRIORITY_HIGH_ACCURACY,
                cancellationTokenSource.token
            ).await()
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }
}
