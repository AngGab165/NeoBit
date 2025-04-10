package com.neobit.sugerencia.presentacion.principal;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class VentanaPrincipal {

    @Autowired
    private ControlPrincipal controlPrincipal;

    public void setControl(ControlPrincipal controlPrincipal) {
        this.controlPrincipal = controlPrincipal;
    }

    public void start(Stage primaryStage) {
        primaryStage.setTitle("Seleccione su Rol");

        // Título principal
        Label titulo = new Label("Sistema Sugerencias - Neobit");
        titulo.setStyle("-fx-font-size: 20px; -fx-font-weight: bold; -fx-text-fill: #004d4d;");

        // Subtítulo
        Label subtitulo = new Label("Seleccione su rol:");
        subtitulo.setStyle("-fx-font-size: 16px; -fx-font-weight: normal; -fx-text-fill: #006666;");

        // Botón para elegir el rol de Empleado
        Button btnEmpleado = new Button("Empleado");
        btnEmpleado.setStyle(
                "-fx-background-color: #006666; -fx-text-fill: white; -fx-font-size: 14px; " +
                        "-fx-padding: 10px 20px; -fx-border-radius: 5px;");
        btnEmpleado.setOnAction(e -> controlPrincipal.muestraLoginEmpleado());

        // Botón para elegir el rol de Administrador
        Button btnAdministrador = new Button("Administrador");
        btnAdministrador.setStyle(
                "-fx-background-color: #006666; -fx-text-fill: white; -fx-font-size: 14px; " +
                        "-fx-padding: 10px 20px; -fx-border-radius: 5px;");
        btnAdministrador.setOnAction(e -> controlPrincipal.muestraLoginAdministrador());

        // Diseño de la ventana
        VBox vbox = new VBox(15, titulo, subtitulo, btnEmpleado, btnAdministrador);
        vbox.setAlignment(Pos.CENTER);
        vbox.setPadding(new Insets(20));
        vbox.setStyle("-fx-background-color: #eaf4f4;");

        // Crear la escena y configurar la ventana
        Scene scene = new Scene(vbox, 350, 300);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public void muestra() {
        Stage stage = new Stage();
        start(stage);
    }
}
