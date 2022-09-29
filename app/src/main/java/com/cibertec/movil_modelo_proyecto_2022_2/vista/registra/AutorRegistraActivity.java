package com.cibertec.movil_modelo_proyecto_2022_2.vista.registra;

import android.os.Bundle;
import android.os.Build;
import android.os.StrictMode;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;
import android.widget.Button;

import com.cibertec.movil_modelo_proyecto_2022_2.R;
import com.cibertec.movil_modelo_proyecto_2022_2.util.NewAppCompatActivity;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.cibertec.movil_modelo_proyecto_2022_2.entity.Grado;
import com.cibertec.movil_modelo_proyecto_2022_2.entity.Autor;
import com.cibertec.movil_modelo_proyecto_2022_2.service.ServiceGrado;
import com.cibertec.movil_modelo_proyecto_2022_2.service.ServiceAutor;
import com.cibertec.movil_modelo_proyecto_2022_2.util.ConnectionRest;
import com.cibertec.movil_modelo_proyecto_2022_2.util.FunctionUtil;
import com.cibertec.movil_modelo_proyecto_2022_2.util.NewAppCompatActivity;
import com.cibertec.movil_modelo_proyecto_2022_2.util.ValidacionUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class AutorRegistraActivity extends NewAppCompatActivity {

    Spinner spnGra;
    ArrayAdapter<String> adaptador;
    ArrayList<String> grados = new ArrayList<String>();

    ServiceAutor serviceAutor;
    ServiceGrado serviceGrado;

    EditText txtnom, txtape, txtfec, txttel;
    Button btnRegistrar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_autor_registra);

        serviceGrado = ConnectionRest.getConnection().create(ServiceGrado.class);
        serviceAutor = ConnectionRest.getConnection().create(ServiceAutor.class);

        adaptador = new ArrayAdapter<String>(this, androidx.appcompat.R.layout.support_simple_spinner_dropdown_item, grados);
        spnGra = findViewById(R.id.spnGrado);
        spnGra.setAdapter(adaptador);

        cargaGrado();

        txtnom = findViewById(R.id.txtNombres);
        txtape = findViewById(R.id.txtApellidos);
        txtfec = findViewById(R.id.txtfechaNacimiento);
        txttel = findViewById(R.id.txtTelefono);
        btnRegistrar = findViewById(R.id.btnRegistrarAutor);
        btnRegistrar.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                String nom = txtnom.getText().toString();
                String ape = txtape.getText().toString();
                String fec = txtfec.getText().toString();
                String tel = txttel.getText().toString();


                if (!nom.matches(ValidacionUtil.NOMBRE)){
                    mensajeToast("El nombre es de de 2 a 20 caracteres");
                }else if (!ape.matches(ValidacionUtil.TEXTO)){
                    mensajeToast("El apellido es de 2 a 20 caracteres");
                }else if (!fec.matches(ValidacionUtil.FECHA)){
                    mensajeToast("La fecha es de formato YYYY-MM-dd");
                }else if (!tel.matches(ValidacionUtil.TELEFONO)){
                    mensajeToast("El telefono es de 9 digitos");
                }else{
                    String grado = spnGra.getSelectedItem().toString();
                    String idGrado = grado.split(":")[0];

                    Grado objGrado = new Grado();
                    objGrado.setIdGrado(Integer.parseInt(idGrado));


                    Autor Autor = new Autor();
                    Autor.setNombres(nom);
                    Autor.setApellidos(ape);
                    Autor.setFechaNacimiento(fec);
                    Autor.setTelefono(tel);
                    Autor.setFechaRegistro(FunctionUtil.getFechaActualStringDateTime());
                    Autor.setEstado(1);
                    Autor.setGrado(objGrado);

                    registra(Autor);


                }
            }
        });

    }
    public void registra(Autor autor){
        Call<Autor> call = serviceAutor.insertarAutor(autor);
        call.enqueue(new Callback<Autor>() {
            @Override
            public void onResponse(Call<Autor> call, Response<Autor> response) {
                if(response.isSuccessful()){
                    Autor objSalida = response.body();
                    if (objSalida != null) {
                        mensajeAlert( "Se registro el Autor" +
                                "\nid >> " + objSalida.getIdAutor() +
                                "\nnombres >> " + objSalida.getNombres() +
                                "\napellidos >> " + objSalida.getApellidos() +
                                "\nfecha nacimiento >> " + objSalida.getFechaNacimiento() +
                                "\ntelefono >> " + objSalida.getTelefono() +
                                "\nfecha registro >> " + objSalida.getFechaRegistro() +
                                "\nestado >> " + objSalida.getEstado() +
                                "\ngrado >> " + objSalida.getGrado());
                }
                    else{
                        mensajeAlert(response.toString());
                    }

                }
            }

            @Override
            public void onFailure(Call<Autor> call, Throwable t) {
                mensajeToast("Error al acceder al Servicio Rest >>> " + t.getMessage());
            }
        });

    }

    public void cargaGrado(){
        Call<List<Grado>> call = serviceGrado.Todos();
        call.enqueue(new Callback<List<Grado>>() {
            @Override
            public void onResponse(Call<List<Grado>> call, Response<List<Grado>> response) {
                if(response.isSuccessful()){
                    List<Grado> lstGrads = response.body();
                    for(Grado obj: lstGrads){
                        grados.add(obj.getIdGrado() +":"+ obj.getDescripcion());
                    }
                    adaptador.notifyDataSetChanged();
                }else{
                    mensajeToast("Error al acceder al Servicio Rest >>> " );
                }
            }

            @Override
            public void onFailure(Call<List<Grado>> call, Throwable t) {
                mensajeToast("Error al acceder al Servicio Rest >>> " + t.getMessage());

            }
        });

    }

    public void mensajeToast(String mensaje) {
        Toast toast1 = Toast.makeText(getApplicationContext(), mensaje, Toast.LENGTH_LONG);
        toast1.show();
    }

    public void mensajeAlert(String msg) {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
        alertDialog.setMessage(msg);
        alertDialog.setCancelable(true);
        alertDialog.show();
    }



}