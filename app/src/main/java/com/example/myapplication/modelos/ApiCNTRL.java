package com.example.myapplication.modelos;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import android.net.Uri;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;
import java.util.zip.GZIPInputStream;

public class ApiCNTRL {
    //URL que siempre vamos a usar para haceder a la base de datos
    private static String URL_BASE = "https://lexica.art/api/v1/search?q=";

    public static String generarPrediccion(String msg){
        //Contenido del json en String
        String content=null;
        //Connector con base de datos mediante http (REST)
        HttpURLConnection httpConn = null;
        try {
            //Generaremos la url
            URL url = new URL(URL_BASE + msg);
            //Crearemos la conexion con la base de datos
            httpConn = (HttpURLConnection) url.openConnection();
            //Recogeremos la informacion json de la url como output
            httpConn.setDoOutput(true);
            httpConn.setRequestProperty("Content-Type", "application/json");
            httpConn.setRequestProperty("Accept", "application/json");
            /**
             * Si la conexion a sido correcta guardaremos en el content el contenido json
             * a un string
             */
            if (httpConn.getResponseCode() == HttpURLConnection.HTTP_OK){
                StringBuilder sb = new StringBuilder();
                BufferedReader reader = new BufferedReader(
                        new InputStreamReader( httpConn.getInputStream() ));
                String line;
                while ((line = reader.readLine()) != null) {
                    sb.append(line);
                }
                content = sb.toString();
                reader.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            //Cerraremos la conexion con la base de datos externa
            if( httpConn != null ) httpConn.disconnect();
        }
        return content;
    }

    /**
     * Cogeremos siempre la primera imagen del json generado en el metodo anterior
     * @param JSON El json con todas las imagenes de la base de datos
     * @return La url de la imagen
     */
    public static String getURLImage(String JSON){
        String url=null;
        try {
            JSONObject jsonObjeto = new JSONObject(JSON);
            JSONArray imagenes = jsonObjeto.getJSONArray("images");
            url = imagenes.getJSONObject(0).getString("src");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return url;
    }
}
