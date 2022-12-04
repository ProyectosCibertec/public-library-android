package com.cibertec.movil_modelo_proyecto_2022_2.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.cibertec.movil_modelo_proyecto_2022_2.R;
import com.cibertec.movil_modelo_proyecto_2022_2.entity.Alumno;

import java.util.List;

public class AlumnoCrudAdapter extends ArrayAdapter<Alumno> {

    private Context context;
    private List<Alumno> lista;

    public AlumnoCrudAdapter(@NonNull Context context, int resource, @NonNull List<Alumno> lista) {
        super(context, resource, lista);
        this.context = context;
        this.lista = lista;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
        View row = inflater.inflate(R.layout.activity_alumno_crud_item, parent, false);

        Alumno objAlumno = lista.get(position);


        TextView txtID = row.findViewById(R.id.idCrudAlumnoItemID);
        txtID.setText(String.valueOf(objAlumno.getIdAlumno()));

        TextView txtNombres = row.findViewById(R.id.idCrudAlumnoItemNombre);
        txtNombres.setText(objAlumno.getNombres());

        TextView txtApellidos = row.findViewById(R.id.idCrudAlumnoItemApellido);
        txtApellidos.setText(objAlumno.getApellidos());



        if (position % 2 == 0) {
            txtID.setBackgroundColor(Color.rgb(245, 245, 220));
            txtApellidos.setBackgroundColor(Color.rgb(204, 255, 204));
            txtNombres.setBackgroundColor(Color.rgb(204, 255, 204));
        }


        return row;
    }
}
