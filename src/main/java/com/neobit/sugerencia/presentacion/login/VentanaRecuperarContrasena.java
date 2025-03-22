package com.neobit.sugerencia.presentacion.login;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

@Component
public class VentanaRecuperarContrasena {

    @Autowired
    @Lazy
    private ControlRecuperarContrasena controlRecuperarContrasena;

    @Autowired
    private VentanaLoginAdministrador ventanaLoginAdministrador;

    public void mostrar() {
        Stage stage = new Stage();
        stage.setTitle("Recuperar Contraseña");

        // Campos de entrada
        TextField txtCorreo = new TextField();
        txtCorreo.setPromptText("Correo Electrónico");

        Button btnRecuperar = new Button("Recuperar Contraseña");
        btnRecuperar.setOnAction(e -> {
            controlRecuperarContrasena.recuperarContrasena(txtCorreo.getText());
        });

        // Botón para regresar al Login del Administrador
        Button btnRegresarLogin = new Button("Regresar al Login");
        btnRegresarLogin.setOnAction(e -> {
            stage.close();
            ventanaLoginAdministrador.start(new Stage());
        });

        VBox vbox = new VBox(20, txtCorreo, btnRecuperar, btnRegresarLogin);
        vbox.setAlignment(Pos.CENTER);
        vbox.setPadding(new Insets(20));
        vbox.setStyle("-fx-background-color: #eaf4f4;");

        Scene scene = new Scene(vbox, 300, 250);
        stage.setScene(scene);
        stage.show();
    }

    public void mostrarAlerta(String titulo, String mensaje) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }
}