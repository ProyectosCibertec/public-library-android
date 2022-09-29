package com.cibertec.movil_modelo_proyecto_2022_2.service;
import com.cibertec.movil_modelo_proyecto_2022_2.entity.Autor;

import retrofit2.http.POST;
import retrofit2.Call;
import retrofit2.http.Body;

public interface ServiceAutor {
    @POST("autor")
    public Call<Autor> insertarAutor(@Body Autor autor);
}
