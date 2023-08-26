package com.ebac.modulo45.feign;

import com.ebac.modulo45.dto.ResponseWrapper;
import com.ebac.modulo45.dto.Usuario;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

@Slf4j
class FeignUserServiceTest {

    public static FeignUserService feignService;

    @BeforeAll
    static void setUp() {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext();
        context.scan("com.ebac.modulo45.feign");
        context.refresh();

        FeignApiClient feignApiClient = (FeignApiClient) context.getBean("feignApiClient");

        feignService = new FeignUserService(feignApiClient);
    }

    @Test
    public void testGetUsers() {
        feignService.getUsers().getResponseEntity().getBody().forEach(System.out::println);
    }

    @Test
    public void testGetUserById() {
        Usuario usuario = feignService.getUserById(23).getResponseEntity().getBody();
        log.info("Usuario: {}", usuario);
    }

    @Test
    public void testCreateUser() {
        Usuario usuario = Usuario.builder()
                .nombre("Ruben")
                .edad(29)
                .build();
        ResponseWrapper<Usuario> responseWrapper = feignService.createUser(usuario);
        log.info("Usuario creado: {}", responseWrapper.getResponseEntity().getBody());
    }

    @Test
    public void testUpdateUserById() {
        Usuario usuario = Usuario.builder()
                .nombre("Rube")
                .edad(25)
                .build();
        Usuario usuarioActualizado = feignService.updateUser(23, usuario).getResponseEntity().getBody();
        log.info("Usuario actualizado: {}", usuarioActualizado);
    }

    @Test
    public void testDeleteUserById() {
        int id = 23;
        feignService.deleteUser(id);
        log.info("Usuario con id {} fue eliminado", id);
    }
}