package com.silvertaurus.trader_go.helper

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.ContextCompat

object PermissionRequester {

    @Composable
    fun NotificationPermissionRequester(
        onGranted: (() -> Unit)? = null,
        onDenied: (() -> Unit)? = null
    ) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            BasePermissionRequester(Manifest.permission.POST_NOTIFICATIONS, onGranted, onDenied)
        }
    }


    @Composable
    private fun BasePermissionRequester(
        permission: String,
        onGranted: (() -> Unit)? = null,
        onDenied: (() -> Unit)? = null
    ) {
        val context = LocalContext.current
        var shouldRequest by remember { mutableStateOf(false) }

        val launcher = rememberLauncherForActivityResult(
            contract = ActivityResultContracts.RequestPermission()
        ) { isGranted ->
            if (isGranted) {
                Toast.makeText(context, "Granted 🎉", Toast.LENGTH_SHORT).show()
                onGranted?.invoke()
            } else {
                Toast.makeText(context, "Denied ❌", Toast.LENGTH_SHORT).show()
                onDenied?.invoke()
            }
        }

        LaunchedEffect(Unit) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                val granted = ContextCompat.checkSelfPermission(
                    context,
                    permission
                ) == PackageManager.PERMISSION_GRANTED

                if (!granted) {
                    shouldRequest = true
                } else {
                    onGranted?.invoke()
                }
            } else {
                onGranted?.invoke()
            }
        }

        LaunchedEffect(shouldRequest) {
            if (shouldRequest) {
                shouldRequest = false
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                    launcher.launch(permission)
                }
            }
        }
    }
}