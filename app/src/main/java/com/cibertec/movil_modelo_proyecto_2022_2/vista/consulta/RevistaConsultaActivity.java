package com.cibertec.movil_modelo_proyecto_2022_2.vista.consulta;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.cibertec.movil_modelo_proyecto_2022_2.R;
import com.cibertec.movil_modelo_proyecto_2022_2.adapter.RevistaAdapter;
import com.cibertec.movil_modelo_proyecto_2022_2.entity.Revista;
import com.cibertec.movil_modelo_proyecto_2022_2.service.ServiceRevista;
import com.cibertec.movil_modelo_proyecto_2022_2.util.ConnectionRest;
import com.cibertec.movil_modelo_proyecto_2022_2.util.NewAppCompatActivity;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RevistaConsultaActivity extends NewAppCompatActivity {
    RecyclerView rclrViewRevistas;
    List<Revista> revistaList = new ArrayList<>();
    RevistaAdapter revistaAdapter;

    ServiceRevista serviceRevista;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_revista_consulta);

        rclrViewRevistas = (RecyclerView) findViewById(R.id.lstViewRevistas);
        revistaAdapter = new RevistaAdapter(revistaList);
        rclrViewRevistas.setAdapter(revistaAdapter);
        rclrViewRevistas.setLayoutManager(new LinearLayoutManager(this));
        rclrViewRevistas.scrollToPosition(0);

        serviceRevista = ConnectionRest.getConnection().create(ServiceRevista.class);
    }

    public void getRevistas(View view) {
        Call<List<Revista>> call = serviceRevista.getMagazines();

        call.enqueue(new Callback<List<Revista>>() {
            @Override
            public void onResponse(Call<List<Revista>> call, Response<List<Revista>> response) {
                if (response.isSuccessful()) {
                    List<Revista> body = response.body();
                    revistaList.clear();

                    if (body != null) {
                        revistaList.addAll(body);
                    }

                    revistaAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onFailure(Call<List<Revista>> call, Throwable t) {

            }
        });
    }
}