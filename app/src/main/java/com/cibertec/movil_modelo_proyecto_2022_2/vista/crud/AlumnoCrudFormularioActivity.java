package com.cibertec.movil_modelo_proyecto_2022_2.vista.crud;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import com.cibertec.movil_modelo_proyecto_2022_2.R;
import com.cibertec.movil_modelo_proyecto_2022_2.adapter.AlumnoAdapter;
import com.cibertec.movil_modelo_proyecto_2022_2.entity.Alumno;
import com.cibertec.movil_modelo_proyecto_2022_2.entity.Pais;
import com.cibertec.movil_modelo_proyecto_2022_2.service.ServiceAlumno;
import com.cibertec.movil_modelo_proyecto_2022_2.service.ServicePais;
import com.cibertec.movil_modelo_proyecto_2022_2.util.ConnectionRest;
import com.cibertec.movil_modelo_proyecto_2022_2.util.FunctionUtil;
import com.cibertec.movil_modelo_proyecto_2022_2.util.NewAppCompatActivity;
import com.cibertec.movil_modelo_proyecto_2022_2.util.ValidacionUtil;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import java.text.SimpleDateFormat;

import android.widget.DatePicker;
import java.util.Locale;

public class AlumnoCrudFormularioActivity extends NewAppCompatActivity {
    TextView txtTitulo;
    Button btnEnviar, btnRegresar;


    Spinner spnPais;
    ArrayAdapter<String> adaptador;
    ArrayList<String> paises = new ArrayList<String>();


    //Acceso al servicio rest
    ServicePais servicePais;
    ServiceAlumno serviceAlumno;

    //ComponenteS
    EditText txtAlumnoNombres, txtAlumnoApellidos, txtAlumnoTelefono, txtAlumnoDni,
            txtAlumnoCorreo, txtAlumnoFecNac;
    //Button btnRegistrar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alumno_crud_formulario);

        txtAlumnoNombres = findViewById(R.id.idCrudAlumnoFrmNombre);
        txtAlumnoApellidos = findViewById(R.id.idCrudAlumnoFrmApellido);
        txtAlumnoTelefono = findViewById(R.id.idCrudAlumnoFrmTelefono);
        txtAlumnoDni = findViewById(R.id.idCrudAlumnoFrmDni);
        txtAlumnoCorreo = findViewById(R.id.idCrudAlumnoFrmCorreo);
        txtAlumnoFecNac = findViewById(R.id.idCrudAlumnoFrmFecNac);

        //Acceso al servicio rest
        serviceAlumno = ConnectionRest.getConnection().create(ServiceAlumno.class);
        servicePais = ConnectionRest.getConnection().create(ServicePais.class);
        //Para el adaptador
        adaptador = new ArrayAdapter<String>(this, androidx.appcompat.R.layout.support_simple_spinner_dropdown_item, paises);
        spnPais = findViewById(R.id.idCrudAlumnoFrmPais);
        spnPais.setAdapter(adaptador);

        cargaPaises();

