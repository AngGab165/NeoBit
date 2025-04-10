package com.neobit.sugerencia.presentacion.login;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import com.neobit.sugerencia.negocio.ServicioUsuario;
import com.neobit.sugerencia.negocio.modelo.Rol;
import com.neobit.sugerencia.negocio.modelo.Usuario;
import com.neobit.sugerencia.presentacion.principal.ControlPrincipalAdministrador;
import com.neobit.sugerencia.presentacion.principal.VentanaPrincipalAdministrador;

import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
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

    @Autowired
    @Lazy
    private ControlPrincipalAdministrador controlPrincipalAdministrador;

    private Stage stage;
    private TextField tfUsuario;
    private PasswordField pfClave;

    @Override
    public void start(Stage primaryStage) {
        this.stage = primaryStage;
        stage.setTitle("Login Administrador");

        Label lblTitulo = new Label("Iniciar Sesión");
        lblTitulo.setFont(Font.font("Arial", FontWeight.BOLD, 20));
        lblTitulo.setStyle("-fx-text-fill: #006666;");

        tfUsuario = new TextField();
        tfUsuario.setPromptText("Ingrese su usuario");
        estilizarCampo(tfUsuario);

        pfClave = new PasswordField();
        pfClave.setPromptText("Ingrese su contraseña");
        estilizarCampo(pfClave);

        Button btnLogin = new Button("Iniciar sesión");
        estilizarBoton(btnLogin, "#006666");
        Button btnRecuperarContrasena = new Button("Recuperar Contraseña");
        estilizarBoton(btnRecuperarContrasena, "#0099cc");
        Button btnRegistrarse = new Button("Registrarse");
        estilizarBoton(btnRegistrarse, "#0099cc");

        btnLogin.setOnAction(e -> loginAdministrador(tfUsuario.getText(), pfClave.getText()));
        btnRecuperarContrasena.setOnAction(e -> recuperarContrasena(tfUsuario.getText()));
        btnRegistrarse.setOnAction(e -> ventanaRegistroAdministrador.mostrar("Administrador"));

        VBox vbox = new VBox(10, lblTitulo, tfUsuario, pfClave, btnLogin, btnRecuperarContrasena, btnRegistrarse);
        vbox.setAlignment(Pos.CENTER);
        vbox.setStyle("-fx-background-color: #eaf4f4;");

        Scene scene = new Scene(vbox, 350, 300);
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
            controlPrincipalAdministrador.setNombreAdministrador(admin.getNombre());
            ventanaPrincipalAdministrador.setUsuario(admin);
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

    private void estilizarCampo(TextField campo) {
        campo.setStyle("-fx-border-color: #006666; -fx-background-color: #ffffff; -fx-text-fill: #006666;");
    }

    private void estilizarBoton(Button boton, String color) {
        boton.setStyle("-fx-background-color: " + color
                + "; -fx-text-fill: #ffffff; -fx-padding: 7px 15px; -fx-border-radius: 5px;");
    }
}
