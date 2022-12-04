package com.cibertec.movil_modelo_proyecto_2022_2.vista.registra;


import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import android.os.Build;
import android.os.StrictMode;

import com.cibertec.movil_modelo_proyecto_2022_2.R;
import com.cibertec.movil_modelo_proyecto_2022_2.util.NewAppCompatActivity;



import com.cibertec.movil_modelo_proyecto_2022_2.R;
import com.cibertec.movil_modelo_proyecto_2022_2.entity.Alumno;
import com.cibertec.movil_modelo_proyecto_2022_2.entity.Pais;
import com.cibertec.movil_modelo_proyecto_2022_2.service.ServiceAlumno;
import com.cibertec.movil_modelo_proyecto_2022_2.service.ServicePais;
import com.cibertec.movil_modelo_proyecto_2022_2.util.ConnectionRest;
import com.cibertec.movil_modelo_proyecto_2022_2.util.FunctionUtil;
import com.cibertec.movil_modelo_proyecto_2022_2.util.NewAppCompatActivity;
import com.cibertec.movil_modelo_proyecto_2022_2.util.ValidacionUtil;
import com.cibertec.movil_modelo_proyecto_2022_2.vista.crud.AlumnoCrudFormularioActivity;

import java.sql.Connection;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;



public class AlumnoRegistraActivity extends NewAppCompatActivity {

     Spinner spnRegPais;
     ArrayAdapter<String> adaptador;
     ArrayList<String> paises = new ArrayList<String>();


    //Acceso al servicio rest
     ServicePais servicePais;
     ServiceAlumno serviceAlumno;

    //ComponenteS
    EditText txtRegAlumnoNombres,txtRegAlumnoApellidos,txtRegAlumnoTelefono,txtRegAlumnoDni,
    txtRegAlumnoCorreo,txtRegAlumnoFecNac;
    Button btnRegistrar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alumno_registra);

        txtRegAlumnoNombres = findViewById(R.id.txtRegAlumnoNombres);
        txtRegAlumnoApellidos = findViewById(R.id.txtRegAlumnoApellidos);
        txtRegAlumnoTelefono = findViewById(R.id.txtRegAlumnoTelefono);
        txtRegAlumnoDni = findViewById(R.id.txtRegAlumnoDni);
        txtRegAlumnoCorreo = findViewById(R.id.txtRegAlumnoCorreo);
        txtRegAlumnoFecNac =  findViewById(R.id.txtRegAlumnoFecNac);


        //Acceso al servicio rest
        serviceAlumno = ConnectionRest.getConnection().create(ServiceAlumno.class);
        servicePais= ConnectionRest.getConnection().create(ServicePais.class);

        //Para el adaptador
        adaptador = new ArrayAdapter<String>(this, androidx.appcompat.R.layout.support_simple_spinner_dropdown_item, paises);
        spnRegPais = findViewById(R.id.spnRegPais);
        spnRegPais.setAdapter(adaptador);

        //Llena paises en el spiner
        cargaPaises();

        txtRegAlumnoFecNac.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar myCalendar = Calendar.getInstance();
                new DatePickerDialog
                        (AlumnoRegistraActivity.this, new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", new Locale("es"));
                                myCalendar.set(Calendar.YEAR, year);
                                myCalendar.set(Calendar.MONTH, month);
                                myCalendar.set(Calendar.DAY_OF_MONTH, day);
                                txtRegAlumnoFecNac.setText(dateFormat.format(myCalendar.getTime()));
                            }
                        }, myCalendar.get(Calendar.YEAR), myCalendar.get(Calendar.MONTH), myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        //Boton REGISTRAR
        btnRegistrar = findViewById(R.id.btnRegistrar);


        btnRegistrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String nom = txtRegAlumnoNombres.getText().toString();
                String ape = txtRegAlumnoApellidos.getText().toString();
                String tel = txtRegAlumnoTelefono.getText().toString();
                String dni = txtRegAlumnoDni.getText().toString();
                String cor = txtRegAlumnoCorreo.getText().toString();
                String fec = txtRegAlumnoFecNac.getText().toString();

               if (!nom.matches(ValidacionUtil.NOMBRE)){
                    mensajeToast("El nombre debe tener de 2 a 20 caracteres");
                }else if (!ape.matches(ValidacionUtil.TEXTO)){
                    mensajeToast("El apellido debe tener de 2 a 20 caracteres");
                }else if (!tel.matches(ValidacionUtil.TELEFONO)){
                    mensajeToast("El teléfono debe tener 9 caracteres numéricos");
                }else if (!dni.matches(ValidacionUtil.DNI)){
                    mensajeToast("El dni debe tener 8 caracteres numéricos");
                }else if (!cor.matches(ValidacionUtil.CORREO)){
                    mensajeToast("El correo tener el siguiente formato: abc@abc.abc");
                }else if (!fec.matches(ValidacionUtil.FECHA)){

                    mensajeToast("La fecha debe tener el siguiente formato: año-mes-día");
                }else{
                   try {
                       String pais = spnRegPais.getSelectedItem().toString();
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
                       registra(alumno);


                   }catch (Exception e ){
                        mensajeToast( "Hay problemas al registrar el Alumno  " + e);
                   }



                }


            }
        });



    }
//Metodo para registrar.
    public void registra (Alumno obj)
    {
        Call<Alumno> call = serviceAlumno.insertaAlumno(obj);
        call.enqueue(new Callback<Alumno>() {
            @Override
            public void onResponse(Call<Alumno> call, Response<Alumno> response) {
                if(response.isSuccessful()){
                    Alumno objSalida = response.body();
                    if(objSalida != null){
                    mensajeAlert("Se registró exitosamente el nuevo Alumno" +
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


                )
                    ;
                    limpia();


                    } else{
                        mensajeAlert(response.toString());
                    }
                }
            }

            @Override
            public void onFailure(Call<Alumno> call, Throwable t) {
                mensajeToast("Error al acceder al servicio Rest" + t.getMessage());

            }
        });
    }


public void cargaPaises(){
    Call<List<Pais>> call = servicePais.listaTodos();
    call.enqueue(new Callback<List<Pais>>() {
        @Override
        public void onResponse(Call<List<Pais>> call, Response<List<Pais>> response) {
            if (response.isSuccessful()){
                List<Pais> lstPaises = response.body();
                for (Pais objPaises:lstPaises){

                  paises.add(objPaises.getIdPais() + ": " + objPaises.getIso() + "-->  " + objPaises.getNombre());
                  //paises.add(objPaises.getNombre());
                }
                adaptador.notifyDataSetChanged();
            }else{
             mensajeToast("Error al acceder al servicio Rest");
            }
        }
        @Override
        public void onFailure(Call<List<Pais>> call, Throwable t) {
            mensajeToast("Error al acceder al Servicio Rest >>> " + t.getMessage());
        }

    });
}
public void limpia(){
   txtRegAlumnoNombres.setText("");
   txtRegAlumnoApellidos.setText("");
   txtRegAlumnoDni.setText("");
   txtRegAlumnoCorreo.setText("");
   txtRegAlumnoTelefono.setText("");
   txtRegAlumnoFecNac.setText("");

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