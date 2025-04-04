package com.neobit.sugerencia.presentacion.login;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
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

        Label lblTitulo = new Label("Registrar Administrador");
        lblTitulo.setFont(Font.font("Arial", FontWeight.BOLD, 18));
        lblTitulo.setStyle("-fx-text-fill: #006666;");

        // Campos de registro
        TextField txtUsuario = new TextField();
        txtUsuario.setPromptText("Nombre de Usuario");
        estilizarCampo(txtUsuario);

        TextField txtNombre = new TextField();
        txtNombre.setPromptText("Nombre Completo");
        estilizarCampo(txtNombre);

        TextField txtCorreo = new TextField();
        txtCorreo.setPromptText("Correo Electrónico");
        estilizarCampo(txtCorreo);

        PasswordField txtContrasena = new PasswordField();
        txtContrasena.setPromptText("Contraseña");
        estilizarCampo(txtContrasena);

        Button btnRegistrar = new Button("Registrar");
        estilizarBoton(btnRegistrar, "#006666");
        btnRegistrar.setOnAction(e -> {
            controlLoginAdministrador.registrarAdministrador(
                    txtUsuario.getText(),
                    txtNombre.getText(),
                    txtCorreo.getText(),
                    txtContrasena.getText(),
                    stage);
        });

        Button btnRegresarLogin = new Button("Regresar al Login");
        estilizarBoton(btnRegresarLogin, "#0099cc");
        btnRegresarLogin.setOnAction(e -> stage.close());

        VBox vbox = new VBox(20, lblTitulo, txtUsuario, txtNombre, txtCorreo, txtContrasena, btnRegistrar,
                btnRegresarLogin);
        vbox.setAlignment(Pos.CENTER);
        vbox.setPadding(new Insets(20));
        vbox.setStyle("-fx-background-color: #eaf4f4;");

        Scene scene = new Scene(vbox, 350, 450); // Aumentado el tamaño para ajustar todos los campos
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

    // Métodos auxiliares para estilos
    private void estilizarCampo(TextField campo) {
        campo.setStyle("-fx-border-color: #006666; -fx-background-color: #ffffff; -fx-text-fill: #006666;");
    }

    private void estilizarBoton(Button boton, String color) {
        boton.setStyle("-fx-background-color: " + color
                + "; -fx-text-fill: #ffffff; -fx-padding: 7px 15px; -fx-border-radius: 5px;");
    }
}
