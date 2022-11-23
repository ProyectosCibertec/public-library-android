package com.cibertec.movil_modelo_proyecto_2022_2.vista.crud;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
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


public class LibroCrudFormularioActivity extends NewAppCompatActivity {

    TextView txtTitulo;
    Button btnEnviar,btnRegresar;

    Spinner spnCategoria;
    ArrayAdapter<String> adaptador;
    ArrayList<String> categorias = new ArrayList<String>();

    //Servicio
    ServiceLibro serviceLibro;
    ServiceCategoria serviceCategoria;

    //Componentes
    EditText txtTitulo2, txtAnio, txtSerie;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_libro_crud_formulario);

        txtTitulo2 = findViewById(R.id.IdCrudLibroFrmTituloLibro);
        txtAnio = findViewById(R.id.IdCrudLibroFrmLibro_Año);
        txtSerie = findViewById(R.id.IdCrudLibroFrmLibro_Serie);

        //Para Conectar el Rest

        serviceLibro = ConnectionRest.getConnection().create(ServiceLibro.class);
        serviceCategoria = ConnectionRest.getConnection().create(ServiceCategoria.class);

        //Adaptador
        adaptador = new ArrayAdapter<String>(this, androidx.appcompat.R.layout.support_simple_spinner_dropdown_item, categorias);
        spnCategoria = findViewById(R.id.IdCrudLibroFrmCategoria);
        spnCategoria.setAdapter(adaptador);

        cargaCategoria();


        txtTitulo = findViewById(R.id.idCrudLibroFrmTitulo);
        btnEnviar = findViewById(R.id.IdCrudLibroFrmbtnRegistrar);
        btnRegresar=findViewById(R.id.IdCrudLibroFrmbtnRegresar);


        Bundle extras = getIntent().getExtras();
        String tipo = extras.getString("var tipo");

        if(tipo.equals("REGISTRAR")){
            txtTitulo.setText("Mantenimiento Libro - REGISTRAR");
            btnEnviar.setText("REGISTRA");
        }else if(tipo.equals("ACTUALIZAR")){
            txtTitulo.setText("Mantenimiento Libro - ACTUALIZA");
            btnEnviar.setText("ACTUALIZA");

            Libro obj = (Libro) extras.get("var_item");

            txtTitulo2.setText(obj.getTitulo());
            txtSerie.setText(obj.getSerie());
            //txtAnio.setText(obj.getAnio());

        }

        btnRegresar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LibroCrudFormularioActivity.this,LibroCrudListaActivity.class);
                startActivity(intent);
            }
        });

        btnEnviar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String tit = txtTitulo2.getText().toString();
                String ani = txtAnio.getText().toString();
                String ser = txtSerie.getText().toString();


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

                    Bundle extras = getIntent().getExtras();
                    String tipo = extras.getString("var tipo");

                    if(tipo.equals("REGISTRAR")){
                        registra(objLibro);
                    }else if(tipo.equals("ACTUALIZAR")){
                        Libro obj = (Libro) extras.get("var_item");
                        objLibro.setIdLibro(obj.getIdLibro());
                        actualiza(objLibro);
                    }




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

                    Bundle extras = getIntent().getExtras();
                    String tipo = extras.getString("var tipo");

                    if(tipo.equals("ACTUALIZAR")){
                        Libro obj =(Libro) extras.get("var_item");

                        int posicion =-1;
                        String item=obj.getCategoria().getIdCategoria()+":"+obj.getCategoria().getDescripcion();
                        for(int i=0;i< categorias.size();i++){
                            if(categorias.get(i).equals(item)){
                                posicion = i;
                                break;
                            }
                        }
                        if(posicion != -1){
                            spnCategoria.setSelection(posicion);
                        }
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
    public void registra(Libro obj){
        Call<Libro> call = serviceLibro.insertaLibro(obj);
        call.enqueue(new Callback<Libro>() {
            @Override
            public void onResponse(Call<Libro> call, Response<Libro> response) {
                if (response.isSuccessful()){
                    Libro objSalida =   response.body();
                    mensajeAlert("Se registro el Libro " +
                            "\nID >> " + objSalida.getIdLibro() +
                            "\nTitulo >> " + objSalida.getTitulo() );

                }

            }

            @Override
            public void onFailure(Call<Libro> call, Throwable t) {
                mensajeToast("Error al acceder al Servicio Rest >>> " + t.getMessage());
            }
        });
    }

    public void actualiza(Libro obj){
        Call<Libro> call = serviceLibro.insertaLibro(obj);
        call.enqueue(new Callback<Libro>() {
            @Override
            public void onResponse(Call<Libro> call, Response<Libro> response) {
                if (response.isSuccessful()){
                    Libro objSalida =   response.body();
                    mensajeAlert("Se actualiazo el Libro " +
                            "\nID >> " + objSalida.getIdLibro() +
                            "\nTitulo >> " + objSalida.getTitulo() );

                }

            }

            @Override
            public void onFailure(Call<Libro> call, Throwable t) {
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