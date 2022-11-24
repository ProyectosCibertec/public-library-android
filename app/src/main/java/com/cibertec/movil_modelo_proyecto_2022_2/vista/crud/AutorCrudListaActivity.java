package com.cibertec.movil_modelo_proyecto_2022_2.vista.crud;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.cibertec.movil_modelo_proyecto_2022_2.R;
import com.cibertec.movil_modelo_proyecto_2022_2.util.NewAppCompatActivity;
import com.cibertec.movil_modelo_proyecto_2022_2.adapter.AutorAdapter;
import com.cibertec.movil_modelo_proyecto_2022_2.adapter.AutorCrudAdapter;
import com.cibertec.movil_modelo_proyecto_2022_2.entity.Autor;
import com.cibertec.movil_modelo_proyecto_2022_2.service.ServiceAutor;
import com.cibertec.movil_modelo_proyecto_2022_2.util.ConnectionRest;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class AutorCrudListaActivity extends NewAppCompatActivity {

    ListView lstAutor;
    ArrayList<Autor> data = new ArrayList<Autor>();
    AutorCrudAdapter adaptador;

    Button btnFiltrar, btnRegistrar, btnEliminar;
    TextView txtFiltrar;

    ServiceAutor serviceAutor;
   
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_autor_crud_lista);

        btnFiltrar = findViewById(R.id.idCrudAutorBtnFiltrar);
        btnRegistrar = findViewById(R.id.idCrudAutorBtnRegistrar);
        txtFiltrar = findViewById(R.id.idCrudAutorTxtFiltrar);

        lstAutor = findViewById(R.id.idCrudAutorListView);
        adaptador = new AutorCrudAdapter(this, R.layout.activity_autor_crud_item, data);
        lstAutor.setAdapter(adaptador);

        serviceAutor = ConnectionRest.getConnection().create(ServiceAutor.class);

        btnFiltrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String filtro = txtFiltrar.getText().toString();
                filtraPorNombre(filtro);
            }
        });

        btnRegistrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AutorCrudListaActivity.this, AutorCrudFormularioActivity.class);
                intent.putExtra("var_tipo", "REGISTRAR");
                startActivity(intent);
            }
        });

        lstAutor.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(AutorCrudListaActivity.this, AutorCrudFormularioActivity.class);
                intent.putExtra("var_tipo", "ACTUALIZAR");

                Autor obj = data.get(i);
                intent.putExtra("var_item", obj);

                startActivity(intent);
            }
        });

        //Muestre todos los datos al inicio
        filtraPorNombre("");
    }

    public void filtraPorNombre(String filtro){
        Call<List<Autor>> call = serviceAutor.listaAutorPorNombre(filtro);
        call.enqueue(new Callback<List<Autor>>() {
            @Override
            public void onResponse(Call<List<Autor>> call, Response<List<Autor>> response) {
                if (response.isSuccessful()){
                    List<Autor> salida = response.body();
                    data.clear();;
                    data.addAll(salida);
                    adaptador.notifyDataSetChanged();
                }

            }
            @Override
            public void onFailure(Call<List<Autor>> call, Throwable t) {

            }
        });
    }





}





