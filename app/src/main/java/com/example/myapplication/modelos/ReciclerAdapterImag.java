package com.example.myapplication.modelos;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.myapplication.R;

import androidx.swiperefreshlayout.widget.CircularProgressDrawable;

import java.util.List;

public class ReciclerAdapterImag extends RecyclerView.Adapter<ReciclerAdapterImag.ImageHolder> {

    //Listar imagenes
    private List<IAImagen> imagenes;
    //View donde tengamos nuestro reciclerView
    private View view;
    //Este listener nos pernitira clickar
    private View.OnClickListener onClickListener;

    private CircularProgressDrawable progressBar;

    //Setters
    public void setOnClickListener (View.OnClickListener OnClickListener) {
        this.onClickListener = OnClickListener;
    }

    public void setImagenes(List<IAImagen> imagenes) {
        this.imagenes = imagenes;
    }

    //Getters
    public List<IAImagen> getImagenes(){
        return imagenes;
    }

    @Override
    public int getItemCount() {
        return imagenes.size();
    }

    //Contructor por parametro que le meteremo
    public ReciclerAdapterImag(List<IAImagen> imagenes){
        this.imagenes = imagenes;
    }

    /**
     * Este metodo se ejecutara cuando se crea el RecoclerAdapter
     * @param parent
     * @param viewType
     * @return
     */
    @NonNull
    @Override
    public ImageHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //tendremos un view que sera el item.xml
        view = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_list_item,parent,false);
        //Crearemos un imageHolder con el view anterior
        ImageHolder imageHolder = new ImageHolder(view);
        //Tambien tendra un onclicklistener
        view.setOnClickListener(onClickListener);
        //Devolveremos el imageholder para el siguiente metodo
        return imageHolder;
    }

    /**
     * Cargará las images al ImageHodler
     * @param holder
     * @param position
     */
    @Override
    public void onBindViewHolder(@NonNull ImageHolder holder, int position) {
        //Recogeremos la imagen con la posicion y la lista de imagenes
        IAImagen iaImagen = imagenes.get(position);

        //Generaremos una pogressbar Circular para la carga de la imagen de la lista de imagenes
        progressBar = new CircularProgressDrawable(view.getContext());
        progressBar.setStrokeWidth(10f);
        progressBar.setStyle(CircularProgressDrawable.LARGE);
        progressBar.setCenterRadius(30f);
        progressBar.start();

        //Cargaremos la imagen gracias a la biblioteca de GLIDE en la imagen del holder
        Glide.with(view)
                .load(iaImagen.getUrl())
                .placeholder(progressBar)
                .error(R.drawable.error404)
                .into(holder.iaImagen);
    }

    /**
     * Se encarga de manegar la informacion de los items
     */
    public class ImageHolder extends RecyclerView.ViewHolder {
        //ImagenView de la actividad list_imaegen
        ImageView iaImagen;

        //Constructor por parametro
        public ImageHolder(@NonNull View itemView) {
            super(itemView);
            //Inicializacion de la imagen del list_images
            iaImagen = (ImageView) itemView.findViewById(R.id.ivItem);
        }
    }
}
