package com.cibertec.movil_modelo_proyecto_2022_2.vista.crud;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.cibertec.movil_modelo_proyecto_2022_2.R;
import com.cibertec.movil_modelo_proyecto_2022_2.adapter.RevistaAdapter;
import com.cibertec.movil_modelo_proyecto_2022_2.entity.Revista;
import com.cibertec.movil_modelo_proyecto_2022_2.service.ServiceRevista;
import com.cibertec.movil_modelo_proyecto_2022_2.util.ConnectionRest;
import com.cibertec.movil_modelo_proyecto_2022_2.util.NewAppCompatActivity;
import com.cibertec.movil_modelo_proyecto_2022_2.vista.registra.RevistaRegistraActivity;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class RevistaCrudListaActivity extends NewAppCompatActivity {

    private static Logger LOGGER = Logger.getLogger(RevistaCrudListaActivity.class.getName());
    RecyclerView lstCrudMantenimientoRevista;
    List<Revista> revistaList = new ArrayList<>();
    RevistaAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_revista_crud_lista);
        // Buscar revistas
        Button btnCrudFiltrarRevista = findViewById(R.id.btnCrudFiltrarRevista);
        btnCrudFiltrarRevista.setOnClickListener(view -> {
            TextView txtCrudNombreRevista = findViewById(R.id.txtCrudNombreRevista);
            String nombreRevista = txtCrudNombreRevista.getText().toString();
            buscarRevistas(nombreRevista);
        });
        // Registrar revistas
        Button btnCrudRegistrarRevista = findViewById(R.id.btnCrudRegistrarRevista);
        btnCrudRegistrarRevista.setOnClickListener(view -> {
            Intent intent = new Intent(this, RevistaRegistraActivity.class);
            startActivity(intent);
        });
        // Listar revistas
        lstCrudMantenimientoRevista = findViewById(R.id.lstCrudMantenimientoRevista);
        adapter = new RevistaAdapter(revistaList);
        lstCrudMantenimientoRevista.setAdapter(adapter);
        lstCrudMantenimientoRevista.setLayoutManager(new LinearLayoutManager(this));
        lstCrudMantenimientoRevista.scrollToPosition(0);
        obtenerRevistas();
    }

    private void buscarRevistas(String nombreRevista) {
        ServiceRevista service = ConnectionRest.getConnection().create(ServiceRevista.class);
        Call<List<Revista>> call = service.searchMagazines(nombreRevista);
        call.enqueue(new Callback<List<Revista>>() {
            @Override
            public void onResponse(Call<List<Revista>> call, Response<List<Revista>> response) {
                if (response.isSuccessful()) {
                    try {
                        if (response.body() != null) {
                            revistaList.clear();
                            revistaList.addAll(response.body());
                            adapter.notifyDataSetChanged();
                        }
                    } catch (Exception e) {
                        LOGGER.log(Level.WARNING, "Error connecting to Revista service");
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<List<Revista>> call, Throwable t) {

            }
        });
    }

    private void obtenerRevistas() {
        ServiceRevista service = ConnectionRest.getConnection().create(ServiceRevista.class);
        Call<List<Revista>> call = service.getMagazines();
        call.enqueue(new Callback<List<Revista>>() {
            @Override
            public void onResponse(Call<List<Revista>> call, Response<List<Revista>> response) {
                if (response.isSuccessful()) {
                    try {
                        if (response.body() != null) {
                            revistaList.clear();
                            revistaList.addAll(response.body());
                            adapter.notifyDataSetChanged();
                        }
                    } catch (Exception e) {
                        LOGGER.log(Level.WARNING, "Error connecting to Revista service");
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<List<Revista>> call, Throwable t) {

            }
        });
    }

}