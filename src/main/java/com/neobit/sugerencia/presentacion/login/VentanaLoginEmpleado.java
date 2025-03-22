package com.neobit.sugerencia.presentacion.login;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import com.neobit.sugerencia.negocio.ServicioUsuario;
import com.neobit.sugerencia.negocio.modelo.Usuario;
import com.neobit.sugerencia.presentacion.principal.VentanaPrincipalEmpleado;

import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

@Component
public class VentanaLoginEmpleado extends Application {

    @Autowired
    private ServicioUsuario servicioUsuario; // 🔹 Servicio de usuario para autenticación

    @Autowired
    private VentanaRegistroEmpleado ventanaRegistroEmpleado; // 🔹 Inyección de la ventana de registro

    @Autowired
    @Lazy
    private VentanaPrincipalEmpleado ventanaPrincipalEmpleado;

    private Stage stage;
    private TextField tfUsuario;
    private PasswordField pfClave;

    @Override
    public void start(Stage primaryStage) {
        this.stage = primaryStage;
        stage.setTitle("Login Empleado");

        // Inicializar los componentes
        tfUsuario = new TextField();
        tfUsuario.setPromptText("Ingrese su usuario");

        pfClave = new PasswordField();
        pfClave.setPromptText("Ingrese su contraseña");

        Button btnLogin = new Button("Iniciar sesión");
        Button btnRecuperarContrasena = new Button("Recuperar Contraseña");
        Button btnRegistrarse = new Button("Registrarse");

        // Acción al hacer clic en el botón de login
        btnLogin.setOnAction(e -> loginEmpleado(tfUsuario.getText(), pfClave.getText()));

        // Acción al hacer clic en el botón de recuperar contraseña
        btnRecuperarContrasena.setOnAction(e -> recuperarContrasena(tfUsuario.getText()));

        // Acción al hacer clic en el botón de registrarse
        btnRegistrarse.setOnAction(e -> mostrarFormularioRegistro());

        // Diseño de la ventana con VBox
        VBox vbox = new VBox(10, tfUsuario, pfClave, btnLogin, btnRecuperarContrasena, btnRegistrarse);
        vbox.setAlignment(Pos.CENTER);
        vbox.setStyle("-fx-padding: 20; -fx-background-color: #f0f0f0;");

        // Crear la escena y asignar la vista
        Scene scene = new Scene(vbox, 300, 200);
        stage.setScene(scene);
        stage.show();
    }

    // 🔹 Método para el login real con la BD
    public void loginEmpleado(String usuario, String contrasena) {
        if (usuario.isEmpty() || contrasena.isEmpty()) {
            mostrarMensajeError("Usuario y contraseña son requeridos.");
            return;
        }

        Usuario empleado = servicioUsuario.buscarUsuarioPorNombre(usuario);

        if (empleado != null && empleado.getContrasena().equals(contrasena)) {
            mostrarMensajeExito("Login exitoso.");
            cerrar(); // Cierra la ventana de login
            // Aquí podrías abrir la ventana principal del empleado
            ventanaPrincipalEmpleado.mostrar();
        } else {
            mostrarMensajeError("Usuario o contraseña incorrectos.");
        }
    }

    // 🔹 Método para la recuperación de contraseña
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

    // 🔹 Método para mostrar la ventana de registro usando la inyección de Spring
    public void mostrarFormularioRegistro() {
        ventanaRegistroEmpleado.mostrar();
    }

    // 🔹 Métodos para mostrar mensajes en pantalla
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

    // 🔹 Método para cerrar la ventana de login
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
}
