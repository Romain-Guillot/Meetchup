package com.example.appprojet.ui.authentication;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import androidx.annotation.Nullable;

import com.example.appprojet.models.User;
import com.example.appprojet.repositories.FirebaseAuthenticationRepository;
import com.example.appprojet.utils.Callback;


/**
 * This service listen the user state provided by the [IAuthenticationRepository] implementation
 * of the app.
 *
 * When the user sign out, the repository send a null user value to all listeners, and so, here we
 * register a listener to the authentication repository and when a null user value is returned
 * through the callback we start the authentication activity.
 *
 * This service must only be started once (typically when the application is launch -> see the
 * MeetChupApplication class)
 */
public class ReturnToAuthenticationService extends Service {

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Intent authActivityIntent = new Intent(this, AuthenticationActivity.class);
        authActivityIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        FirebaseAuthenticationRepository.getInstance().addAuthStateListener(new Callback<User>() {
            @Override public void onFail(Exception e) { }
            @Override
            public void onSucceed(User result) {
                if (result == null)
                    startActivity(authActivityIntent);
            }
        });
        return START_NOT_STICKY;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
