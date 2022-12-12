package com.example.myapplication.controladores;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.swiperefreshlayout.widget.CircularProgressDrawable;

import com.aghajari.zoomhelper.ZoomHelper;
import com.bumptech.glide.Glide;
import com.example.myapplication.R;
import com.example.myapplication.modelos.ApiCNTRL;
import com.example.myapplication.modelos.IAImagen;
import com.example.myapplication.modelos.SQLManager;

public class AddActivity extends AppCompatActivity implements View.OnClickListener{
    //Componentes de la Actividad relacionada
    private EditText etNombre;
    private EditText etDescripcion;
    private Button btCrear;
    private Button btGuardar;
    private ConnectTask connectTask;
    private ImageView imageAdd;

    private String accion;
    private String image;
    //Esta es barra de pogreso circular de la base de datos
    private CircularProgressDrawable progressbar;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_image);
        //Modificaremos la barra de notificaciones para que sea del color que eligamos en mi caso un rosa
        getWindow().setStatusBarColor(Color.parseColor("#FCC2FC"));
        //Modificamos la barra de navegacion para que sea del color que eligamos en mi caso un rosa
        getWindow().setNavigationBarColor(Color.parseColor("#FCC2FC"));
        ActionBar barra = getSupportActionBar();
        //Modificamos el color del fondo del actionbar junto al texto con un color predefinido
        barra.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#FCC2FC")));
        barra.setTitle(Html.fromHtml("<font color='#006ca0'>Crear Imagen</font>"));

        //Inicializaremos los componentes de la actividad
        btCrear = (Button) findViewById(R.id.btnCrear);
        btGuardar = (Button) findViewById(R.id.btnAniadir);
        btGuardar.setVisibility(View.INVISIBLE);
        etNombre = (EditText) findViewById(R.id.etNombre);
        etDescripcion = (EditText) findViewById(R.id.etDescripcion);
        imageAdd = (ImageView) findViewById(R.id.imgAdd);

        //Crearemos ClickListener para los dos botones (btCrear,btGuardar) implementados en esta clase
        btCrear.setOnClickListener(this);
        btGuardar.setOnClickListener(this);

        //Recogeremos la accion del intent creado para abrir esta actividad
        accion = getIntent().getAction();
        /**
         * Si accion que hemos recogido es mostrar podremos hacer zoom a nuestra imagen.
         * El zoom que haremos será Gracias a la biblioteca ZoomHelper.
         * Solo podremos hacer zoom a la imagen visible
         */
        if (accion.equals("mostrar")){
            ZoomHelper.Companion.addZoomableView(imageAdd);
        }

        //Generaremos una pogressbar Circular para la carga de la imagen de la base de datos o de la
        //imagen recogida de las conexiones REST
        progressbar = new CircularProgressDrawable(this);
        progressbar.setStrokeWidth(10f);
        progressbar.setStyle(CircularProgressDrawable.LARGE);
        progressbar.setCenterRadius(30f);
        progressbar.start();
        //Mientras la accion sea mostrar o editar haremos los siguientes cambios
        switch (accion) {
            case "mostrar":
                /**
                 * Haremos invisible los botones y deshabilitaremos los editTexts
                 * Modificaremos el texto del Actionbar a Mostrar Imagen
                 * Recogeremos los String de name y desc del intent utilizado para abrir esta actividad y
                 * lo guardaremos en los editText correspondientes
                 * Cargaremos la imagen pasado por el intent al ImagenView gracias a la biblioteca GLIDE
                 */
                btCrear.setVisibility(View.INVISIBLE);
                btGuardar.setVisibility(View.INVISIBLE);
                etDescripcion.setEnabled(false);
                etNombre.setEnabled(false);
                barra.setTitle(Html.fromHtml("<font color='#006ca0'>Mostrar Imagen</font>"));
                Log.i(TAG, "onCreate: "+ getIntent().getStringExtra("name"));
                etNombre.setText(getIntent().getStringExtra("name"));
                etDescripcion.setText(getIntent().getStringExtra("desc"));
                Glide.with(AddActivity.this)
                        .load(getIntent().getStringExtra("url"))
                        .placeholder(progressbar)
                        .error(R.drawable.error404)
                        .into(imageAdd);
                break;
            case "editar":
                /**
                 * Haremos invisible el boton de crear
                 * Modificaremos el texto del boton de guardar a Modificar
                 * Deshabilitaremos el editText del nombre
                 * Modificaremos el texto del Actionbar a Editar Imagen
                 * Recogeremos los String de name y desc del intent utilizado para abrir esta actividad y
                 * lo guardaremos en los editText correspondientes
                 * Cargaremos la imagen pasado por el intent al ImagenView gracias a la biblioteca GLIDE
                 */
                btCrear.setVisibility(View.INVISIBLE);
                btGuardar.setVisibility(View.VISIBLE);
                btGuardar.setText("Modificar");
                etNombre.setEnabled(false);
                barra.setTitle(Html.fromHtml("<font color='#006ca0'>Editar Imagen</font>"));
                etNombre.setText(getIntent().getStringExtra("name"));
                etDescripcion.setText(getIntent().getStringExtra("desc"));
                Glide.with(AddActivity.this)
                        .load(getIntent().getStringExtra("url"))
                        .placeholder(progressbar)
                        .error(R.drawable.error404)
                        .into(imageAdd);
                break;
        }
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

    /**
     * OnClick que implementaremos en los botones.
     * En este metodo tendremos en cuenta que boton hemos seleccinamos y la accion del intent que
     * tengamos recogida
     * @param view
     */
    @Override
    public void onClick(View view) {
        SQLManager db = new SQLManager(AddActivity.this);
        Intent intent = null;
        IAImagen imagenTemp = null;
        switch (view.getId()){
            //Cuando se el boton de añadir
            case R.id.btnAniadir:
                //Comprobaremos que la accion es editar o añadir ya que la accion de mostrar no tiene
                //botones
                if (accion.equals("editar")){
                    /**
                     ** Crearemos una imagen temp a la cual vamos a añadir el codigo de la imagen a modificar
                     * y la descripcion modificada del editText
                     ** Pasaremos por parametro al metodo que se encarga de modificar la imagen en la
                     * base de datos
                     ** Saldremos de la actividad teniendo en cuenta que todo esté correcto
                      */
                    imagenTemp = new IAImagen();
                    imagenTemp.setCodigo_Imagen(Integer.parseInt(getIntent().getStringExtra("cod")));
                    imagenTemp.setDescripcion(etDescripcion.getText().toString());
                    db.updateImagen(imagenTemp);
                    intent = new Intent();
                    setResult(Activity.RESULT_OK,intent);
                    finish();
                }else{
                    /**
                     ** Crearemos una imagen temp a la cual vamos a añadir tabto el nombre,
                     * como descripcion y su url
                     ** Insertaremos esa imagen en la base de datos
                     ** Volveremos a la lista de imagenes
                     */
                    imagenTemp = new IAImagen(LoginActivity.clienteLog.getNombre(),
                            etNombre.getText().toString(),etDescripcion.getText().toString(),image);
                    db.insertarImagen(imagenTemp);
                    intent = new Intent();
                    Toast.makeText(this,"Imagen Guardada",Toast.LENGTH_SHORT).show();
                    setResult(Activity.RESULT_OK,intent);
                    finish();
                }
                break;
            case R.id.btnCrear:
                if (!etNombre.getText().toString().trim().isEmpty()){
                    //Llamaremos al asinctask creado en esta clase y lo ejecutaremos
                    connectTask = new ConnectTask();
                    connectTask.execute();
                }else{
                    Toast.makeText(this,"No has introducido nada en el nombre",Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

    /**
     * Esta clase privada nos creará un AsincTask para que nos permita acceder a la base de datos
     * externa (REST) para poder recoger la imagen
     */
    private class ConnectTask extends AsyncTask<Void, String, String> {

        /**
         * Una vez que se ejecute el AsyncTask volverá invisible los botones y haremos no editables
         * los editText
         */
        @Override
        protected void onPreExecute() {
            btCrear.setVisibility(View.INVISIBLE);
            etNombre.setEnabled(false);
            etDescripcion.setEnabled(false);
        }

        /**
         * Recogeremos la imagen con el nombre introducido en el editText del nombre
         * @param voids
         * @return Pasaremos el json de las imagenes recogida de la base de datos
         */
        @Override
        protected String doInBackground(Void... voids) {
            String allJSON = ApiCNTRL.generarPrediccion(etNombre.getText().toString());
            return allJSON;
        }

        /**
         ** Recogeremos el primer link del json recogido anterior y la pondremos con la biblioteca
         * GLIDE
         ** Habilitaremos el editText de la descripcion y haremos visible el boton para guardar la imagen
         * en nuestra base de datos
         * @param s links de las imagenes pasada por return del doInBackground
         */
        @Override
        protected void onPostExecute(String s) {
            String url = ApiCNTRL.getURLImage(s);
            Glide.with(AddActivity.this)
                    .load(url)
                    .placeholder(progressbar)
                    .error(R.drawable.error404)
                    .into(imageAdd);
            image = url;
            etDescripcion.setEnabled(true);
            btGuardar.setVisibility(View.VISIBLE);
            Toast.makeText(AddActivity.this,"Imagen generada",Toast.LENGTH_SHORT).show();
        }

        /**
         * Cuando cancelemos el hilo no harermos nada
         */
        public void cancel() {
        }
    }

    /**
     * Este metodo nos permitira implementar el zoom de la biblioteca ZoomHelper
     * @param ev
     * @return
     */
    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        return ZoomHelper.Companion.getInstance().dispatchTouchEvent(ev,this) || super.dispatchTouchEvent(ev);
    }
}