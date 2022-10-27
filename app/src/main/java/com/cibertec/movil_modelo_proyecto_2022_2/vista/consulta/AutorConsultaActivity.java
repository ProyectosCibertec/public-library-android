package com.cibertec.movil_modelo_proyecto_2022_2.vista.consulta;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

import com.cibertec.movil_modelo_proyecto_2022_2.R;
import com.cibertec.movil_modelo_proyecto_2022_2.adapter.AutorAdapter;
import com.cibertec.movil_modelo_proyecto_2022_2.entity.Autor;
import com.cibertec.movil_modelo_proyecto_2022_2.service.ServiceAutor;
import com.cibertec.movil_modelo_proyecto_2022_2.util.ConnectionRest;
import com.cibertec.movil_modelo_proyecto_2022_2.util.NewAppCompatActivity;

import java.util.ArrayList;
import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.http.Body;

public class AutorConsultaActivity extends NewAppCompatActivity {

    ListView idConsultarAutorListView;
    ArrayList<Autor>  autorList = new ArrayList<Autor>();
    AutorAdapter adaptador;
    Button btnConsultarAutor;

    ServiceAutor serviceAutor;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_autor_consulta);
        btnConsultarAutor = findViewById(R.id.btnConsultarAutor);

        idConsultarAutorListView = findViewById(R.id.idConsultarAutorListView);
        adaptador = new AutorAdapter(this, R.layout.activity_autor_consulta_item, autorList);
        idConsultarAutorListView.setAdapter(adaptador);

        serviceAutor = ConnectionRest.getConnection().create(ServiceAutor.class);

        btnConsultarAutor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listaAutor();
            }
        });
    }

    private void listaAutor(){
        Call<List<Autor>> call =  serviceAutor.getAutores();
        call.enqueue(new Callback<List<Autor>>() {
            @Override
            public void onResponse(Call<List<Autor>> call, Response<List<Autor>> response) {
                if (response.isSuccessful()){
                    List<Autor> lstSalida = response.body();
                    autorList.clear();
                    autorList.addAll(lstSalida);
                    adaptador.notifyDataSetChanged();
                }

            }
            @Override
            public void onFailure(Call<List<Autor>> call, Throwable t) {

            }
        });


    }
}




