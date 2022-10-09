package com.cibertec.movil_modelo_proyecto_2022_2.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.cibertec.movil_modelo_proyecto_2022_2.R;
import com.cibertec.movil_modelo_proyecto_2022_2.entity.Revista;

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

        return row;
    }
}
