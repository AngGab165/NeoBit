package com.neobit.sugerencia.presentacion.principal;

import com.neobit.sugerencia.presentacion.empleados.VentanaEmpleados;
import com.neobit.sugerencia.presentacion.sugerencia.VentanaSugerencias;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ControlAdministrador {

    @Autowired
    private VentanaEmpleados ventanaEmpleados;

    @Autowired
    private VentanaSugerencias ventanaSugerencias;

    // Muestra la ventana para gestionar empleados
    public void muestraVentanaGestionEmpleados() {
        ventanaEmpleados.muestra(); // Aquí debería llamar al método muestra de la clase VentanaEmpleados
    }

    // Muestra la ventana para revisar sugerencias
    public void muestraVentanaRevisarSugerencias() {
        ventanaSugerencias.muestra(); // Aquí debería llamar al método muestra de la clase VentanaSugerencias
    }
}
