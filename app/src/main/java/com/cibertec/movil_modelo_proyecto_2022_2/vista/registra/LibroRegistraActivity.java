package com.cibertec.movil_modelo_proyecto_2022_2.vista.registra;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;

import com.cibertec.movil_modelo_proyecto_2022_2.R;
import com.cibertec.movil_modelo_proyecto_2022_2.entity.Categoria;
import com.cibertec.movil_modelo_proyecto_2022_2.entity.Libro;
import com.cibertec.movil_modelo_proyecto_2022_2.service.ServiceCategoria;
import com.cibertec.movil_modelo_proyecto_2022_2.service.ServiceLibro;
import com.cibertec.movil_modelo_proyecto_2022_2.util.ConnectionRest;
import com.cibertec.movil_modelo_proyecto_2022_2.util.FunctionUtil;
import com.cibertec.movil_modelo_proyecto_2022_2.util.NewAppCompatActivity;
import com.cibertec.movil_modelo_proyecto_2022_2.util.ValidacionUtil;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class LibroRegistraActivity extends NewAppCompatActivity {

    Spinner spnCategoria;
    ArrayAdapter<String> adaptador;
    ArrayList<String> categorias = new ArrayList<String>();

    //Servicio
    ServiceLibro serviceLibro;
    ServiceCategoria serviceCategoria;

    //Componentes
    EditText txtTitulo, txtAnio, txtSerie;
    Button btnRegistrar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_libro_registra);

        txtTitulo = findViewById(R.id.txt_RegLibro_Titulo);
        txtAnio = findViewById(R.id.txt_RegLibro_Año);
        txtSerie = findViewById(R.id.txt_RegLibro_Serie);
        btnRegistrar =findViewById(R.id.btnRegistrar);

        //Para Conectar el Rest

        serviceLibro = ConnectionRest.getConnection().create(ServiceLibro.class);
        serviceCategoria = ConnectionRest.getConnection().create(ServiceCategoria.class);

        //Adaptador
        adaptador = new ArrayAdapter<String>(this, androidx.appcompat.R.layout.support_simple_spinner_dropdown_item, categorias);
        spnCategoria = findViewById(R.id.spnRegistCategoria);
        spnCategoria.setAdapter(adaptador);

        btnRegistrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String tit = txtTitulo.getText().toString();
                String ani = txtAnio.getText().toString();
                String ser = txtSerie.getText().toString();


                if (!tit.matches(ValidacionUtil.TEXTO)){
                    mensajeToast("El Titulo es de 2 a 20 caracteres");
                }else if (!ani.matches(ValidacionUtil.ANIO)){
                    mensajeToast("El año tiene que tener 4 digitos");
                }else if (!ser.matches(ValidacionUtil.TEXTO)){
                    mensajeToast("Serie es de 2 a 20 caracteres");
                }else{
                    String categoria = spnCategoria.getSelectedItem().toString();
                    String idCategoria = categoria.split(":")[0];

                    Categoria objCategoria = new Categoria();
                    objCategoria.setIdCategoria(Integer.parseInt(idCategoria));

                    Libro objLibro = new Libro();
                    objLibro.setTitulo(tit);
                    objLibro.setAnio(Integer.parseInt(ani));
                    objLibro.setSerie(ser);
                    objLibro.setFechaRegistro(FunctionUtil.getFechaActualStringDateTime());
                    objLibro.setEstado(1);
                    objLibro.setCategoria(objCategoria);
                    registra(objLibro);
                }

            }
        });

        cargaCategoria();
    }

    public void registra(Libro obj){
        Call<Libro> call = serviceLibro.insertaLibro(obj);
        call.enqueue(new Callback<Libro>() {
            @Override
            public void onResponse(Call<Libro> call, Response<Libro> response) {
                if (response.isSuccessful()){
                    Libro objSalida =   response.body();
                    mensajeAlert("Se registro el Libro " +
                            "\nID >> " + objSalida.getIdLibro() +
                            "\nRazón Social >> " + objSalida.getTitulo() );

                }

            }

            @Override
            public void onFailure(Call<Libro> call, Throwable t) {
                mensajeToast("Error al acceder al Servicio Rest >>> " + t.getMessage());
            }
        });
    }

    public void cargaCategoria(){
        Call<List<Categoria>> call = serviceCategoria.listaTodos();

        call.enqueue(new Callback<List<Categoria>>() {
            @Override
            public void onResponse(Call<List<Categoria>> call, Response<List<Categoria>> response) {
                if (response.isSuccessful()){
                    List<Categoria> lstPaises =  response.body();
                    for(Categoria objcategoria: lstPaises){
                        categorias.add(objcategoria.getIdCategoria() +":"+ objcategoria.getDescripcion());
                    }
                    adaptador.notifyDataSetChanged();
                }else{
                    mensajeToast("Error al acceder al Servicio Rest >>> ");
                }
            }

            @Override
            public void onFailure(Call<List<Categoria>> call, Throwable t) {
                mensajeToast("Error al acceder al Servicio Rest >>> " + t.getMessage());
            }
        });
    }

    public void mensajeToast(String mensaje){
        Toast toast1 =  Toast.makeText(getApplicationContext(),mensaje, Toast.LENGTH_LONG);
        toast1.show();
    }

    public void mensajeAlert(String msg){
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
        alertDialog.setMessage(msg);
        alertDialog.setCancelable(true);
        alertDialog.show();
    }



}