package com.example.myapplication.controladores;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.Html;
import android.view.Menu;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.PreferenceManager;
import androidx.recyclerview.widget.GridLayoutManager;

import com.example.myapplication.R;
import com.example.myapplication.modelos.ConfigFragmento;

public class ConfigActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_config);
        ActionBar barra = getSupportActionBar();
        //Modificamos el color del fondo del actionbar junto al texto con un color predefinido
        barra.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#FCC2FC")));
        barra.setTitle(Html.fromHtml("<font color='#006ca0'>Crear Imagen</font>"));
        //Modificaremos la barra de notificaciones para que sea del color que eligamos en mi caso un rosa
        getWindow().setStatusBarColor(Color.parseColor("#FCC2FC"));
        //Modificamos la barra de navegacion para que sea del color que eligamos en mi caso un rosa
        getWindow().setNavigationBarColor(Color.parseColor("#FCC2FC"));

        //Iniciaremos la vista con el fragmento de la configuracion
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.configContainer,new ConfigFragmento())
                .commit();
    }

    /**
     * Cambiaremos el menu por el menu para salir de la Actividad
     * @param menu
     * @return
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_back,menu);
        return true;
    }

    /**
     * Cuando pulsemos la imagen del ActionBar («) saldremos de la actividad
     * @param item
     * @return
     */
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.btnBack:
                onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}

