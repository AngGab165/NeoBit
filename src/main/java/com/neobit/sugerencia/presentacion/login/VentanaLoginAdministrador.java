package com.neobit.sugerencia.presentacion.login;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import com.neobit.sugerencia.negocio.ServicioUsuario;
import com.neobit.sugerencia.negocio.modelo.Rol;
import com.neobit.sugerencia.negocio.modelo.Usuario;
import com.neobit.sugerencia.presentacion.principal.VentanaPrincipalAdministrador;

import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

@Component
public class VentanaLoginAdministrador extends Application {

    @Autowired
    private ServicioUsuario servicioUsuario;

    @Autowired
    @Lazy
    private VentanaRegistroAdministrador ventanaRegistroAdministrador;

    @Autowired
    @Lazy
    private VentanaPrincipalAdministrador ventanaPrincipalAdministrador;

    private Stage stage;
    private TextField tfUsuario;
    private PasswordField pfClave;

    @Override
    public void start(Stage primaryStage) {
        this.stage = primaryStage;
        stage.setTitle("Login Administrador");

        tfUsuario = new TextField();
        tfUsuario.setPromptText("Ingrese su usuario");

        pfClave = new PasswordField();
        pfClave.setPromptText("Ingrese su contraseña");

        Button btnLogin = new Button("Iniciar sesión");
        Button btnRecuperarContrasena = new Button("Recuperar Contraseña");
        Button btnRegistrarse = new Button("Registrarse");

        btnLogin.setOnAction(e -> loginAdministrador(tfUsuario.getText(), pfClave.getText()));
        btnRecuperarContrasena.setOnAction(e -> recuperarContrasena(tfUsuario.getText()));
        btnRegistrarse.setOnAction(e -> ventanaRegistroAdministrador.mostrar("Administrador"));

        VBox vbox = new VBox(10, tfUsuario, pfClave, btnLogin, btnRecuperarContrasena, btnRegistrarse);
        vbox.setAlignment(Pos.CENTER);
        vbox.setStyle("-fx-padding: 20; -fx-background-color: #f0f0f0;");

        Scene scene = new Scene(vbox, 300, 200);
        stage.setScene(scene);
        stage.show();
    }

    public void loginAdministrador(String usuario, String contrasena) {
        if (usuario.isEmpty() || contrasena.isEmpty()) {
            mostrarMensajeError("Usuario y contraseña son requeridos.");
            return;
        }

        Usuario admin = servicioUsuario.buscarUsuarioPorNombre(usuario);

        if (admin != null && admin.getContrasena().equals(contrasena) && admin.getRol() == Rol.ADMINISTRADOR) {
            mostrarMensajeExito("Login exitoso.");
            cerrar();
            ventanaPrincipalAdministrador.mostrar();
        } else {
            mostrarMensajeError("Usuario o contraseña incorrectos.");
        }
    }

    public void recuperarContrasena(String usuario) {
        if (usuario.isEmpty()) {
            mostrarMensajeError("Por favor ingresa tu usuario.");
            return;
        }

        if (servicioUsuario.existeUsuario(usuario)) {
            mostrarMensajeExito("Te hemos enviado un enlace para recuperar tu contraseña.");
        } else {
            mostrarMensajeError("El usuario no existe.");
        }
    }

    public void mostrarMensajeError(String mensaje) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }

    public void mostrarMensajeExito(String mensaje) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Éxito");
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }

    public void cerrar() {
        stage.close();
    }

    public static void main(String[] args) {
        launch(args);
    }

    public void muestra() {
        Stage stage = new Stage();
        start(stage);
    }

    public void mostrarAlerta(String titulo, String mensaje) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }
}
