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
import com.cibertec.movil_modelo_proyecto_2022_2.entity.Alumno;


import java.io.BufferedInputStream;
import java.io.InputStream;
import java.net.URL;
import java.util.List;

public class AlumnoAdapter extends ArrayAdapter<Alumno>  {

    private Context context;
    private List<Alumno> lista;

    public AlumnoAdapter(@NonNull Context context, int resource, @NonNull List<Alumno> lista) {
        super(context, resource, lista);
        this.context = context;
        this.lista = lista;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater)context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
        View row = inflater.inflate(R.layout.activity_alumno_consulta_item, parent, false);

        Alumno objAlumno = lista.get(position);



        TextView txtID = row.findViewById(R.id.txtConID);
        txtID.setText(String.valueOf(objAlumno.getIdAlumno()));

        TextView txtNombres = row.findViewById(R.id.txtConNombre);
        txtNombres.setText(objAlumno.getNombres());

        TextView txtApellidos = row.findViewById(R.id.txtConApellido);
        txtApellidos.setText(objAlumno.getApellidos());

        new Thread(new Runnable() {

            @Override

            public void run() {

                try {
                    String ruta ;
                    if (objAlumno.getIdAlumno() == 1){
                        ruta = "https://i.postimg.cc/Kz6S9Z4s/Screenshot-1.jpg";
                    }else if(objAlumno.getIdAlumno() == 2){
                        ruta = "https://i.postimg.cc/6qCsgDv6/Screenshot-2.jpg";
                    }else if(objAlumno.getIdAlumno() == 3){
                        ruta = "https://i.postimg.cc/DfGKSbMV/Screenshot-3.jpg";
                    }else if(objAlumno.getIdAlumno() == 128){
                        ruta = "https://i.postimg.cc/4xfC22NZ/Screenshot-4.jpg";
                    }else if(objAlumno.getIdAlumno() == 129){
                        ruta = "https://i.postimg.cc/QMC2hBTy/Screenshot-5.jpg";
                    }else if(objAlumno.getIdAlumno() == 130){
                        ruta = "https://i.postimg.cc/K8J667RT/Screenshot-6.jpg";
                    }else if(objAlumno.getIdAlumno() == 131){
                        ruta = "https://i.postimg.cc/R0YjFM1V/Screenshot-7.jpg";
                    }else if(objAlumno.getIdAlumno() == 132){
                        ruta = "https://i.postimg.cc/sXZbGtKP/Screenshot-8.jpg";
                    }else if(objAlumno.getIdAlumno() == 215){
                    ruta = "https://i.postimg.cc/Bb9kTMwr/Screenshot-9.jpg";
                    } else {
                        ruta = "https://i.postimg.cc/B63YdPxv/no-disponible.png";
                    }
                    URL rutaImagen  = new URL(ruta);
                    InputStream is = new BufferedInputStream(rutaImagen.openStream());
                    Bitmap b = BitmapFactory.decodeStream(is);
                    ImageView vista = row.findViewById(R.id.idAlumnoItemImagen);
                    vista.setImageBitmap(b);
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        }).start();







        return row ;
    }
}
