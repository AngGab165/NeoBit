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
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import com.neobit.sugerencia.negocio.modelo.Rol;
import com.neobit.sugerencia.negocio.modelo.Usuario;
import com.neobit.sugerencia.presentacion.notificaciones.VentanaNotificacionesEmpleado;
import com.neobit.sugerencia.presentacion.sugerencia.VentanaSugerencias;

@Component
public class VentanaPrincipalEmpleado extends Application {

    private ControlPrincipalEmpleado control;

    @Autowired
    @Lazy
    private VentanaSugerencias ventanaSugerencias;

    @Autowired
    @Lazy
    private VentanaNotificacionesEmpleado ventanaNotificaciones;

    private Label lblBienvenido;

    private Usuario usuario;

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Ventana de Empleado");

        lblBienvenido = new Label();
        lblBienvenido.setStyle("-fx-font-size: 18px; -fx-font-weight: bold; -fx-text-fill: #006666;");
        actualizarBienvenida(); // Método para actualizar el mensaje

        // Opciones para el empleado
        Button btnGestionSugerencias = new Button("Gestionar Sugerencias");
        estilizarBoton(btnGestionSugerencias, "#006666");
        btnGestionSugerencias.setOnAction(e -> ventanaSugerencias.mostrar());

        Button btnGestionNotificaciones = new Button("Gestionar Notificaciones");
        estilizarBoton(btnGestionNotificaciones, "#006666");
        btnGestionNotificaciones.setOnAction(e -> ventanaNotificaciones.muestra());

        VBox vbox = new VBox(20, lblBienvenido, btnGestionSugerencias, btnGestionNotificaciones);
        vbox.setAlignment(Pos.CENTER);
        vbox.setPadding(new Insets(20));
        vbox.setStyle("-fx-background-color: #eaf4f4;");

        Scene scene = new Scene(vbox, 600, 350);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    // Método para asignar usuario antes de mostrar la ventana
    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
        actualizarBienvenida();
    }

    // Método para actualizar el mensaje de bienvenida con el nombre solo si es un
    // administrador
    private void actualizarBienvenida() {
        if (lblBienvenido != null) {
            if (usuario != null) {
                lblBienvenido.setText("Bienvenido, " + usuario.getNombre());
            } else {
                lblBienvenido.setText("Bienvenido");
            }
        }
    }

    // Método para inyectar el controlador
    public void setControl(ControlPrincipalEmpleado control) {
        this.control = control;
    }

    public static void main(String[] args) {
        launch(args);
    }

    public void muestra(Stage primaryStage) {
        start(primaryStage);
    }

    public void mostrar() {
        Stage stage = new Stage();
        start(stage);
    }

    // Estilización de los botones
    private void estilizarBoton(Button boton, String color) {
        boton.setStyle("-fx-background-color: " + color
                + "; -fx-text-fill: #ffffff; -fx-padding: 7px 15px; -fx-border-radius: 5px;");
    }
}