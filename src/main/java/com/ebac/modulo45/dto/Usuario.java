package com.ebac.modulo45.dto;

import com.ebac.modulo45.controller.Response;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Builder
@Data
@AllArgsConstructor
@RequiredArgsConstructor
public class Usuario {
    private int idUsuario;
    private String nombre;
    private int edad;
    private List<Telefono> telefonos = new ArrayList<>();

    public static Usuario creaUsuarioVacio() {
        Telefono telefono = Telefono.builder()
                .numero("")
                .lada(0)
                .tipoTelefono("")
                .build();
        return Usuario.builder()
                .idUsuario(0)
                .nombre("")
                .edad(0)
                .telefonos(List.of(telefono))
                .build();
    }
}
