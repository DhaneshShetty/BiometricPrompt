package com.example.biometricprompt;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.biometric.BiometricManager;
import androidx.biometric.BiometricPrompt;
import androidx.core.hardware.fingerprint.FingerprintManagerCompat;

import android.content.Context;
import android.hardware.fingerprint.FingerprintManager;
import android.os.Bundle;
import android.os.FileObserver;
import android.view.View;
import android.widget.Button;

import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import static androidx.biometric.BiometricConstants.ERROR_NEGATIVE_BUTTON;

public class MainActivity extends AppCompatActivity {
    private BiometricPrompt biometricPrompt=null;
    private Executor executor = Executors.newSingleThreadExecutor();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if(biometricPrompt==null){
            biometricPrompt=new BiometricPrompt(this,executor,callback);
        }
        Button auth=findViewById(R.id.authenticate);
        auth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkAndAuthenticate();
            }
        });
    }
    private void checkAndAuthenticate(){
        BiometricManager biometricManager=BiometricManager.from(this);
        BiometricPrompt.PromptInfo promptInfo = buildBiometricPrompt();
        biometricPrompt.authenticate(promptInfo);

    }
    private BiometricPrompt.PromptInfo buildBiometricPrompt()
    {
        return new BiometricPrompt.PromptInfo.Builder()
                .setTitle("Login")
                .setSubtitle("FingerPrint Authentication")
                .setDescription("Please place your finger on the sensor to unlock")
                .setDeviceCredentialAllowed(true)
                .build();

    }
    private void snack(String text)
    {
        View view=findViewById(R.id.view);
        Snackbar snackbar=Snackbar.make(view,text, BaseTransientBottomBar.LENGTH_LONG);
        snackbar.show();
    }
    private BiometricPrompt.AuthenticationCallback callback=new
            BiometricPrompt.AuthenticationCallback() {
                @Override
                public void onAuthenticationError(int errorCode, @NonNull CharSequence errString) {
                    if(errorCode==ERROR_NEGATIVE_BUTTON && biometricPrompt!=null)
                        biometricPrompt.cancelAuthentication();
                    snack((String) errString);
                }

                @Override
                public void onAuthenticationSucceeded(@NonNull BiometricPrompt.AuthenticationResult result) {
                    snack("Authenticated");
                }

                @Override
                public void onAuthenticationFailed() {
                    snack("Authentication Failed.Please Try Again!");
                }
            };
}

