package com.example.myapplication.controladores;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.myapplication.R;
import com.example.myapplication.modelos.Cliente;
import com.example.myapplication.modelos.SQLManager;
import com.prathameshmore.toastylibrary.Toasty;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener{
    //Componentes de la Actividad relacionada
    private TextView tvRegister;
    private EditText etNombre;
    private EditText etContrasena;
    private Button btLogin;
    //Codigo Intent para la actividad de registrarse
    public final static int REGISTERINT = 1;
    //Sesion del cliente
    public static Cliente clienteLog;
    //Biblioteca que nos ayudará a generar notificaciones simples
    private Toasty toasty;

    /**
     * Inizializaremos los componentes de la actividad principal, crearemos los listener necesarios y
     * modificaremos elementos de la interfaz
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //Inizializaremos la biblioteca de las notificicaciones
        toasty = new Toasty(this);
        //Modificaremos la barra de notificaciones para que sea del color que eligamos en mi caso un rosa
        getWindow().setStatusBarColor(Color.parseColor("#FCC2FC"));
        //Ocultaremos el actionbar
        getSupportActionBar().hide();
        //Modificamos la barra de navegacion para que sea del color que eligamos en mi caso un rosa
        getWindow().setNavigationBarColor(Color.parseColor("#FCC2FC"));

        //Inicializaremos los componentes de la actividad
        tvRegister = (TextView) findViewById(R.id.tvRegistrar);
        etNombre = (EditText) findViewById(R.id.etNombreLogin);
        etContrasena = (EditText) findViewById(R.id.etContrasenaLogin);
        btLogin = (Button) findViewById(R.id.btnLogin);

        //Generaremos los listeners para ell boton y el textView
        tvRegister.setOnClickListener(this);
        btLogin.setOnClickListener(this);
    }

    /**
     * Este metodo nos permite recoger los Intent devueltos de otras actividades para recoger el
     * cliente registrado
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        /**
         * Si el resultCode es correcto.
         * En ese caso rellenaremos los editText con la informacion del cliente añadido
         */
        if(resultCode == RESULT_OK){
            String[] cliente = data.getStringExtra("result").split("-");
            etNombre.setText(cliente[0]);
            etContrasena.setText(cliente[1]);
        }
    }

    /**
     * LLamaremos a las actividades de Registrarse y de la lista de imagenes según el componente
     * seleccionado
     * @param view
     */
    @Override
    public void onClick(View view) {
        Intent intent = null;
        switch (view.getId()) {
            //Si presionamos el componente textview tvRegistrar abre la actividad del registrar
            case R.id.tvRegistrar:
                intent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivityForResult(intent, REGISTERINT);
                break;
            //Si presionamos el componente button btnLogin abre la lista de imagenes del usuario registrado
            case R.id.btnLogin:
                //Guardaremos ese usuario en una variable global para que todas clases y actividades tengan acceso
                clienteLog = new Cliente(etNombre.getText().toString(), etContrasena.getText().toString());
                //Comprobaremos que no esté vacio
                if (clienteLog.isEmpty() == 0) {
                    SQLManager sqlCliente = new SQLManager(this);
                    //Comprobaremos que exista ese cliente en la base de datos
                    if (sqlCliente.existeCliente(clienteLog) != 0 ) {
                        //Comporbaremos que existe el cliente con la contraseña en la base de datos
                        if (sqlCliente.existeClientePassword(clienteLog)!= 0){
                            //Abriremos la lista de actividades con el Intent
                            intent = new Intent(LoginActivity.this, ListActivity.class);
                            startActivity(intent);
                            Toast.makeText(this,"Sesion aceptada",Toast.LENGTH_SHORT).show();
                        }else{
                            toasty.dangerToasty(this, "Contrasenia Incorrecta", Toasty.LENGTH_SHORT, Toasty.CENTER);
                        }
                    } else {
                        toasty.dangerToasty(this, "No existe el usuario", Toasty.LENGTH_SHORT, Toasty.CENTER);
                    }
                } else {
                    toasty.dangerToasty(this, "Introduce datos en los campos", Toasty.LENGTH_SHORT, Toasty.CENTER);
                }
                break;
        }
    }
}