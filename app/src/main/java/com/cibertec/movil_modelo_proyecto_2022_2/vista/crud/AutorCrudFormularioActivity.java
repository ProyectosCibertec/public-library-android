package com.cibertec.movil_modelo_proyecto_2022_2.vista.crud;

import android.os.Bundle;

import android.content.Intent;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.cibertec.movil_modelo_proyecto_2022_2.R;
import com.cibertec.movil_modelo_proyecto_2022_2.util.NewAppCompatActivity;
import com.cibertec.movil_modelo_proyecto_2022_2.entity.Autor;
import com.cibertec.movil_modelo_proyecto_2022_2.entity.Grado;
import com.cibertec.movil_modelo_proyecto_2022_2.service.ServiceAutor;
import com.cibertec.movil_modelo_proyecto_2022_2.service.ServiceGrado;
import com.cibertec.movil_modelo_proyecto_2022_2.util.ConnectionRest;
import com.cibertec.movil_modelo_proyecto_2022_2.util.FunctionUtil;
import com.cibertec.movil_modelo_proyecto_2022_2.util.ValidacionUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AutorCrudFormularioActivity extends NewAppCompatActivity {

    Spinner spnGrado;
    ArrayAdapter<String> adaptador;
    ArrayList<String> grados = new ArrayList<String>();

    //Servicio
    ServiceAutor serviceAutor;
    ServiceGrado serviceGrado;

    //Componentes
    TextView txtTitulo;
    EditText txtNom, txtApe, txtFec, txtTel;
    Button btnEnviar, btnRegresar;

    //Tipo señala si es registra o actualizar
    String tipo;

    //Objeto que contiene los datos de editorial seleccionado
    Autor obj = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_autor_crud_formulario);
        txtTitulo = findViewById(R.id.idCrudAutorFrmTitulo);

        txtNom = findViewById(R.id.idCrudAutorFrmNombre);
        txtApe = findViewById(R.id.idCrudAutorFrmApellido);
        txtFec = findViewById(R.id.idCrudAutorFrmFechaNacimiento);
        txtTel = findViewById(R.id.idCrudAutorFrmTelefono);
        btnEnviar = findViewById(R.id.idCrudAutorFrmBtnEnviar);
        btnRegresar = findViewById(R.id.idCrudAutorFrmBtnRegresar);


        adaptador = new ArrayAdapter<String>(this, androidx.appcompat.R.layout.support_simple_spinner_dropdown_item, grados);
        spnGrado = findViewById(R.id.idCrudAutorFrmGrado);
        spnGrado.setAdapter(adaptador);


        serviceAutor = ConnectionRest.getConnection().create(ServiceAutor.class);
        serviceGrado = ConnectionRest.getConnection().create(ServiceGrado.class);

        cargaGrado();

        Bundle extras = getIntent().getExtras();
        tipo = extras.getString("var_tipo");


        if (tipo.equals("REGISTRAR")){
            txtTitulo.setText("Mantenimiento Autor - REGISTRA");
            btnEnviar.setText("REGISTRA");

        }else if (tipo.equals("ACTUALIZAR")){
            txtTitulo.setText("Mantenimiento Autor - ACTUALIZA");
            btnEnviar.setText("ACTUALIZA");

            obj = (Autor) extras.get("var_item");

            txtNom.setText(obj.getNombres());
            txtApe.setText(obj.getApellidos());
            txtFec.setText(obj.getFechaNacimiento());
            txtTel.setText(obj.getTelefono());

        }

        btnEnviar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String nom = txtNom.getText().toString();
                String ape = txtApe.getText().toString();
                String fec = txtFec.getText().toString();
                String tel = txtTel.getText().toString();

                if (!nom.matches(ValidacionUtil.NOMBRE)){
                    mensajeAlert("El nombre es de 2 a mas caracteres ");
                }else if (!ape.matches(ValidacionUtil.TEXTO)){
                    mensajeAlert("El apellido es de 2 a mas caracteres");
                }else if (!fec.matches(ValidacionUtil.FECHA)){
                    mensajeAlert("La fecha es de formato YYYY-MM-dd");
                }else if (!tel.matches(ValidacionUtil.TELEFONO)){
                    mensajeAlert("La telefono es de 9 digitos");
                }else{
                    String grado = spnGrado.getSelectedItem().toString();
                    String idGrado = grado.split(":")[0];

                    Grado objGrado = new Grado();
                    objGrado.setIdGrado(Integer.parseInt(idGrado));

                    Autor objAutor = new Autor();
                    objAutor.setNombres(nom);
                    objAutor.setApellidos(ape);
                    objAutor.setFechaNacimiento(fec);
                    objAutor.setTelefono(tel);
                    objAutor.setFechaRegistro(FunctionUtil.getFechaActualStringDateTime());
                    objAutor.setEstado(1);
                    objAutor.setGrado(objGrado);

                    if("REGISTRAR".equals(tipo)){
                        registraAutor(objAutor);
                    }else if("ACTUALIZAR".equals(tipo)) {
                        Autor obj = (Autor) extras.get("var_item");
                        objAutor.setIdAutor(obj.getIdAutor());
                        actualizaAutor(objAutor);
                    }
                }

            }
        });

        btnRegresar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AutorCrudFormularioActivity.this, AutorCrudListaActivity.class);
                startActivity(intent);
            }
        });
    }

    public void registraAutor(Autor obj){
        Call<Autor> call = serviceAutor.insertarAutor(obj);
        call.enqueue(new Callback<Autor>() {
            @Override
            public void onResponse(Call<Autor> call, Response<Autor> response) {
                if (response.isSuccessful()){
                    Autor objSalida =   response.body();
                    mensajeAlert("Se registro el Autor" +
                            "\nID >> " + objSalida.getIdAutor() +
                            "\nNombre >> " + objSalida.getNombres() );
                }
            }
            @Override
            public void onFailure(Call<Autor> call, Throwable t) {
                mensajeAlert("Error al acceder al Servicio Rest >>> " + t.getMessage());
            }
        });
    }


    public void actualizaAutor(Autor obj){
        Call<Autor> call = serviceAutor.actualizaAutor(obj);
        call.enqueue(new Callback<Autor>() {
            @Override
            public void onResponse(Call<Autor> call, Response<Autor> response) {
                if (response.isSuccessful()){
                    Autor objSalida =   response.body();
                    mensajeAlert("Se actualiza el Autor " +
                            "\nID >> " + objSalida.getIdAutor() +
                            "\nNombre >> " + objSalida.getNombres() );
                }
            }
            @Override
            public void onFailure(Call<Autor> call, Throwable t) {
                mensajeAlert("Error al acceder al Servicio Rest >>> " + t.getMessage());
            }
        });
    }

    public void cargaGrado(){
        Call<List<Grado>> call = serviceGrado.Todos();
        call.enqueue(new Callback<List<Grado>>() {
            @Override
            public void onResponse(Call<List<Grado>> call, Response<List<Grado>> response) {
                if (response.isSuccessful()){
                    List<Grado> lstGrados =  response.body();
                    for(Grado obj: lstGrados){
                        grados.add(obj.getIdGrado() +":"+ obj.getDescripcion());
                    }
                    adaptador.notifyDataSetChanged();

                    if (tipo.equals("ACTUALIZAR")){

                        int idGrado = obj.getGrado().getIdGrado();
                        String nombreGrado = obj.getGrado().getDescripcion();

                        String itemGrado = idGrado+":"+nombreGrado;
                        int posSeleccionada = -1;
                        for(int i=0; i< grados.size(); i++){
                            if (grados.get(i).equals(itemGrado)){
                                posSeleccionada = i;
                                break;
                            }
                        }
                        spnGrado.setSelection(posSeleccionada);
                    }

                }else{
                    mensajeToastLong("Error al acceder al Servicio Rest >>> ");
                }
            }

            @Override
            public void onFailure(Call<List<Grado>> call, Throwable t) {
                mensajeToastLong("Error al acceder al Servicio Rest >>> " + t.getMessage());
            }
        });
    }


}


