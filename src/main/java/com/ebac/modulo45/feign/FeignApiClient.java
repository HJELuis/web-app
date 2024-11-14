package com.ebac.modulo45.feign;

import com.ebac.modulo45.dto.InfoUsuario;
import com.ebac.modulo45.dto.ResponseWrapper;
import com.ebac.modulo45.dto.Usuario;
import feign.Headers;
import feign.Param;
import feign.RequestLine;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public interface FeignApiClient {

    @RequestLine("GET /usuarios")
    ResponseWrapper<List<Usuario>> getUsers();

    @RequestLine("GET /usuarios/{id}")
    ResponseWrapper<Usuario> getUserById(@Param("id") int id);

    @RequestLine("POST /usuarios")
    @Headers({"Content-Type: application/json"})
    ResponseWrapper<Usuario> createUser(Usuario usuario);

    @RequestLine("PUT /usuarios/{id}")
    @Headers({"Content-Type: application/json"})
    ResponseWrapper<Usuario> updateUser(Usuario usuario, @Param("id") int id);

    @RequestLine("DELETE /usuarios/{id}")
    void deleteUser(@Param("id") int id);

    /*
    Sección para obtener informacion de la tabla INFO_USUARIO
     */
    @RequestLine("GET /info_usuario")
    ResponseWrapper<InfoUsuario> getInfoUsuario();
}
