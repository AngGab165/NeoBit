package com.neobit.sugerencia.presentacion.principal;

import javafx.application.Application;
import javafx.application.Platform;
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

@Component
public class VentanaPrincipalAdministrador extends Application {

    @Autowired
    @Lazy
    private ControlPrincipalAdministrador controlPrincipalAdministrador;

    private Stage stage;
    private boolean initialized = false; // Para asegurarnos de no inicializar la ventana dos veces

    private Label lblBienvenido;

    private Usuario usuario;

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

        lblBienvenido = new Label();
        lblBienvenido.setStyle("-fx-font-size: 18px; -fx-font-weight: bold; -fx-text-fill: #006666;");
        actualizarBienvenida(); // Método para actualizar el mensaje

        // Opciones para el administrador
        Button btnGestionEmpleados = new Button("Gestionar Empleados");
        btnGestionEmpleados.setOnAction(e -> controlPrincipalAdministrador.abrirRegistroEmpleado());
        btnGestionEmpleados.setStyle(
                "-fx-background-color: #006666; -fx-text-fill: white; -fx-font-size: 14px; -fx-padding: 10px 20px;");

        Button btnGestionSugerencias = new Button("Revisar Sugerencias");
        btnGestionSugerencias.setOnAction(e -> controlPrincipalAdministrador.muestraVentanaRevisarSugerencias());
        btnGestionSugerencias.setStyle(
                "-fx-background-color: #006666; -fx-text-fill: white; -fx-font-size: 14px; -fx-padding: 10px 20px;");

        // Botón para gestionar notificaciones
        Button btnGestionNotificaciones = new Button("Gestionar Notificaciones");
        btnGestionNotificaciones
                .setOnAction(e -> controlPrincipalAdministrador.muestraVentanaNotificacionesAdministrador());
        btnGestionNotificaciones.setStyle(
                "-fx-background-color: #006666; -fx-text-fill: white; -fx-font-size: 14px; -fx-padding: 10px 20px;");

        // Layout
        VBox vbox = new VBox(20, lblBienvenido, btnGestionEmpleados, btnGestionSugerencias, btnGestionNotificaciones);
        vbox.setAlignment(Pos.CENTER);
        vbox.setPadding(new Insets(20));
        vbox.setStyle("-fx-background-color: #eaf4f4;");

        Scene scene = new Scene(vbox, 450, 250);
        stage.setScene(scene);

        initialized = true; // Marcar como inicializada
    }

    // Método para asignar usuario antes de mostrar la ventana
    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
        actualizarBienvenida();
    }

    // Método para actualizar el mensaje de bienvenida con el nombre solo si es un
    // empleado
    private void actualizarBienvenida() {
        if (lblBienvenido != null) {
            if (usuario != null && usuario.getRol() == Rol.ADMINISTRADOR) {
                lblBienvenido.setText("Bienvenido, " + usuario.getNombre());
            } else {
                lblBienvenido.setText("Bienvenido, Administardor");
            }
        }
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
