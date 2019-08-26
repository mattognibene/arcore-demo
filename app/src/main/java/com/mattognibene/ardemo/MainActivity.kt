package com.mattognibene.ardemo

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.mattognibene.ardemo.common.CameraPermissionHelper
import android.widget.Toast
import com.google.ar.core.ArCoreApk
import com.google.ar.core.Session
import com.google.ar.core.ArCoreApk.InstallStatus
import com.google.ar.core.exceptions.UnavailableUserDeclinedInstallationException
import java.lang.Exception


class MainActivity : AppCompatActivity() {

    private var arSession: Session? = null
    private var userRequestedInstall = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    override fun onResume() {
        super.onResume()

        // Camera Checks
        if (!CameraPermissionHelper.hasCameraPermission(this)) {
            CameraPermissionHelper.requestCameraPermission(this)
            return
        }

        // Make sure Google Play Services for AR is installed and up to date.
        try {
            if (arSession == null) {
                when (ArCoreApk.getInstance().requestInstall(this, userRequestedInstall)) {
                    InstallStatus.INSTALLED -> {
                        // Success, create the AR session.
                        arSession = Session(this)
                    }
                    InstallStatus.INSTALL_REQUESTED -> {
                        // Ensures next invocation of requestInstall() will either return
                        // INSTALLED or throw an exception.
                        userRequestedInstall = false
                        return
                    }
                }
            }
        } catch (e: UnavailableUserDeclinedInstallationException) {
            // Display an appropriate message to the user and return gracefully.
            Toast.makeText(this, "TODO: handle exception $e", Toast.LENGTH_LONG)
                    .show()
            return
        } catch (e: Exception) {
            // Display an appropriate message to the user and return gracefully.
            Toast.makeText(this, "TODO: handle exception $e", Toast.LENGTH_LONG)
                    .show()
            return
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, results: IntArray) {
        if (!CameraPermissionHelper.hasCameraPermission(this)) {
            Toast.makeText(this, "Camera permission is needed to run this application", Toast.LENGTH_LONG)
                    .show()
            if (!CameraPermissionHelper.shouldShowRequestPermissionRationale(this)) {
                // Permission denied with checking "Do not ask again".
                CameraPermissionHelper.launchPermissionSettings(this)
            }
            finish()
        }
    }

}