        txtAlumnoFecNac.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar myCalendar = Calendar.getInstance();
                new DatePickerDialog
                        (AlumnoCrudFormularioActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", new Locale("es"));
                        myCalendar.set(Calendar.YEAR, year);
                        myCalendar.set(Calendar.MONTH, month);
                        myCalendar.set(Calendar.DAY_OF_MONTH, day);
                        txtAlumnoFecNac.setText(dateFormat.format(myCalendar.getTime()));
                    }
                }, myCalendar.get(Calendar.YEAR), myCalendar.get(Calendar.MONTH), myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });


        txtTitulo = findViewById(R.id.idCrudAlumnoFrmTitulo);
        btnEnviar = findViewById(R.id.idCrudAlumnoFrmBtnRegistrar);
        btnRegresar = findViewById(R.id.idCrudAlumnoFrmBtnRegresar);

        Bundle extras = getIntent().getExtras();
        String tipo = extras.getString("var_tipo");
        if (tipo.equals("REGISTRAR")) {
            txtTitulo.setText("Mantenimiento Alumno - REGISTRAR");
            btnEnviar.setText("REGISTRAR");
        } else if (tipo.equals("ACTUALIZAR")) {
            txtTitulo.setText("Mantenimiento Alumno - ACTUALIZAR");
            btnEnviar.setText("ACTUALIZAR");

            Alumno obj = (Alumno) extras.get("var_item");
            txtAlumnoNombres.setText(obj.getNombres());
            txtAlumnoApellidos.setText(obj.getApellidos());
            txtAlumnoTelefono.setText(obj.getTelefono());
            txtAlumnoDni.setText(obj.getDni());
            txtAlumnoCorreo.setText(obj.getCorreo());
            txtAlumnoFecNac.setText(obj.getFechaNacimiento());


        }
        btnRegresar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AlumnoCrudFormularioActivity.this, AlumnoCrudListaActivity.class);
                startActivity(intent);
            }
        });


        btnEnviar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String nom = txtAlumnoNombres.getText().toString();
                String ape = txtAlumnoApellidos.getText().toString();
                String tel = txtAlumnoTelefono.getText().toString();
                String dni = txtAlumnoDni.getText().toString();
                String cor = txtAlumnoCorreo.getText().toString();
                String fec = txtAlumnoFecNac.getText().toString();

                if (!nom.matches(ValidacionUtil.NOMBRE)) {
                    mensajeToastShort("El nombre debe tener de 2 a 20 caracteres");
                } else if (!ape.matches(ValidacionUtil.TEXTO)) {
                    mensajeToastShort("El apellido debe tener de 2 a 20 caracteres");
                } else if (!tel.matches(ValidacionUtil.TELEFONO)) {
                    mensajeToastShort("El teléfono debe tener 9 caracteres numéricos");
                } else if (!dni.matches(ValidacionUtil.DNI)) {
                    mensajeToastShort("El dni debe tener 8 carácteres numéricos");
                } else if (!cor.matches(ValidacionUtil.CORREO)) {
                    mensajeToastShort("El correo tener el siguiente formato: abc@abc.abc");
                } else if (!fec.matches(ValidacionUtil.FECHA)) {
                    mensajeToastShort("La fecha debe tener el siguiente formato: año-mes-día");
                } else {
                    try {
                        String pais = spnPais.getSelectedItem().toString();
                        String idPais = pais.split(":")[0];

                        Pais objPais = new Pais();
                        objPais.setIdPais(Integer.parseInt(idPais));

                        Alumno alumno = new Alumno();
                        alumno.setNombres(nom);
                        alumno.setApellidos(ape);
                        alumno.setTelefono(tel);
                        alumno.setDni(dni);
                        alumno.setCorreo(cor);
                        alumno.setFechaNacimiento(fec);
                        alumno.setFechaRegistro(FunctionUtil.getFechaActualStringDateTime());
                        alumno.setEstado(1);
                        alumno.setPais(objPais);

                        Bundle extras = getIntent().getExtras();
                        String tipo = extras.getString("var_tipo");
                        if (tipo.equals("REGISTRAR")) {
                            registra(alumno);

                        } else if (tipo.equals("ACTUALIZAR")) {
                            Alumno obj = (Alumno) extras.get("var_item");
                            alumno.setIdAlumno(obj.getIdAlumno());
                            actualiza(alumno);

                        }


                    } catch (Exception e) {
                        mensajeToastShort("Hay problemas al registrar el Alumno  " + e);
                    }
                }
            }
        });

    }


    public void cargaPaises() {
        Call<List<Pais>> call = servicePais.listaTodos();
        call.enqueue(new Callback<List<Pais>>() {
            @Override
            public void onResponse(Call<List<Pais>> call, Response<List<Pais>> response) {
                if (response.isSuccessful()) {
                    List<Pais> lstPaises = response.body();
                    for (Pais obj : lstPaises) {

                        paises.add(obj.getIdPais() + ":  " + obj.getIso() + "-->  " + obj.getNombre());
                        //paises.add(objPaises.getNombre());
                    }


                    Bundle extras = getIntent().getExtras();
                    String tipo = extras.getString("var_tipo");

                    if (tipo.equals("ACTUALIZAR")) {
                        Alumno obj = (Alumno) extras.get("var_item");
                        int posicion = -1;
                        String item = obj.getPais().getIdPais() + ":  " + obj.getPais().getIso() + "-->  " + obj.getPais().getNombre();
                        for (int i = 0; i < paises.size(); i++) {
                            if (paises.get(i).equals(item)) {
                                posicion = i;
                                break;
                            }
                        }
                        if (posicion != -1) {
                            spnPais.setSelection(posicion);
                        }
                    }

                    adaptador.notifyDataSetChanged();
                } else {
                    mensajeToastShort("Error al acceder al servicio Rest");
                }
            }

            @Override
            public void onFailure(Call<List<Pais>> call, Throwable t) {
                mensajeToastLong("Error");

            }
        });
    }


    //Metodo para registrar.
    public void registra(Alumno obj) {
        Call<Alumno> call = serviceAlumno.insertaAlumno(obj);
        call.enqueue(new Callback<Alumno>() {
            @Override
            public void onResponse(Call<Alumno> call, Response<Alumno> response) {
                if (response.isSuccessful()) {
                    Alumno objSalida = response.body();
                    if (objSalida != null) {
                        mensajeAlert("Se registró el nuevo Alumno" +
                                "\nId >> " + objSalida.getIdAlumno() +
                                "\nNombres >> " + objSalida.getNombres() +
                                "\nApellidos >> " + objSalida.getApellidos() +
                                "\nTelefono >> " + objSalida.getTelefono() +
                                "\nDNI >> " + objSalida.getDni() +
                                "\nCorreo >> " + objSalida.getCorreo() +
                                "\nFecha de nacimiento >> " + objSalida.getFechaNacimiento() +
                                "\nFecha de registro >> " + objSalida.getFechaRegistro() +
                                "\nEstado  >> " + objSalida.getEstado() +
                                "\nPaís >> " + objSalida.getPais()

                        );

                    } else {
                        mensajeAlert(response.toString());
                    }
                }
            }

            @Override
            public void onFailure(Call<Alumno> call, Throwable t) {
                mensajeToastShort("Error al acceder al servicio Rest" + t.getMessage());

            }
        });
    }

    //Metodo para actualizar.
    public void actualiza(Alumno obj) {
        Call<Alumno> call = serviceAlumno.actualizarAlumno(obj);
        call.enqueue(new Callback<Alumno>() {
            @Override
            public void onResponse(Call<Alumno> call, Response<Alumno> response) {
                if (response.isSuccessful()) {
                    Alumno objSalida = response.body();
                    if (objSalida != null) {
                        mensajeAlert("Se actualizó datos del Alumno" +
                                "\nId >> " + objSalida.getIdAlumno() +
                                "\nNombres >> " + objSalida.getNombres() +
                                "\nApellidos >> " + objSalida.getApellidos() +
                                "\nTelefono >> " + objSalida.getTelefono() +
                                "\nDNI >> " + objSalida.getDni() +
                                "\nCorreo >> " + objSalida.getCorreo() +
                                "\nFecha de nacimiento >> " + objSalida.getFechaNacimiento() +
                                "\nFecha de registro >> " + objSalida.getFechaRegistro() +
                                "\nEstado  >> " + objSalida.getEstado() +
                                "\nPaís >> " + objSalida.getPais()

                        );

                    } else {
                        mensajeAlert(response.toString());
                    }
                }
            }

            @Override
            public void onFailure(Call<Alumno> call, Throwable t) {
                mensajeToastShort("Error al acceder al servicio Rest" + t.getMessage());

            }
        });
    }


}