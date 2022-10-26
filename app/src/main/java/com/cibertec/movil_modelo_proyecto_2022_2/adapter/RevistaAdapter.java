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
import com.cibertec.movil_modelo_proyecto_2022_2.entity.Revista;

import java.io.BufferedInputStream;
import java.io.InputStream;
import java.net.URL;
import java.util.List;

public class RevistaAdapter extends ArrayAdapter<Revista>  {

    private final Context context;
    private final List<Revista> lista;

    public RevistaAdapter(@NonNull Context context, int resource, @NonNull List<Revista> lista) {
        super(context, resource, lista);
        this.context = context;
        this.lista = lista;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View row = inflater.inflate(R.layout.activity_revista_consulta_item, parent, false);

        Revista obj = lista.get(position);

        TextView txtID = row.findViewById(R.id.txtRevistaId);
        txtID.setText(String.valueOf(obj.getIdRevista()));

        TextView  txtRazSoc = row.findViewById(R.id.txtRevistaNombre);
        txtRazSoc.setText(String.valueOf(obj.getNombre()));

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    String ruta ;
                    if (obj.getIdRevista() == 1){
                        ruta = "https://static01.nyt.com/images/2021/09/02/fashion/02DIVERSITY-FASHION-4/merlin_193911462_b01eac35-35de-4b4c-8cd4-6d456efce25f-mobileMasterAt3x.jpg";
                    }else if (obj.getIdRevista() == 2){
                        ruta = "https://www.lovehappensmag.com/blog/wp-content/uploads/2021/01/A1iiLPi1rL-785x1024.jpg";
                    }else{
                        ruta = "https://assets.bizclikmedia.net/321/759fd25a84e7a24eb6ea485025327825:376cf38c239acc692235b9c827fd427f/01-cover-mining-july2022.jpg";
                    }
                    URL rutaImagen  = new URL(ruta);
                    InputStream is = new BufferedInputStream(rutaImagen.openStream());
                    Bitmap b = BitmapFactory.decodeStream(is);
                    ImageView vista = row.findViewById(R.id.idRevistaItemImagen);
                    vista.setImageBitmap(b);
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        }).start();

        return row;
    }
}
