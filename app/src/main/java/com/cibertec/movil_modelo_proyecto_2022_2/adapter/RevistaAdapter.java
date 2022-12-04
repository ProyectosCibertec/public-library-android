package com.cibertec.movil_modelo_proyecto_2022_2.adapter;

import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.cibertec.movil_modelo_proyecto_2022_2.R;
import com.cibertec.movil_modelo_proyecto_2022_2.entity.Revista;
import com.cibertec.movil_modelo_proyecto_2022_2.vista.crud.RevistaCrudEliminaActivity;
import com.cibertec.movil_modelo_proyecto_2022_2.vista.registra.RevistaRegistraActivity;

import java.io.BufferedInputStream;
import java.io.InputStream;
import java.net.URL;
import java.util.List;

public class RevistaAdapter extends RecyclerView.Adapter<RevistaAdapter.ViewHolder> {
    private final List<Revista> lista;

    public RevistaAdapter(@NonNull List<Revista> lista) {
        this.lista = lista;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.activity_revista_consulta_item, parent, false);

        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Revista obj = lista.get(position);

        holder.getTxtRevistaId().setText(String.valueOf(obj.getIdRevista()));
        holder.getTxtRevistaNombre().setText(obj.getNombre());
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    String ruta;
                    if (obj.getIdRevista() % 2 == 0) {
                        ruta = "https://static01.nyt.com/images/2021/09/02/fashion/02DIVERSITY-FASHION-4/merlin_193911462_b01eac35-35de-4b4c-8cd4-6d456efce25f-mobileMasterAt3x.jpg";
                    } else if (obj.getIdRevista() % 2 != 0) {
                        ruta = "https://www.lovehappensmag.com/blog/wp-content/uploads/2021/01/A1iiLPi1rL-785x1024.jpg";
                    } else {
                        ruta = "https://assets.bizclikmedia.net/321/759fd25a84e7a24eb6ea485025327825:376cf38c239acc692235b9c827fd427f/01-cover-mining-july2022.jpg";
                    }
                    URL rutaImagen = new URL(ruta);
                    InputStream is = new BufferedInputStream(rutaImagen.openStream());
                    Bitmap b = BitmapFactory.decodeStream(is);
                    ImageView vista = holder.getIdRevistaItemImagen();
                    vista.setImageBitmap(b);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    @Override
    public int getItemCount() {
        return lista.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView txtRevistaId;
        private final TextView txtRevistaNombre;
        private final ImageView idRevistaItemImagen;

        public ViewHolder(View v) {
            super(v);

            v.setOnClickListener(view -> {
                AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
                builder.setTitle("¿Qué desea hacer?");
                builder.setPositiveButton("Actualizar", (dialogInterface, j) -> {
                    Intent intent = new Intent(view.getContext(), RevistaRegistraActivity.class);
                    TextView txtRevistaId = view.findViewById(R.id.txtRevistaId);
                    intent.putExtra("ES_REGISTRO", false);
                    intent.putExtra("ID_REVISTA", Integer.parseInt(txtRevistaId.getText().toString()));
                    view.getContext().startActivity(intent);
                });
                builder.setNegativeButton("Eliminar", (dialogInterface, j) -> {
                    Intent intent = new Intent(view.getContext(), RevistaCrudEliminaActivity.class);
                    TextView txtRevistaId = view.findViewById(R.id.txtRevistaId);
                    intent.putExtra("ID_REVISTA", Integer.parseInt(txtRevistaId.getText().toString()));
                    view.getContext().startActivity(intent);
                });
                AlertDialog dialog = builder.create();
                dialog.show();
            });

            txtRevistaId = (TextView) v.findViewById(R.id.txtRevistaId);
            txtRevistaNombre = (TextView) v.findViewById(R.id.txtRevistaNombre);
            idRevistaItemImagen = (ImageView) v.findViewById(R.id.idRevistaItemImagen);
        }

        public TextView getTxtRevistaId() {
            return txtRevistaId;
        }

        public TextView getTxtRevistaNombre() {
            return txtRevistaNombre;
        }

        public ImageView getIdRevistaItemImagen() {
            return idRevistaItemImagen;
        }
    }
}
