package com.proyecto.novalearn.utils;

import android.content.Context;
import android.content.SharedPreferences;

public class SessionManager {

    private static final String PREF_NAME = "NovaLearnSession";
    private static final String KEY_EMAIL = "user_email";
    private static final String KEY_NAME = "user_name";
    private static final String KEY_PICTURE = "user_picture";
    private static final String KEY_TOKEN = "auth_token";

    private final SharedPreferences prefs;

    public SessionManager(Context context) {
        prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
    }

    public void guardarSesion(String email, String nombre, String foto, String token) {
        prefs.edit()
                .putString(KEY_EMAIL, email)
                .putString(KEY_NAME, nombre)
                .putString(KEY_PICTURE, foto)
                .putString(KEY_TOKEN, token)
                .apply();
    }

    public String getEmail() { return prefs.getString(KEY_EMAIL, null); }
    public String getNombre() { return prefs.getString(KEY_NAME, null); }
    public String getFoto() { return prefs.getString(KEY_PICTURE, null); }
    public String getToken() { return prefs.getString(KEY_TOKEN, null); }

    public boolean haySesion() { return getEmail() != null; }

    public void cerrarSesion() {
        prefs.edit().clear().apply();
    }
}