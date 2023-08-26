package com.ebac.modulo45.controller;

import com.ebac.modulo45.dto.ResponseWrapper;
import com.ebac.modulo45.dto.Usuario;
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

import java.util.ArrayList;
import java.util.List;

@Slf4j
@RestController
public class IndexController {

    @Autowired
    FeignUserService feignApiClient;

    @RequestMapping(value = "/", method = RequestMethod.GET)
    public Object index(HttpServletRequest request, HttpServletResponse response, Model model) {
        log.info("Ingresando a página principal");
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("index");

        ResponseWrapper<List<Usuario>> usersResponse = feignApiClient.getUsers();
        List<Usuario> usuarios = new ArrayList<>();
        if (usersResponse.isSuccess()) {
            usuarios = usersResponse.getResponseEntity().getBody();
        }

        model.addAttribute("numUsuarios", usuarios.size());
        model.addAttribute("usuarios", usuarios);

        return modelAndView;
    }
}
