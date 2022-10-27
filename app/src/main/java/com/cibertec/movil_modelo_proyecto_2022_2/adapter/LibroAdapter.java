package com.cibertec.movil_modelo_proyecto_2022_2.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.cibertec.movil_modelo_proyecto_2022_2.R;
import com.cibertec.movil_modelo_proyecto_2022_2.entity.Libro;

import java.io.BufferedInputStream;
import java.io.InputStream;
import java.net.URL;
import java.util.List;

import retrofit2.http.Url;

public class LibroAdapter extends ArrayAdapter<Libro>  {

    private Context context;
    private List<Libro> lista;

    public LibroAdapter(@NonNull Context context, int resource, @NonNull List<Libro> lista) {
        super(context, resource, lista);
        this.context = context;
        this.lista = lista;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        LayoutInflater inflater = (LayoutInflater)context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
        View row = inflater.inflate(R.layout.activity_libro_consulta_item,parent,false);

        Libro objLibro = lista.get(position);

        TextView txtID= row.findViewById(R.id.idLibroItemId);
        txtID.setText(String.valueOf(objLibro.getIdLibro()));

        TextView txtTitulo= row.findViewById(R.id.idLibroItemRazSoc);
        txtTitulo.setText(objLibro.getTitulo());

        new Thread(new Runnable() {
            @Override
            public void run() {

                try{
                    String ruta ;
                    if(objLibro.getCategoria().getIdCategoria()== 1){
                        ruta="https://i.postimg.cc/bN2zRrVm/novela.jpg";
                    }else if(objLibro.getCategoria().getIdCategoria()== 2){
                        ruta="https://i.postimg.cc/66PWRnc7/cuentos.png";
                    }else if(objLibro.getCategoria().getIdCategoria()== 3) {
                        ruta="https://i.postimg.cc/8cxTSgcd/poesia.jpg";
                    } else {
                        ruta="https://i.postimg.cc/3Nk3Vx8J/emciclopedia.jpg";
                    }

                    URL rutaImagen = new URL(ruta);
                    InputStream is = new BufferedInputStream(rutaImagen.openStream());
                    Bitmap b = BitmapFactory.decodeStream(is);
                    ImageView vista = row.findViewById(R.id.idLibroItemImagen);
                    vista.setImageBitmap(b);

                }catch (Exception e){

                    e.printStackTrace();
                }


            }
        }).start();


        return row;
    }
}
