package com.cibertec.movil_modelo_proyecto_2022_2.vista.crud;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;

import com.cibertec.movil_modelo_proyecto_2022_2.R;
import com.cibertec.movil_modelo_proyecto_2022_2.entity.Revista;
import com.cibertec.movil_modelo_proyecto_2022_2.service.ServiceRevista;
import com.cibertec.movil_modelo_proyecto_2022_2.util.ConnectionRest;
import com.cibertec.movil_modelo_proyecto_2022_2.util.NewAppCompatActivity;

import java.util.logging.Level;
import java.util.logging.Logger;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RevistaCrudEliminaActivity extends NewAppCompatActivity {

    private static final Logger LOGGER = Logger.getLogger(RevistaCrudEliminaActivity.class.getName());
    TextView editNombre;
    TextView editFrecuencia;
    TextView editFechaCreacion;
    TextView editModalidad;
    Button btnEliminar;
    int idRevista;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_revista_crud_elimina);

        idRevista = getIntent().getIntExtra("ID_REVISTA", 0);
        editNombre = findViewById(R.id.txtNombre);
        editFrecuencia = findViewById(R.id.txtFrecuencia);
        editFechaCreacion = findViewById(R.id.txtFechaCreacion);
        editModalidad = findViewById(R.id.txtModalidad);
        btnEliminar = findViewById(R.id.btnEliminar);

        btnEliminar.setOnClickListener(view -> {
            deleteMagazine();
        });

        getRevistaPorId(idRevista);
    }

    private void getRevistaPorId(int idRevista) {
        ServiceRevista service = ConnectionRest.getConnection().create(ServiceRevista.class);
        Call<Revista> call = service.getMagazineById(idRevista);
        Callback<Revista> revistaCallback = new Callback<Revista>() {
            @Override
            public void onResponse(Call<Revista> call, Response<Revista> response) {
                if (response.isSuccessful()) {
                    try {
                        if (response.body() != null) {
                            Revista revista = response.body();
                            editNombre.setText(revista.getNombre());
                            editFechaCreacion.setText(revista.getFechaCreacion());
                            editFrecuencia.setText(revista.getFrecuencia());
                            editModalidad.setText(revista.getModalidad().getDescripcion());
                        }
                    } catch (Exception e) {
                        LOGGER.log(Level.WARNING, "Error connecting to Revista service");
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<Revista> call, Throwable t) {

            }
        };
        call.enqueue(revistaCallback);
    }

    public void deleteMagazine() {
        ServiceRevista service = ConnectionRest.getConnection().create(ServiceRevista.class);
        Call<Revista> call = service.deleteMagazine(idRevista);
        call.enqueue(new Callback<Revista>() {
            @Override
            public void onResponse(Call<Revista> call, Response<Revista> response) {
                if (response.isSuccessful()) {
                    Revista objSalida = response.body();

                    if (response.body() != null) {
                        mensajeAlert("Se eliminó la Revista" +
                                "\nid: " + objSalida.getIdRevista()
                        );
                    }
                }
            }

            @Override
            public void onFailure(Call<Revista> call, Throwable t) {

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