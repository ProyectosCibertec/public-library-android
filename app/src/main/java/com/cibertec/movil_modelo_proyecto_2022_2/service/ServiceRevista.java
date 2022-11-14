package com.cibertec.movil_modelo_proyecto_2022_2.service;

import com.cibertec.movil_modelo_proyecto_2022_2.entity.Revista;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface ServiceRevista {

    @POST("revista")
    Call<Revista> createMagazine(@Body Revista revista);

    @GET("revista")
    Call<List<Revista>> getMagazines();

    @GET("revista/porNombre/{nombre}")
    Call<List<Revista>> searchMagazines(@Path("nombre") String nombre);

    @DELETE("revista/{id}")
    Call<Revista> deleteMagazine(@Path("id") int id);

}
