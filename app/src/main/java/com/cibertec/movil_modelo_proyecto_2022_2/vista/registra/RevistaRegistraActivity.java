package com.cibertec.movil_modelo_proyecto_2022_2.vista.registra;

import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.cibertec.movil_modelo_proyecto_2022_2.R;
import com.cibertec.movil_modelo_proyecto_2022_2.entity.Modalidad;
import com.cibertec.movil_modelo_proyecto_2022_2.service.ServiceModalidad;
import com.cibertec.movil_modelo_proyecto_2022_2.util.ConnectionRest;
import com.cibertec.movil_modelo_proyecto_2022_2.util.NewAppCompatActivity;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RevistaRegistraActivity extends NewAppCompatActivity {
    private static Logger logger = Logger.getLogger(RevistaRegistraActivity.class.getName());

    Spinner spnModalidad;
    ArrayAdapter<String> adapter;
    List<String> modalidadList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_revista_registra);

        int SDK_INT = android.os.Build.VERSION.SDK_INT;

        if (SDK_INT > 8) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                    .permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }

        spnModalidad = findViewById(R.id.spnModalidad);

        adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, modalidadList);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spnModalidad.setAdapter(adapter);

        fillModalidadList();
    }

    private void fillModalidadList() {
        ServiceModalidad serviceModalidad = ConnectionRest.getConnection().create(ServiceModalidad.class);
        Call<List<Modalidad>> call = serviceModalidad.listaTodos();

        call.enqueue(new Callback<List<Modalidad>>() {
            @Override
            public void onResponse(Call<List<Modalidad>> call, Response<List<Modalidad>> response) {
                if (response.isSuccessful()) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                        Objects.requireNonNull(response.body())
                                .forEach(modality -> modalidadList.add(modality.getDescripcion()));
                        adapter.notifyDataSetChanged();
                    }
                } else {
                    logger.log(Level.SEVERE, "Response not successful, status code: {0}", response.code());
                }
            }

            @Override
            public void onFailure(Call<List<Modalidad>> call, Throwable t) {
                logger.log(Level.SEVERE, "There was an error creating the request {0}", t.getMessage());
            }
        });
    }
}