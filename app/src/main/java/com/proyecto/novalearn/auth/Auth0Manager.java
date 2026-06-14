package com.proyecto.novalearn.auth;

import android.content.Context;
import android.app.Activity;

import com.auth0.android.Auth0;
import com.auth0.android.authentication.AuthenticationException;
import com.auth0.android.callback.Callback;
import com.auth0.android.provider.WebAuthProvider;
import com.auth0.android.result.Credentials;
import com.proyecto.novalearn.R;

public class Auth0Manager {

    private static Auth0Manager instance;
    private final Auth0 auth0;

    private Auth0Manager(Context context) {
        auth0 = Auth0.getInstance(
                context.getString(R.string.auth0_client_id),
                context.getString(R.string.auth0_domain)
        );
    }

    public static Auth0Manager getInstance(Context context) {
        if (instance == null) {
            instance = new Auth0Manager(context.getApplicationContext());
        }
        return instance;
    }

    public static Auth0 getAuth0(Context context) {
        return getInstance(context).auth0;
    }

    public void logout(Activity activity, Callback<Void, AuthenticationException> callback) {
        WebAuthProvider.logout(auth0)
                .withScheme("com.proyecto.novalearn")
                .start(activity, callback);
    }
}