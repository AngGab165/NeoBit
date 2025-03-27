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

import com.neobit.sugerencia.presentacion.principal.VentanaPrincipalAdministrador;

@Component
public class VentanaRegistroAdministrador {

    @Autowired
    private ControlLoginAdministrador controlLoginAdministrador;

    @Autowired
    @Lazy
    private VentanaPrincipalAdministrador ventanaPrincipalAdministrador;

    public void mostrar(String tipoUsuario) {
        Stage stage = new Stage();
        stage.setTitle("Registro " + tipoUsuario);

        // Campos de registro
        TextField txtUsuario = new TextField();
        txtUsuario.setPromptText("Nombre de Usuario");

        TextField txtNombre = new TextField();
        txtNombre.setPromptText("Nombre Completo");

        TextField txtCorreo = new TextField();
        txtCorreo.setPromptText("Correo Electrónico");

        PasswordField txtContrasena = new PasswordField();
        txtContrasena.setPromptText("Contraseña");

        Button btnRegistrar = new Button("Registrar");
        btnRegistrar.setOnAction(e -> {
            controlLoginAdministrador.registrarAdministrador(
                    txtUsuario.getText(),
                    txtNombre.getText(),
                    txtCorreo.getText(),
                    txtContrasena.getText(),
                    stage);
        });

        Button btnRegresarLogin = new Button("Regresar al Login");
        btnRegresarLogin.setOnAction(e -> stage.close());

        VBox vbox = new VBox(20, txtUsuario, txtNombre, txtCorreo, txtContrasena, btnRegistrar, btnRegresarLogin);
        vbox.setAlignment(Pos.CENTER);
        vbox.setPadding(new Insets(20));
        vbox.setStyle("-fx-background-color: #eaf4f4;");

        Scene scene = new Scene(vbox, 300, 400); // Aumentado el tamaño para ajustar todos los campos
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
