package com.example.fingerprintandbiometric.auth.provider

import android.content.Context
import android.os.Build
import androidx.annotation.RequiresApi
import com.example.fingerprintandbiometric.auth.biometric.BiometricAuthManager
import com.example.fingerprintandbiometric.auth.exceptions.AuthManagerProvideException
import com.example.fingerprintandbiometric.auth.extensions.checkDeviceFingerprintState
import com.example.fingerprintandbiometric.auth.extensions.isBiometricSupported
import com.example.fingerprintandbiometric.auth.fingerprint.FingerprintAuthManager
import com.example.fingerprintandbiometric.auth.interfaces.AuthManager
import com.example.fingerprintandbiometric.auth.interfaces.AuthenticationListener
import com.example.fingerprintandbiometric.auth.state.FingerprintState

@RequiresApi(api = Build.VERSION_CODES.M)
class AuthManagerProvider {
    companion object {
        @Throws(AuthManagerProvideException::class)
        fun provideAuthManager(context: Context, authenticationListener: AuthenticationListener): AuthManager {
            val fingerprintState = context.checkDeviceFingerprintState()
            return if (fingerprintState == FingerprintState.FINGERPRINT_ALLOW) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P && context.isBiometricSupported()) {
                    BiometricAuthManager(context).apply {
                        attachAuthListener(authenticationListener)
                    }
                } else {
                    FingerprintAuthManager(context).apply {
                        attachAuthListener(authenticationListener)
                    }
                }
            } else {
                throw AuthManagerProvideException(fingerprintState)
            }
        }
    }
}