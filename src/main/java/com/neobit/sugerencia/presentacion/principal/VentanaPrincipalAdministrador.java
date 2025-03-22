package com.neobit.sugerencia.presentacion.principal;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class VentanaPrincipalAdministrador extends Application {

    @Autowired
    private ControlPrincipalAdministrador controlPrincipalAdministrador;

    private Stage stage;
    private boolean initialized = false; // Para asegurarnos de no inicializar la ventana dos veces

    @Override
    public void start(Stage primaryStage) {
        this.stage = primaryStage;
        inicializarUI();
        stage.show();
    }

    private void inicializarUI() {
        if (initialized)
            return; // Evita inicialización múltiple

        stage.setTitle("Ventana de Administrador");

        // Opciones para el administrador
        Button btnGestionEmpleados = new Button("Gestionar Empleados");
        btnGestionEmpleados.setOnAction(e -> controlPrincipalAdministrador.muestraVentanaGestionEmpleados());
        btnGestionEmpleados.setStyle(
                "-fx-background-color: #006666; -fx-text-fill: white; -fx-font-size: 14px; -fx-padding: 10px 20px;");

        Button btnGestionSugerencias = new Button("Revisar Sugerencias");
        btnGestionSugerencias.setOnAction(e -> controlPrincipalAdministrador.muestraVentanaRevisarSugerencias());
        btnGestionSugerencias.setStyle(
                "-fx-background-color: #006666; -fx-text-fill: white; -fx-font-size: 14px; -fx-padding: 10px 20px;");

        // Layout
        VBox vbox = new VBox(20, btnGestionEmpleados, btnGestionSugerencias);
        vbox.setAlignment(Pos.CENTER);
        vbox.setPadding(new Insets(20));
        vbox.setStyle("-fx-background-color: #eaf4f4;");

        Scene scene = new Scene(vbox, 300, 250);
        stage.setScene(scene);

        initialized = true; // Marcar como inicializada
    }

    public void mostrar() {
        Platform.runLater(() -> {
            if (stage == null) {
                stage = new Stage();
                inicializarUI();
            }
            stage.show();
        });
    }

    public static void main(String[] args) {
        launch(args);
    }
}
