package com.example.myapplication.modelos;

import android.os.Bundle;

import androidx.preference.PreferenceFragmentCompat;

import com.example.myapplication.R;

public class ConfigFragmento extends PreferenceFragmentCompat {
    /**
     * Creador del fragmento de las preferencias o configurador de esta aplicacion
     * @param savedInstanceState
     * @param rootKey
     */
    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        setPreferencesFromResource(R.xml.list_config,rootKey);
    }
}
