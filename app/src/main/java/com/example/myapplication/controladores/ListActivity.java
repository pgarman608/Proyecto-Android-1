package com.example.myapplication.controladores;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.PreferenceManager;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.R;
import com.example.myapplication.modelos.ReciclerAdapterImag;
import com.example.myapplication.modelos.IAImagen;
import com.example.myapplication.modelos.SQLManager;

import java.util.ArrayList;

public class ListActivity extends AppCompatActivity {
    //Componentes de la Actividad relacionada
    private RecyclerView recyclerView;
    private TextView tvVacio;
    //ReciclerAdapter de las imagenes
    private ReciclerAdapterImag imageHolder;
    //Identificadores de los Intents
    public static final int SEE = 0;
    public static final int ADD = 1;
    public static final int MOD = 2;

    //ArrayList de las imagenes
    private ArrayList<IAImagen> imagenes;

    private SQLManager sqlCliente;
    public static IAImagen iaSelect;
    //Layout del reciclerview
    private GridLayoutManager matrizlayout;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);
        //Modificaremos la barra de notificaciones para que sea del color que eligamos en mi caso un rosa
        getWindow().setStatusBarColor(Color.parseColor("#FCC2FC"));
        //Modificamos la barra de navegacion para que sea del color que eligamos en mi caso un rosa
        getWindow().setNavigationBarColor(Color.parseColor("#FCC2FC"));
        ActionBar barra = getSupportActionBar();
        //Modificamos el color del fondo del actionbar junto al texto con un color predefinido
        barra.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#FCC2FC")));
        barra.setTitle(Html.fromHtml("<font color='#006ca0'>Imagenes Guardadas</font>"));
        //Llamaremos al metodo para que rellene la lista de imagenes
        rellenarArray();

        sqlCliente = new SQLManager(ListActivity.this);

        //Inicializaremos los componentes de la actividad
        recyclerView = (RecyclerView) findViewById(R.id.rvImagenes);
        tvVacio = (TextView) findViewById(R.id.tvVacio);

        imageHolder = new ReciclerAdapterImag(imagenes);

        //Si el numero de imagenes de la lista no esta vacia quitaremos el texto del tvVacio
        if (imagenes.size() > 0){
            tvVacio.setText("");
        }
        //Inidicaremos el nunero de columnas gracias a las preferencias
        int col = loadPreferences();
        matrizlayout= new GridLayoutManager(this,col);

        recyclerView.setLayoutManager(matrizlayout);
        recyclerView.setAdapter(imageHolder);
        //Recogeremos la imagen clickada en el reciclerview y llamaremos al menu flotante
        imageHolder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int select = recyclerView.getChildAdapterPosition(view);
                iaSelect = imagenes.get(select);
                registerForContextMenu(view);
            }
        });
    }

    //Guardaremos todas las imagenes del cliente en el array la lista de imagenes
    private void rellenarArray(){
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                sqlCliente = new SQLManager(ListActivity.this);
                imagenes = sqlCliente.imagenesUsuario(LoginActivity.clienteLog.getNombre());
            }
        });
    }

    //Modificaremos el Actionbar con el menu_simple_list
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_simple_list,menu);
        return true;
    }

    //Generador del menu flotante
    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        getMenuInflater().inflate(R.menu.menu_float_list,menu);
    }

    /**
     * Este metodo se llamara cada vez que seleccionamos un elemento el menu flotante
     * @param item
     * @return
     */
    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item) {
        int itemId = item.getItemId();
        Intent intent = null;
        switch (itemId){
            //Cuando seleccionamos el boton de ver y modificar, iniciaremos un Intent donde pasaremos
            //la informacion de la imagen seleccionada
            case R.id.btnVer:
                intent = new Intent(ListActivity.this,AddActivity.class);
                intent.putExtra("name",iaSelect.getNombre_Imagen());
                intent.putExtra("desc",iaSelect.getDescripcion());
                intent.putExtra("url",iaSelect.getUrl());
                intent.setAction("mostrar");
                startActivityForResult(intent,SEE);
                break;
            case R.id.btnMod:
                intent = new Intent(ListActivity.this,AddActivity.class);
                intent.putExtra("cod",""+iaSelect.getCodigo_Imagen());
                intent.putExtra("name",iaSelect.getNombre_Imagen());
                intent.putExtra("desc",iaSelect.getDescripcion());
                intent.putExtra("url",iaSelect.getUrl());
                intent.setAction("editar");
                startActivityForResult(intent,MOD);
                break;
            //Eliminaremos de la base de datos la imagen seleccionada
            case R.id.btnDel:
                AlertDialog alertaDelete = createAlert("Elimnar Image","¿Desea eliminar la imagen?");
                alertaDelete.show();
                break;
        }
        return true;
    }

    /**
     * Este metodo se llama cada vez que que pulsemos un elemento del actionbar
     * @param item
     * @return
     */
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int itemId = item.getItemId();
        Intent intent=null;
        switch (itemId){
            case R.id.btnMenuAdd:
                //Abrira la actividad de AddActivity para poder añadir
                intent = new Intent(ListActivity.this,AddActivity.class);
                intent.setAction("aniadir");
                startActivityForResult(intent,ADD);
                break;
            case R.id.btnMenuPreferencias:
                //Abrira las preferencias de la config
                intent = new Intent(ListActivity.this,ConfigActivity.class);
                startActivity(intent);
                break;
        }
        return true;
    }

    /**
     * Cuando volvamos al actividad con los intents de añadir y modificar actualizaremos los
     * reciclerview y la lista de imagenes
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == ADD || requestCode == MOD){
            actualizarRV();
        }
    }

    //Actualizaremos los datos de reciclerview
    private void actualizarRV(){
        rellenarArray();
        IAImagen.setCodigos(sqlCliente.setcodigos(LoginActivity.clienteLog.getNombre()));
        imageHolder.setImagenes(imagenes);
        recyclerView.setAdapter(imageHolder);
        if (imagenes.size() > 0){
            tvVacio.setText("");
        }else{
            tvVacio.setText("No hay Imagenes");
        }
    }

    /**
     * Cargaremos las preferencias siempre que se salga de dicha actividad
     */
    @Override
    protected void onResume() {
        super.onResume();
        int col = loadPreferences();
        matrizlayout = new GridLayoutManager(this,col);
        recyclerView.setLayoutManager(matrizlayout);
    }

    //Cargador de las preferencias
    private int loadPreferences(){
        SharedPreferences config = PreferenceManager.getDefaultSharedPreferences(this);
        int col = Integer.parseInt(config.getString("numCol","3"));
        return col;
    }

    /**
     * Tendremos un alertdialog para preguntarle al usuario si está seguro de que quiera elimanar la imagen
     * @param titulo
     * @param texto
     * @return
     */
    private AlertDialog createAlert(String titulo,String texto){
        //Tendremos el constructor del alertdialog
        AlertDialog.Builder constructorAlertdialog = new AlertDialog.Builder(ListActivity.this);
        //Introduciremos el titulo y el texto introducido por parametro
        constructorAlertdialog.setTitle(titulo)
                .setMessage(texto);
        constructorAlertdialog.setPositiveButton("Si", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                //Eliminaremos la imagen si el ususario a elegido SI
                sqlCliente = new SQLManager(ListActivity.this);
                if (sqlCliente.deleteImagen(iaSelect) != 0){
                    actualizarRV();
                }
                Toast.makeText(ListActivity.this,"Imagen eliminada",Toast.LENGTH_SHORT).show();
            }
        });
        constructorAlertdialog.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                //No haremos nada si el usuario a dicho NO
            }
        });
        return constructorAlertdialog.create();
    }
}