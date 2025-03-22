package com.neobit.sugerencia.presentacion.principal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import com.neobit.sugerencia.negocio.ServicioVentanas;
import javafx.stage.Stage;

@Component
public class ControlPrincipal {

    @Autowired
    @Lazy
    private ServicioVentanas servicioVentanas;

    private Stage primaryStage;
    private VentanaPrincipal ventanaPrincipal; // Declara ventanaPrincipal

    public void inicia(Stage primaryStage) {
        ventanaPrincipal.setControl(this);
        ventanaPrincipal.start(primaryStage); // Usamos start() de Application
    }

    public void muestraLoginEmpleado() {
        servicioVentanas.muestraLoginEmpleado();
    }

    public void muestraLoginAdministrador() {
        servicioVentanas.muestraLoginAdministrador();
    }

    public void muestraVentanaEmpleados() {
        servicioVentanas.muestraVentanaEmpleados();
    }

    public void muestraVentanaAdministrador() {
        servicioVentanas.muestraVentanaAdministrador();
    }
}
