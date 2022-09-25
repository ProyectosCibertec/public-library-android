package com.cibertec.movil_modelo_proyecto_2022_2.vista.registra;

import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;

import com.cibertec.movil_modelo_proyecto_2022_2.R;
import com.cibertec.movil_modelo_proyecto_2022_2.entity.Modalidad;
import com.cibertec.movil_modelo_proyecto_2022_2.entity.Revista;
import com.cibertec.movil_modelo_proyecto_2022_2.service.ServiceModalidad;
import com.cibertec.movil_modelo_proyecto_2022_2.service.ServiceRevista;
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

public class RevistaRegistraActivity extends NewAppCompatActivity {
    private static final Logger LOGGER = Logger.getLogger(RevistaRegistraActivity.class.getName());

    ServiceModalidad serviceModalidad;
    ServiceRevista serviceRevista;

    Spinner spnModalidad;
    ArrayAdapter<String> adapter;
    List<String> modalidadList = new ArrayList<>();

    EditText editNombre;
    EditText editFrecuencia;
    EditText editFechaCreacion;

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

        serviceModalidad = ConnectionRest.getConnection().create(ServiceModalidad.class);
        serviceRevista = ConnectionRest.getConnection().create(ServiceRevista.class);

        editNombre = findViewById(R.id.editNombre);
        editFrecuencia = findViewById(R.id.editFrecuencia);
        editFechaCreacion = findViewById(R.id.editFechaCreacion);
        spnModalidad = findViewById(R.id.spnModalidad);

        adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, modalidadList);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spnModalidad.setAdapter(adapter);

        fillModalidadList();
    }

    private void fillModalidadList() {
        Call<List<Modalidad>> call = serviceModalidad.listaTodos();

        call.enqueue(new Callback<List<Modalidad>>() {
            @Override
            public void onResponse(Call<List<Modalidad>> call, Response<List<Modalidad>> response) {
                if (!response.isSuccessful()) {
                    LOGGER.log(Level.SEVERE, "Response not successful, status code: {0}", response.code());
                }

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    Objects.requireNonNull(response.body())
                            .forEach(modality -> modalidadList.add(modality.getIdModalidad() + ": " + modality.getDescripcion()));
                    adapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onFailure(Call<List<Modalidad>> call, Throwable t) {
                LOGGER.log(Level.SEVERE, "There was an error creating the request {0}", t.getMessage());
            }
        });
    }

    public void registerMagazine(View view) {
        String nombre = editNombre.getText().toString();
        String frecuencia = editFrecuencia.getText().toString();
        String fechaCreacion = editFechaCreacion.getText().toString();

        if (!nombre.matches(ValidacionUtil.NOMBRE)) {
            mensajeToast("El nombre debe tener entre 3 a 30 caracteres");
        } else if (!frecuencia.matches(ValidacionUtil.TEXTO)) {
            mensajeToast("El texto debe tener entre 2 a 20 caracteres");
        } else if (!fechaCreacion.matches(ValidacionUtil.FECHA)) {
            mensajeToast("La fecha debe estar en el formato YYYY-MM-dd");
        } else {
            String modalidad = spnModalidad.getSelectedItem().toString();
            String idModalidad = modalidad.split(":")[0];

            Modalidad objModalidad = new Modalidad();
            objModalidad.setIdModalidad(Integer.parseInt(idModalidad));

            Revista revista = new Revista();
            revista.setNombre(nombre);
            revista.setFrecuencia(frecuencia);
            revista.setFechaCreacion(fechaCreacion);
            revista.setModalidad(objModalidad);
            revista.setFechaRegistro(FunctionUtil.getFechaActualStringDateTime());
            revista.setEstado(1);

            Call<Revista> call = serviceRevista.createMagazine(revista);

            call.enqueue(new Callback<Revista>() {
                @Override
                public void onResponse(Call<Revista> call, Response<Revista> response) {
                    if (!response.isSuccessful()) {
                        LOGGER.log(Level.SEVERE, "Response not successful, status code: {0}", response.code());
                    }

                    Revista objSalida = response.body();

                    if (objSalida != null) {
                        mensajeAlert("Se registró la Revista" +
                                "\nid: " + objSalida.getIdRevista() +
                                "\nnombre: " + objSalida.getNombre() +
                                "\nfrecuencia: " + objSalida.getFrecuencia() +
                                "\nfecha creación: " + objSalida.getFechaCreacion() +
                                "\nfecha registro: " + objSalida.getFechaRegistro() +
                                "\nestado: " + objSalida.getEstado()
                        );
                    }
                }

                @Override
                public void onFailure(Call<Revista> call, Throwable t) {
                    LOGGER.log(Level.SEVERE, "There was an error creating the request {0}", t.getMessage());
                }
            });
        }
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