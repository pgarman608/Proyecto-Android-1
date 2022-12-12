package com.example.myapplication.modelos;

public class IAImagen {
    //Codigo comun de todas las imagenes
    private static int CODIGOS = 0;
    //Nombre del cliente, codigo, nombre de la imagen, descripcion y la url
    private String nombre_Cliente;
    private int codigo_Imagen;
    private String nombre_Imagen;
    private String descripcion;
    private String url;

    //Constructor por defecto de la clase CLientes
    public IAImagen(){
        this.nombre_Cliente = "";
        this.nombre_Imagen = "";
        this.descripcion = "";
        this.url = "";
    }
    //Constructor por parametro de la clase IAImagen
    public IAImagen(String nombre_Cliente, String nombre_Imagen, String descripcion, String url){
        this.nombre_Cliente = nombre_Cliente;
        this.nombre_Imagen = nombre_Imagen;
        this.descripcion = descripcion;
        this.url = url;
        this.CODIGOS++;
        this.codigo_Imagen=CODIGOS;
    }

    //Setters
    public static void setCodigos(int codigos){
        CODIGOS = codigos;
        CODIGOS++;
    }

    public void setCodigo_Imagen(int codigo_Imagen) {
        this.codigo_Imagen = codigo_Imagen;
    }

    public void setNombre_Imagen(String nombre_Imagen) {
        this.nombre_Imagen = nombre_Imagen;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public void setNombre_Cliente(String nombre_Cliente) {
        this.nombre_Cliente = nombre_Cliente;
    }

    //Getters
    public int getCodigo_Imagen() {
        return codigo_Imagen;
    }

    public String getNombre_Imagen() {
        return nombre_Imagen;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public String getNombre_Cliente() {
        return nombre_Cliente;
    }

    public String getUrl() {
        return url;
    }

    /**
     * Metodo que nos devuelve los atributos del IAImagen en un solo string
     * @return
     */
    @Override
    public String toString() {
        return "IAImagen{" +
                "nombre_Cliente='" + nombre_Cliente + '\'' +
                ", codigo_Imagen=" + codigo_Imagen +
                ", nombre_Imagen='" + nombre_Imagen + '\'' +
                ", descripcion='" + descripcion + '\'' +
                ", url='" + url + '\'' +
                '}';
    }
}
