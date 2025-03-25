package com.neobit.sugerencia.presentacion.principal;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import com.neobit.sugerencia.presentacion.notificaciones.VentanaNotificacionesEmpleado;
import com.neobit.sugerencia.presentacion.sugerencia.VentanaSugerencias;

@Component
public class VentanaPrincipalEmpleado extends Application {

    private ControlPrincipalEmpleado control;

    @Autowired
    @Lazy
    private VentanaSugerencias ventanaSugerencias;

    @Autowired
    @Lazy
    private VentanaNotificacionesEmpleado ventanaNotificaciones;

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Ventana de Empleado");

        // Opciones para el empleado
        Button btnGestionSugerencias = new Button("Gestionar Sugerencias");
        btnGestionSugerencias.setOnAction(e -> ventanaSugerencias.mostrar());

        Button btnGestionNotificaciones = new Button("Gestionar Notificaciones");
        btnGestionNotificaciones.setOnAction(e -> ventanaNotificaciones.muestra());

        VBox vbox = new VBox(20, btnGestionSugerencias, btnGestionNotificaciones);
        vbox.setAlignment(Pos.CENTER);
        vbox.setPadding(new Insets(20));

        Scene scene = new Scene(vbox, 300, 250);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    // Método para inyectar el controlador
    public void setControl(ControlPrincipalEmpleado control) {
        this.control = control;
    }

    public static void main(String[] args) {
        launch(args);
    }

    public void muestra(Stage primaryStage) {
        start(primaryStage);
    }

    public void mostrar() {
        Stage stage = new Stage();
        start(stage);
    }
}