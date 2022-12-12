package com.example.myapplication.modelos;

public class Cliente{
    //Nombre del cliente
    private String nombre;
    //Contraseña del cliente
    private String contrasena;

    //Constructor por parametro de la clase Clientes
    public Cliente(String nombre,String contrasena) {
        this.nombre = nombre;
        this.contrasena = contrasena;
    }
    //Constructor por defecto de la clase CLientes
    public Cliente(){
        this.nombre = "";
        this.contrasena = "";
    }

    //Getters
    public String getNombre() {
        return nombre;
    }

    public String getContrasena() {
        return contrasena;
    }

    //Setters
    public void setContrasena(String contrasena) {
        this.contrasena = contrasena;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    /**
     * Metodo que nos devuelve los atributos del cliente en un solo string
     * @return
     */
    @Override
    public String toString() {
        return this.nombre + "-" + this.contrasena;
    }

    /**
     * Comprobaremos que los atributos del cliente no esten bacios
     * @return
     */
    public int isEmpty(){
        int error = 0;
        if (this.contrasena.trim().isEmpty() || this.contrasena.trim().isEmpty()){
            error =-1;
        }
        return error;
    }
}