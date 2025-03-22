package com.neobit.sugerencia;

import com.neobit.sugerencia.presentacion.principal.VentanaPrincipal;
import com.neobit.sugerencia.presentacion.login.VentanaLoginEmpleado;
import com.neobit.sugerencia.presentacion.login.VentanaLoginAdministrador;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Controller;
import javafx.stage.Stage;

@Controller
public class MyController {

    @Autowired
    private VentanaPrincipal ventanaPrincipal;

    @Autowired
    @Lazy
    private VentanaLoginEmpleado ventanaLoginEmpleado;

    @Autowired
    private VentanaLoginAdministrador ventanaLoginAdministrador;

    public void initialize(Stage primaryStage) {
        ventanaPrincipal.start(primaryStage); // Muestra la ventana principal
    }

    public void muestraLoginEmpleado() {
        System.out.println("Mostrando ventana de login para Empleado.");
        Stage stage = new Stage();
        ventanaLoginEmpleado.start(stage); // Abre la ventana de login
    }

    public void muestraLoginAdministrador() {
        System.out.println("Mostrando ventana de login para Administrador.");
        Stage stage = new Stage();
        ventanaLoginAdministrador.start(stage); // Abre la ventana de login
    }
}
