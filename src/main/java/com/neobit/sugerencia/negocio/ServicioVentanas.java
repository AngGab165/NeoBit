package com.neobit.sugerencia.negocio;

import com.neobit.sugerencia.presentacion.login.VentanaLoginEmpleado;
import com.neobit.sugerencia.presentacion.principal.VentanaAdministrador;
import com.neobit.sugerencia.presentacion.login.VentanaLoginAdministrador;
import com.neobit.sugerencia.presentacion.empleados.VentanaEmpleados;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ServicioVentanas {

    @Autowired
    private final VentanaLoginEmpleado ventanaLoginEmpleado;
    @Autowired
    private final VentanaLoginAdministrador ventanaLoginAdministrador;
    private final VentanaEmpleados ventanaEmpleados;
    private final VentanaAdministrador ventanaAdministrador;

    // Constructor para inyectar las dependencias
    public ServicioVentanas(VentanaLoginEmpleado ventanaLoginEmpleado,
            VentanaLoginAdministrador ventanaLoginAdministrador,
            VentanaEmpleados ventanaEmpleados,
            VentanaAdministrador ventanaAdministrador) {
        this.ventanaLoginEmpleado = ventanaLoginEmpleado;
        this.ventanaLoginAdministrador = ventanaLoginAdministrador;
        this.ventanaEmpleados = ventanaEmpleados;
        this.ventanaAdministrador = ventanaAdministrador;
    }

    // Métodos para mostrar las ventanas
    public void muestraLoginEmpleado() {
        ventanaLoginEmpleado.muestra();
    }

    public void muestraLoginAdministrador() {
        ventanaLoginAdministrador.muestra();
    }

    public void muestraVentanaEmpleados() {
        ventanaEmpleados.muestra();
    }

    public void muestraVentanaAdministrador() {
        ventanaAdministrador.muestra();
    }
}