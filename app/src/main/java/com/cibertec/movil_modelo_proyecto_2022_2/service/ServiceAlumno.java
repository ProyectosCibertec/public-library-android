package com.cibertec.movil_modelo_proyecto_2022_2.service;

import com.cibertec.movil_modelo_proyecto_2022_2.entity.Alumno;
import com.cibertec.movil_modelo_proyecto_2022_2.entity.Pais;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface ServiceAlumno {
    @POST("alumno")
public Call<Alumno>insertaAlumno(@Body Alumno obj);

    @GET("alumno")
    public Call<List<Alumno>> listaAlumno();

    @GET("alumno/porNombre/{nombre}")
    public Call<List<Alumno>> listaAlumnoPorNombre(@Path("nombre") String nombre);

    @PUT("alumno")
    public Call<Alumno> actualizarAlumno(@Body Alumno obj);

}
