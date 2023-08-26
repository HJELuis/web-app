package com.ebac.modulo45.controller;

import com.ebac.modulo45.dto.ResponseWrapper;
import com.ebac.modulo45.dto.Telefono;
import com.ebac.modulo45.dto.Usuario;
import com.ebac.modulo45.feign.FeignUserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Slf4j
@RestController
public class UsuarioController {

    @Autowired
    FeignUserService feignUserService;

    @RequestMapping(value = "/usuario", method = RequestMethod.GET)
    public Object informacionUsuario(HttpServletRequest request, HttpServletResponse response, Model model) {
        String idUsuario = request.getParameter("idUsuario");
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("usuario");
        Usuario usuario = Usuario.creaUsuarioVacio();

        if (!Objects.isNull(idUsuario) && !idUsuario.isEmpty()) {
            ResponseWrapper<Usuario> usuarioResponse = feignUserService.getUserById(Integer.parseInt(idUsuario));
            if (usuarioResponse.isSuccess()) {
                usuario = usuarioResponse.getResponseEntity().getBody();
            }
        }

        model.addAttribute("usuario", usuario);
        return modelAndView;
    }

    @RequestMapping(value = "/formulario-usuario", method = RequestMethod.GET)
    public Object formularioUsuario(HttpServletRequest request, HttpServletResponse response, Model model) {
        String idUsuario = request.getParameter("idUsuario");
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("formulario-usuario");

        Usuario usuario = Usuario.creaUsuarioVacio();
        Telefono telefono = usuario.getTelefonos().get(0);
        model.addAttribute("propositoFormulario", "Crear usuario");

        if (!Objects.isNull(idUsuario) && !idUsuario.isEmpty()) {
            ResponseWrapper<Usuario> usuarioResponse = feignUserService.getUserById(Integer.parseInt(idUsuario));
            if (usuarioResponse.isSuccess()) {
                usuario = usuarioResponse.getResponseEntity().getBody();
                // Solo extraemos el primer telefono (En caso de tener)
                // Actividad sugerida: Generar funcionalidad para retornar todos los telefonos en caso de tener mas de 1 y generar el formulario correspondiente
                if (usuario.getTelefonos().size() > 0) {
                    telefono = usuario.getTelefonos().get(0);
                }
                model.addAttribute("propositoFormulario", "Actualizar usuario");
            }
        }

        model.addAttribute("usuario", usuario);
        model.addAttribute("telefono", telefono);
        return modelAndView;
    }

    @RequestMapping(value = "/guardar-usuario", method = RequestMethod.POST)
    public ResponseEntity<Response> saveUserConfiguration(HttpServletRequest request) {
        log.info("Guardando información de Walmart para usuario {}", request.getParameter("GPSServerUser"));
        HttpStatus statusCode = HttpStatus.OK;
        Response response = null;

        try {
            String usuarioId = request.getParameter("FormUsuarioId");
            String usuarioNombre = request.getParameter("FormUsuarioNombre");
            String usuarioEdad = request.getParameter("FormUsuarioEdad");
            String telefonoTipo = request.getParameter("FormTelefonoTipo");
            String telefonoLada = request.getParameter("FormTelefonoLada");
            String telefonoNumero = request.getParameter("FormTelefonoNumero");

            // Validar informacion recibida
            // ...

            // Si el usuarioId tiene informacion se trata de una actualizacion por lo tanto seteamos el id recibido
            Usuario.UsuarioBuilder usuarioBuilder = Usuario.builder();
            if (!usuarioId.equals("0")) {
                usuarioBuilder = usuarioBuilder.idUsuario(Integer.parseInt(usuarioId));
            }

            // En caso de haber agregado la funcionalidad para editar mas de 1 telefono, esta seccion se debera adecuar
            // para recibir todos los telefonos y realizar el guardado/actualizacion

            //Generamos el telefono recibido
            Telefono telefono = Telefono.builder()
                    .tipoTelefono(telefonoTipo)
                    .lada(Integer.parseInt(telefonoLada))
                    .numero(telefonoNumero)
                    .build();
            //Generamos el usuario
            Usuario usuario = usuarioBuilder
                    .nombre(usuarioNombre)
                    .edad(Integer.parseInt(usuarioEdad))
                    .telefonos(List.of(telefono))
                    .build();

            ResponseWrapper<Usuario> user = feignUserService.createUser(usuario);
            if (!user.isSuccess()) {
                ErrorResponse errorResponse = new ErrorResponse();
                errorResponse.addMessage(user.getMessage());
                response = errorResponse;

                statusCode = HttpStatus.resolve(user.getResponseEntity().getStatusCodeValue());
            }
        } catch(Exception e) {
            statusCode = HttpStatus.BAD_REQUEST;
        }

        return new ResponseEntity<>(response, statusCode);
    }
}
