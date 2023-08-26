package com.ebac.modulo45.feign;

import com.ebac.modulo45.dto.ResponseWrapper;
import com.ebac.modulo45.dto.Usuario;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class FeignUserService {

    public final FeignApiClient feignApiClient;

    @Autowired
    public FeignUserService(FeignApiClient FeignApiClient) {
        this.feignApiClient = FeignApiClient;
    }

    public ResponseWrapper<List<Usuario>> getUsers() {
        log.info("Obteniendo usuarios");
        return feignApiClient.getUsers();
    }

    public ResponseWrapper<Usuario> getUserById(int id) {
        log.info("Obteniendo usuario con id {}", id);
        // Tarea agregar validaciones pertinentes
        return feignApiClient.getUserById(id);
    }

    public ResponseWrapper<Usuario> createUser(Usuario user) {
        log.info("Creando usuario {}", user);
        return feignApiClient.createUser(user);
    }

    public ResponseWrapper<Usuario> updateUser(int id, Usuario user) {
        log.info("Actualizando usuario {}", user.getNombre());
        return feignApiClient.updateUser(user, id);
    }

    public void deleteUser(int id) {
        log.info("Eliminando usuario con id {}", id);
        feignApiClient.deleteUser(id);
    }
}
