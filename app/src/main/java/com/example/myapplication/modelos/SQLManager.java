package com.example.myapplication.modelos;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import androidx.annotation.Nullable;

import com.example.myapplication.modelos.Cliente;
import com.example.myapplication.modelos.IAImagen;

import java.util.ArrayList;

public class SQLManager extends SQLiteOpenHelper {
    //Nombre de la base de datos
    private static final String DB_AIESCENE = "DB_AIESCENE";
    //Nombre de las tablas
    private static final String DB_NAME_TABLE_CLIENTE = "DB_TABLE_CLIENTE";
    private static final String DB_NAME_TABLE_IMAGENES = "DB_TABLE_IMAGENES";
    //Version de la base de datos
    private static final int VERSION = 10;

    //Columnas de la tabla de Clientes
    private static final String COL_CLIENTE_NOMBRE = "NOMBRE";
    private static final String COL_CLIENTE_CONTRASENIA = "CONTRASENIA";

    //Columnas de la tabla de Imagenes
    private static final String COL_IMAGENES_NOMBRE_CLIENTE = "NOMBRE_CLIENTE";
    private static final String COL_IMAGENES_CODIGO_IMAGEN = "CODIGO_IMAGEN";
    private static final String COL_IMAGENES_NOMBRE = "NOMBRE_IMAGEN" ;
    private static final String COL_IMAGENES_DESCRIPCION = "DESCRIPCION";
    private static final String COL_IMAGENES_URL = "URL_IMAGEN";

    //Contexto donde se va hace las conexiones don la base de datos
    private Context context;

    /**
     * Constructor por defecto de la clase SQLManager
     * Introduciremos el nombre de la base de datos con su version
     * @param context
     */
    public SQLManager(@Nullable Context context) {
        super(context, DB_AIESCENE, null, VERSION);

        this.context = context;
    }

    /**
     * Este metodo solo se hará una vez, cuando se crea por primera vez la base de datos
     * en un dispositivo
     * @param sqLiteDatabase
     */
    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        //Crearemos la tabla de Clientes
        String CREATE_TABLE_CLIENTE = "CREATE TABLE " + DB_NAME_TABLE_CLIENTE +" ( " + COL_CLIENTE_NOMBRE
                + " TEXT primary key," + COL_CLIENTE_CONTRASENIA + " TEXT );";
        //Crearemos la tabla de las Imagenes
        String CREATE_TABLE_IMAGENES = "CREATE TABLE " + DB_NAME_TABLE_IMAGENES +" ( " + COL_IMAGENES_NOMBRE_CLIENTE
                + " TEXT," + COL_IMAGENES_CODIGO_IMAGEN + " TEXT," + COL_IMAGENES_NOMBRE+ " TEXT,"
                + COL_IMAGENES_DESCRIPCION + " TEXT," + COL_IMAGENES_URL+ " TEXT);";

