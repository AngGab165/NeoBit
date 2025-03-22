package com.neobit.sugerencia.presentacion.login;

import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.neobit.sugerencia.negocio.ServicioUsuario;
import com.neobit.sugerencia.negocio.modelo.Rol;
import com.neobit.sugerencia.negocio.modelo.Usuario;

@Component
public class VentanaRegistroEmpleado {

    @Autowired
    private ServicioUsuario servicioUsuario; // Servicio para interactuar con la BD

    private Stage stage;

    public void mostrar() {
        Platform.runLater(() -> {
            if (stage == null || !stage.isShowing()) {
                stage = new Stage();
                stage.setTitle("Registro de Nuevo Empleado");

                // Campos de registro
                TextField txtNombre = new TextField();
                txtNombre.setPromptText("Nombre");

                TextField txtCorreo = new TextField();
                txtCorreo.setPromptText("Correo");

                TextField txtUsuario = new TextField();
                txtUsuario.setPromptText("Usuario");

                PasswordField txtContrasena = new PasswordField();
                txtContrasena.setPromptText("Contraseña");

                Button btnRegistrar = new Button("Registrar");
                btnRegistrar.setStyle("-fx-background-color: #33cc33; -fx-text-fill: white;");
                btnRegistrar.setOnAction(e -> {
                    registrarEmpleado(txtNombre.getText(), txtCorreo.getText(), txtUsuario.getText(),
                            txtContrasena.getText());
                });

                VBox vbox = new VBox(15, txtNombre, txtCorreo, txtUsuario, txtContrasena, btnRegistrar);
                vbox.setAlignment(Pos.CENTER);
                vbox.setPadding(new Insets(20));
                vbox.setStyle("-fx-background-color: #eaf4f4;");

                Scene scene = new Scene(vbox, 350, 400);
                stage.setScene(scene);
                stage.show();
            } else {
                stage.toFront();
            }
        });
    }

    public void registrarEmpleado(String nombre, String correo, String usuario, String contrasena) {
        if (nombre.isEmpty() || correo.isEmpty() || usuario.isEmpty() || contrasena.isEmpty()) {
            mostrarAlerta("Error", "Todos los campos son obligatorios.");
            return;
        }

        if (servicioUsuario.existeCorreo(correo)) {
            mostrarAlerta("Error", "El correo ya está registrado.");
            return;
        }

        Usuario nuevoEmpleado = new Usuario();
        nuevoEmpleado.setNombre(nombre);
        nuevoEmpleado.setCorreo(correo);
        nuevoEmpleado.setUsuario(usuario);
        nuevoEmpleado.setClave(contrasena);
        nuevoEmpleado.setRol(Rol.EMPLEADO);

        servicioUsuario.registrarUsuario(nuevoEmpleado);
        mostrarAlerta("Éxito", "Empleado registrado correctamente.");
        stage.close();
    }

    public void mostrarAlerta(String titulo, String mensaje) {
        Alert alerta = new Alert(Alert.AlertType.INFORMATION);
        alerta.setTitle(titulo);
        alerta.setHeaderText(null);
        alerta.setContentText(mensaje);
        alerta.showAndWait();
    }

    public void start(Stage stage2) {
        this.stage = stage2;
        mostrar();
    }
}
