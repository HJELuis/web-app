package com.ebac.modulo45.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Builder
@Data
@AllArgsConstructor
@RequiredArgsConstructor
public class InfoUsuario {
    private int idInfoUsuario;
    private String nombreUsuario;
    private String contrasena;
}