        sqLiteDatabase.execSQL(CREATE_TABLE_CLIENTE);
        sqLiteDatabase.execSQL(CREATE_TABLE_IMAGENES);
    }

    /**
     * Este metodo se encarga de actualizar la base de datos cada vez que le hagamos cambios
     * en un dispositivo con la app ya instalada
     * @param db
     * @param i
     * @param i1
     */
    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
    }

    /**
     * En este metodo insertaremos un cliente en la base de datos metido por parametro
     * @param cliente
     * @return
     */
    public long insertarCliente(Cliente cliente){
        long error = -1;
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();

        values.put(COL_CLIENTE_NOMBRE,cliente.getNombre());
        values.put(COL_CLIENTE_CONTRASENIA,cliente.getContrasena());

        error = db.insert(DB_NAME_TABLE_CLIENTE,null,values);

        db.close();

        return error;
    }

    /**
     * En este metodo insertaremos una imagen en la base de datos metido por parametro
     * @param iaImagen
     * @return
     */
    public long insertarImagen(IAImagen iaImagen){
        long error = -1;
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues valores = new ContentValues();
        valores.put(COL_IMAGENES_NOMBRE_CLIENTE,iaImagen.getNombre_Cliente());
        valores.put(COL_IMAGENES_CODIGO_IMAGEN,iaImagen.getCodigo_Imagen());
        valores.put(COL_IMAGENES_NOMBRE,iaImagen.getNombre_Imagen());
        valores.put(COL_IMAGENES_DESCRIPCION,iaImagen.getDescripcion());
        valores.put(COL_IMAGENES_URL,iaImagen.getUrl());

        error = db.insert(DB_NAME_TABLE_IMAGENES,null,valores);

        db.close();

        return error;
    }

    /**
     * En este metodo comprobaremos si existe el cliente introducido por paramtero existe en la base
     * de datos
     * @param cliente
     * @return
     */
    public long existeCliente(Cliente cliente){
        int error = 0;
        SQLiteDatabase db = this.getReadableDatabase();

        String[] columnas = new String[]{COL_CLIENTE_NOMBRE};
        String[] where = new String[]{cliente.getNombre()};

        Cursor cursor = db.query(DB_NAME_TABLE_CLIENTE,columnas,"NOMBRE = ?",where,null,null,null);;

        error = cursor.getCount();

        db.close();
        cursor.close();

        return error;
    }

    /**
     * En este metodo comprobaremos si la contraseña del cliente introducido por paramtero existe
     * en la base de datos
     * @param cliente
     * @return
     */
    public long existeClientePassword(Cliente cliente){
        int error = 0;
        SQLiteDatabase db = this.getReadableDatabase();

        String[] columnas = new String[]{COL_CLIENTE_NOMBRE};
        String[] where = new String[]{cliente.getNombre(),cliente.getContrasena()};

        Cursor cursor = db.query(DB_NAME_TABLE_CLIENTE,columnas,"NOMBRE = ? AND CONTRASENIA = ?",where,null,null,null);;

        error = cursor.getCount();

        db.close();
        cursor.close();

        return error;
    }

    /**
     * Recogeremos todos las las imagenes de la base de datos del nombre del usuario
     * @param nombre
     * @return
     */
    public ArrayList<IAImagen> imagenesUsuario(String nombre){
        ArrayList<IAImagen> imagenes = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        String[] columnas = new String[]{COL_IMAGENES_CODIGO_IMAGEN,COL_IMAGENES_NOMBRE,COL_IMAGENES_DESCRIPCION,COL_IMAGENES_URL};
        String[] where = new String[]{nombre};

        Cursor cursor = db.query(DB_NAME_TABLE_IMAGENES,columnas,"NOMBRE_CLIENTE = ? ",where,null,null,null,null);
        if (cursor.moveToNext()){
            int cod = 0;
            do {
                cod++;
                IAImagen iaImagen = new IAImagen();
                iaImagen.setNombre_Cliente(nombre);
                iaImagen.setCodigo_Imagen(Integer.parseInt(cursor.getString(0)));
                iaImagen.setNombre_Imagen(cursor.getString(1));
                iaImagen.setDescripcion(cursor.getString(2));
                iaImagen.setUrl(cursor.getString(3));
                imagenes.add(iaImagen);
            }while (cursor.moveToNext());
        }
        return imagenes;
    }

    /**
     * Actualizaremos la imagen introducida por parametro de la base de datos
     * @param imagenMod
     */
    public void updateImagen(IAImagen imagenMod){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues valores = new ContentValues();

        valores.put(COL_IMAGENES_DESCRIPCION,imagenMod.getDescripcion());

        String[] cod = new String[]{String.valueOf(imagenMod.getCodigo_Imagen())};

        db.update(DB_NAME_TABLE_IMAGENES,valores,"CODIGO_IMAGEN = ?",cod);

        db.close();
    }

    /**
     * Eliminaremos la imagen introducida por parametro de la base de datos
     * @param imagenDel
     * @return
     */
    public long deleteImagen(IAImagen imagenDel){
        long error;
        SQLiteDatabase db = this.getWritableDatabase();

        String[] cod = new String[]{""+imagenDel.getCodigo_Imagen()};

        error = db.delete(DB_NAME_TABLE_IMAGENES,"CODIGO_IMAGEN = ?",cod);
        Log.i(TAG, "deleteImagen: " + error);
        db.close();
        return  error;
    }

    /**
     * Recogeremose el codigo mas elevado de las imagenes de la base de datos
     * @param nombre
     * @return
     */
    public int setcodigos(String nombre){
        int codigoMax = 0;
        SQLiteDatabase db = this.getReadableDatabase();
        String[] columnas = new String[]{COL_IMAGENES_CODIGO_IMAGEN};
        String[] where = new String[]{nombre};

        Cursor cursor = db.query(DB_NAME_TABLE_IMAGENES,columnas,"NOMBRE_CLIENTE = ? ",where,null,null,null,null);

        if (cursor.moveToFirst()){
            do {
                int codigoTemp = Integer.parseInt(cursor.getString(0));
                if (codigoTemp > codigoMax){
                    codigoMax = codigoTemp;
                }
            }while (cursor.moveToNext());
        }
        return codigoMax;
    }
}
