package com.example.myapplication.controladores;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.myapplication.R;
import com.example.myapplication.modelos.Cliente;
import com.example.myapplication.modelos.SQLManager;
import com.prathameshmore.toastylibrary.Toasty;

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener{
    //Componentes de la Actividad relacionada
    private TextView tvLogin;
    private EditText etNombre;
    private EditText etContrasena;
    private Button btRegister;
    //Clase que trabaja con la base de datos
    private SQLManager sqlCliente;
    //Biblioteca que nos ayudará a generar notificaciones simples
    private Toasty toasty;

    /**
     * Inizializaremos los componentes de la actividad principal, crearemos los listener necesarios y
     * modificaremos elementos de la interfaz
     * @param savedInstanceState
     */
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        //Inizializaremos la biblioteca de las notificicaciones
        toasty = new Toasty(RegisterActivity.this);
        //Modificaremos la barra de notificaciones para que sea del color que eligamos en mi caso un rosa
        getWindow().setStatusBarColor(Color.parseColor("#D9EE66"));
        //Ocultaremos el actionbar
        getSupportActionBar().hide();
        //Modificamos la barra de navegacion para que sea del color que eligamos en mi caso un rosa
        getWindow().setNavigationBarColor(Color.parseColor("#D9EE66"));

        //Inicializaremos los componentes de la actividad
        tvLogin = (TextView) findViewById(R.id.tvLogin);
        etNombre = (EditText) findViewById(R.id.etNombreRegister);
        etContrasena = (EditText) findViewById(R.id.etContrasenaRegister);
        btRegister = (Button) findViewById(R.id.btnRegister);

        //Inicializaremos el objeto que trabaja con la base de datos
        sqlCliente = new SQLManager(this);

        //Generaremos los listeners para ell boton y el textView
        tvLogin.setOnClickListener(this);
        btRegister.setOnClickListener(this);
    }

    /**
     * Volveremos a la actividad de Login con la informacion del usuario que hemos registrado o sin
     * ningun usuario registrado
     * @param view
     */
    @Override
    public void onClick(View view) {
        switch (view.getId()){
            //Volvemos a la actividad de login sin ninguna informacion
            case R.id.tvLogin:
                Intent intentLogin1 = new Intent();
                setResult(Activity.RESULT_CANCELED,intentLogin1);
                finish();
                break;
            //Volvemos a la actividad de login con la informacion del cliente y añadimos ese cliente a la base de datos
            //si no existe
            case R.id.btnRegister:
                //Guardaremos ese usuario en una variable global para que todas clases y actividades tengan acceso
                Cliente cliente = new Cliente(etNombre.getText().toString(),etContrasena.getText().toString());
                //Comprobaremos que no esté vacio
                if (cliente.isEmpty() == 0){
                    //Comporbaremos que existe existe el cliente con la contraseña en la base de datos
                    if (sqlCliente.existeCliente(cliente) != sqlCliente.insertarCliente(cliente)){
                        toasty.successToasty(this,"Usuario añadio",Toasty.BOTTOM,Toasty.LENGTH_SHORT);
                        Intent intentregister = new Intent();
                        intentregister.putExtra("result",cliente.toString());
                        setResult(Activity.RESULT_OK,intentregister);
                        finish();
                    }else{
                        toasty.dangerToasty(this,"Ya existe ese usuario",Toasty.LENGTH_SHORT,Toasty.CENTER);
                    }
                }else{
                    toasty.dangerToasty(this,"Introduce datos en los campos",Toasty.LENGTH_SHORT,Toasty.CENTER);
                }
                break;
        }
    }

}
