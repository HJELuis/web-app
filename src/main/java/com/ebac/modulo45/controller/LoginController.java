package com.ebac.modulo45.controller;

import com.ebac.modulo45.dto.InfoUsuario;
import com.ebac.modulo45.feign.FeignUserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import java.io.IOException;

@Slf4j
@RestController
public class LoginController {

    @Autowired
    FeignUserService feignApiClient;

    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public Object login(HttpServletRequest request, HttpServletResponse response, Model model) throws IOException {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("pagina-login");

        String username = request.getParameter("username");
        String password = request.getParameter("password");

        // Actividad sugerida, validar el usuario y el password contra una tabla en la DB
        InfoUsuario  infoUsuario = feignApiClient.getInfoUsuario().getResponseEntity().getBody();
        if (username.equals(infoUsuario.getNombreUsuario()) && password.equals(infoUsuario.getContrasena())) {
            request.getSession().setAttribute("username", username);
            response.sendRedirect("/");
        }
//        if (username.equals("salvador") && password.equals("123")) {
//            request.getSession().setAttribute("username", username);
//            response.sendRedirect("/");
//        }

        return modelAndView;
    }

    @RequestMapping(value = "/logout", method = RequestMethod.GET)
    public Object logout(HttpServletRequest request, HttpServletResponse response) {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("pagina-login");

        request.getSession().setAttribute("username", null);

        return modelAndView;
    }
}
