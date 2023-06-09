package com.cibertec.movil_modelo_proyecto_2022_2.service;
import com.cibertec.movil_modelo_proyecto_2022_2.entity.Autor;
import com.cibertec.movil_modelo_proyecto_2022_2.entity.Editorial;
import com.cibertec.movil_modelo_proyecto_2022_2.entity.Revista;

import java.util.List;

import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface ServiceAutor {
    @POST("autor")
    public Call<Autor> insertarAutor(@Body Autor autor);
    @GET("autor")
    Call<List<Autor>> getAutores();
    @GET("autor/porNombre/{nombre}")
    public Call<List<Autor>> listaAutorPorNombre(@Path("nombre")String nombre);
    @PUT("autor")
    public Call<Autor> actualizaAutor(@Body Autor obj);
}
