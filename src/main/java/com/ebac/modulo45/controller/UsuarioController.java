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
        boolean actualizacion = false;
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
                List<Telefono> telefonos = new ArrayList<>();

                if (usuario.getTelefonos().size() > 0) {
                    telefonos = usuario.getTelefonos();
                    actualizacion = true;
                }
                model.addAttribute("telefonos", telefonos);
                model.addAttribute("propositoFormulario", "Actualizar usuario");
            }
        }

        model.addAttribute("usuario", usuario);
        if(!actualizacion) model.addAttribute("telefono", telefono);
        return modelAndView;
    }

    @RequestMapping(value = "/guardar-usuario", method = RequestMethod.POST)
    public ResponseEntity<Response> saveUserConfiguration(HttpServletRequest request) {
        log.info("Guardando información de Walmart para usuario {}", request.getParameter("GPSServerUser"));
        HttpStatus statusCode = HttpStatus.OK;
        Response response = null;
        ErrorResponse errorResponse = new ErrorResponse();
        ResponseWrapper<Usuario> responseUsuario = new ResponseWrapper<>();
        Usuario auxUsuario = new Usuario();

        try {
            String usuarioId = request.getParameter("FormUsuarioId");
            String usuarioNombre = request.getParameter("FormUsuarioNombre");
            String usuarioEdad = request.getParameter("FormUsuarioEdad");





            // Si el usuarioId tiene informacion se trata de una actualizacion por lo tanto seteamos el id recibido
            Usuario.UsuarioBuilder usuarioBuilder = Usuario.builder();
            if (!usuarioId.equals("0")) {
                usuarioBuilder = usuarioBuilder.idUsuario(Integer.parseInt(usuarioId));
                responseUsuario = feignUserService.getUserById(Integer.parseInt(usuarioId));
                auxUsuario = responseUsuario.getResponseEntity().getBody();
            }

            // En caso de haber agregado la funcionalidad para editar mas de 1 telefono, esta seccion se debera adecuar
            // para recibir todos los telefonos y realizar el guardado/actualizacion

            int index = 0;
            List<Telefono> telefonos = new ArrayList<>();


            while (true) {
                String tipoTelefono = request.getParameter("telefonos[" + index + "].tipoTelefono");
                String lada = request.getParameter("telefonos[" + index + "].lada");
                String numero = request.getParameter("telefonos[" + index + "].numero");

                if (tipoTelefono == null || lada == null || numero == null) {
                    break;
                }

                // Validar informacion recibida
                // ...
                if(usuarioNombre.length() == 0 || usuarioEdad.length() == 0 || tipoTelefono.length() == 0 || lada.length() == 0 || numero.length() == 0) {

                    errorResponse.addMessage("Existen campos vacíos, se requieren llenar");
                    response = errorResponse;

                    statusCode = HttpStatus.BAD_REQUEST;

                    throw new MiExcepcion("Existen campos vacíos, se requieren llenar");
                } else if (!tipoTelefono.equals("Casa") && !tipoTelefono.equals("casa") && !tipoTelefono.equals("Oficina") && !tipoTelefono.equals("oficina") ) {

                    errorResponse.addMessage("El tipo de teléfono no es vállido");
                    response = errorResponse;

                    statusCode = HttpStatus.BAD_REQUEST;

                    throw new MiExcepcion("El tipo de teléfono no es válido");
                } else if (!(numero.length() == 10)) {
                    errorResponse.addMessage("El número de teléfono es demasiado grande o muy pequeño");
                    response = errorResponse;

                    statusCode = HttpStatus.BAD_REQUEST;

                    throw new MiExcepcion("El número de teléfono es demasiado grande o muy pequeño");
                }

                Telefono auxTelefono = auxUsuario.getTelefonos().get(index);
                int idTelefono = auxTelefono.getIdTelefono();

                //Generamos el telefono recibido
                Telefono telefono = Telefono.builder()
                        .idTelefono(idTelefono)
                        .tipoTelefono(tipoTelefono)
                        .lada(Integer.parseInt(lada))
                        .numero(numero)
                        .build();
                telefonos.add(telefono);

                index++;
            }






            //Generamos el usuario
            Usuario usuario = usuarioBuilder
                    .nombre(usuarioNombre)
                    .edad(Integer.parseInt(usuarioEdad))
                    .telefonos(telefonos)
                    .build();

            ResponseWrapper<Usuario> user = feignUserService.createUser(usuario);
            if (!user.isSuccess()) {
                //ErrorResponse errorResponse = new ErrorResponse();
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

class MiExcepcion extends Exception {
    public MiExcepcion(String mensaje) {
        super(mensaje);
    }
}
