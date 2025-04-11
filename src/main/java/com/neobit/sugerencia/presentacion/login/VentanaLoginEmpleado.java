package com.neobit.sugerencia.presentacion.login;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import com.neobit.sugerencia.negocio.ServicioUsuario;
import com.neobit.sugerencia.negocio.modelo.Usuario;
import com.neobit.sugerencia.presentacion.principal.VentanaPrincipalEmpleado;
import com.neobit.sugerencia.util.CorreoUtil;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Modality;
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

        Label lblTitulo = new Label("Iniciar Sesión");
        lblTitulo.setFont(Font.font("Arial", FontWeight.BOLD, 20));
        lblTitulo.setStyle("-fx-text-fill: #006666;");
        // Inicializar los componentes
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

        // Acción al hacer clic en el botón de login
        btnLogin.setOnAction(e -> loginEmpleado(tfUsuario.getText(), pfClave.getText()));

        // Acción al hacer clic en el botón de recuperar contraseña
        btnRecuperarContrasena.setOnAction(e -> mostrarVentanaEmergenteRecuperarContrasena());

        // Acción al hacer clic en el botón de registrarse
        btnRegistrarse.setOnAction(e -> mostrarFormularioRegistro());

        // Diseño de la ventana con VBox
        VBox vbox = new VBox(15, lblTitulo, tfUsuario, pfClave, btnLogin, btnRecuperarContrasena, btnRegistrarse);
        vbox.setAlignment(Pos.CENTER);
        vbox.setStyle("-fx-background-color: #eaf4f4;");

        // Crear la escena y asignar la vista
        Scene scene = new Scene(vbox, 400, 350);
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
            ventanaPrincipalEmpleado.setUsuario(empleado);
            // Aquí podrías abrir la ventana principal del empleado
            ventanaPrincipalEmpleado.mostrar();
        } else {
            mostrarMensajeError("Usuario o contraseña incorrectos.");
        }
    }

    public void recuperarContrasena(String usuario) {
        if (usuario.isEmpty()) {
            mostrarMensajeError("Por favor ingresa tu usuario.");
            return;
        }

        Usuario empleado = servicioUsuario.buscarUsuarioPorNombre(usuario);

        if (empleado != null) {
            String correo = empleado.getCorreo(); // Asegúrate de que tu clase Usuario tenga getCorreo()
            String nuevaClave = generarClaveTemporal();

            // Cambia la clave del usuario en la base de datos
            empleado.setContrasena(nuevaClave);
            servicioUsuario.actualizarUsuario(empleado); // Asegúrate de tener este método

            String mensaje = "Tu nueva contraseña temporal es: " + nuevaClave
                    + "\nPor favor cámbiala al iniciar sesión.";
            CorreoUtil.enviarCorreo(correo, "Recuperación de contraseña", mensaje);

            mostrarMensajeExito("Se ha enviado una contraseña temporal a tu correo.");
        } else {
            mostrarMensajeError("El usuario no existe.");
        }
    }

    private String generarClaveTemporal() {
        return "temp" + (int) (Math.random() * 10000);
    }

    private void mostrarVentanaEmergenteRecuperarContrasena() {
        Stage ventana = new Stage();
        ventana.initModality(Modality.APPLICATION_MODAL);
        ventana.setTitle("Recuperar Contraseña");

        Label lblTitulo = new Label("Recuperación de Contraseña");
        lblTitulo.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");

        Label lblUsuario = new Label("Usuario:");
        TextField txtUsuario = new TextField();
        txtUsuario.setPromptText("Ingrese su nombre de usuario");

        Label lblMensaje = new Label();
        lblMensaje.setStyle("-fx-text-fill: red;");

        Button btnEnviar = new Button("Enviar contraseña temporal");
        btnEnviar.setStyle("-fx-background-color: #006666; -fx-text-fill: white;");

        btnEnviar.setOnAction(e -> {
            String usuario = txtUsuario.getText().trim();
            if (usuario.isEmpty()) {
                lblMensaje.setText("⚠️ Por favor, ingresa tu usuario.");
                return;
            }

            Usuario u = servicioUsuario.buscarUsuarioPorNombre(usuario);
            if (u != null) {
                String tempPass = generarClaveTemporal();
                u.setContrasena(tempPass);
                servicioUsuario.actualizarUsuario(u);

                String cuerpo = "Hola " + u.getNombre() + ",\n\nTu contraseña temporal es: " + tempPass +
                        "\n\nPor favor cámbiala al iniciar sesión.";
                CorreoUtil.enviarCorreo(u.getCorreo(), "Recuperación de contraseña", cuerpo);

                lblMensaje.setStyle("-fx-text-fill: green;");
                lblMensaje.setText("✅ Se envió la contraseña temporal al correo.");
            } else {
                lblMensaje.setText("❌ Usuario no encontrado.");
            }
        });

        VBox layout = new VBox(10, lblTitulo, lblUsuario, txtUsuario, btnEnviar, lblMensaje);
        layout.setPadding(new Insets(20));
        layout.setAlignment(Pos.CENTER);
        layout.setStyle("-fx-background-color: #f9f9f9;");

        ventana.setScene(new Scene(layout, 400, 250));
        ventana.showAndWait();
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

    private void estilizarCampo(TextField campo) {
        campo.setStyle("-fx-border-color: #006666; -fx-background-color: #ffffff; -fx-text-fill: #006666;");
    }

    private void estilizarBoton(Button boton, String color) {
        boton.setStyle("-fx-background-color: " + color
                + "; -fx-text-fill: #ffffff; -fx-padding: 7px 15px; -fx-border-radius: 5px;");
    }
}
