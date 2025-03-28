package com.neobit.sugerencia.presentacion.notificaciones;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.*;
import javafx.stage.Stage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;
import com.neobit.sugerencia.negocio.modelo.Notificaciones;
import com.neobit.sugerencia.presentacion.principal.ControlPrincipalAdministrador;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Date;
import java.util.List;

@Component
public class VentanaNotificacionesAdministrador {

    private Stage stage;
    private TableView<Notificaciones> tableNotificaciones;
    @Autowired
    private ControlNotificaciones control;
    @Autowired
    @Lazy
    private ControlPrincipalAdministrador controlPrincipalAdministrador;

    private boolean initialized = false;

    public VentanaNotificacionesAdministrador() {
        // Constructor vacío
    }

    private void initializeUI() {
        if (initialized)
            return;

        if (!Platform.isFxApplicationThread()) {
            Platform.runLater(this::initializeUI);
            return;
        }

        stage = new Stage();
        stage.setTitle("Gestión de Notificaciones (Administrador)");

        // Header
        Label lblTitulo = new Label("Gestión de Notificaciones");
        lblTitulo.setStyle("-fx-font-size: 24px; -fx-font-weight: bold; -fx-text-fill: #006666;");
        VBox header = new VBox(10, lblTitulo);
        header.setAlignment(Pos.CENTER);
        header.setPadding(new Insets(20));
        header.setStyle("-fx-background-color: #eaf4f4;");

        // Formulario para agregar notificación
        GridPane formPane = new GridPane();
        formPane.setPadding(new Insets(20));
        formPane.setHgap(10);
        formPane.setVgap(10);
        formPane.setStyle(
                "-fx-background-color: #ffffff; -fx-border-color: #006666; -fx-border-width: 2px; -fx-border-radius: 10px;");

        Label lblMensaje = new Label("Mensaje:");
        lblMensaje.setStyle("-fx-font-size: 1rem; -fx-text-fill: #006666;");
        TextField txtMensaje = new TextField();

        Label lblFecha = new Label("Fecha (yyyy-MM-dd):");
        lblFecha.setStyle("-fx-font-size: 1rem; -fx-text-fill: #006666;");
        TextField txtFecha = new TextField();

        Button btnAgregar = new Button("Agregar");
        btnAgregar.setStyle("-fx-background-color: #006666; -fx-text-fill: #ffffff;");
        btnAgregar.setOnAction(e -> {
            String mensaje = txtMensaje.getText();
            try {
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
                LocalDateTime fecha = LocalDateTime.parse(txtFecha.getText(), formatter);
                if (!mensaje.isEmpty()) {
                    control.agregaNotificacion(mensaje, fecha, null);
                    actualizarTabla();
                    txtMensaje.clear();
                    txtFecha.clear();
                }
            } catch (DateTimeParseException ex) {
                mostrarAlerta("Error", "Formato de fecha incorrecto. Usa el formato 'yyyy-MM-dd'.");
            }
        });

        formPane.add(lblMensaje, 0, 0);
        formPane.add(txtMensaje, 1, 0);
        formPane.add(lblFecha, 0, 1);
        formPane.add(txtFecha, 1, 1);
        formPane.add(btnAgregar, 1, 2);

        // Tabla de notificaciones
        tableNotificaciones = new TableView<>();
        tableNotificaciones.setStyle(
                "-fx-background-color: #ffffff; -fx-border-color: #006666; -fx-border-width: 2px; -fx-border-radius: 10px;");

        TableColumn<Notificaciones, Long> idColumn = new TableColumn<>("ID");
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));

        TableColumn<Notificaciones, String> mensajeColumn = new TableColumn<>("Mensaje");
        mensajeColumn.setCellValueFactory(new PropertyValueFactory<>("mensaje"));

        TableColumn<Notificaciones, Date> fechaColumn = new TableColumn<>("Fecha");
        fechaColumn.setCellValueFactory(new PropertyValueFactory<>("fecha"));

        TableColumn<Notificaciones, Void> accionesColumn = new TableColumn<>("Acciones");
        accionesColumn.setCellFactory(param -> new TableCell<Notificaciones, Void>() {
            private final Button btnEliminar = new Button("Eliminar");

            {
                btnEliminar.setStyle("-fx-background-color: #ff4d4d; -fx-text-fill: #ffffff;");
                btnEliminar.setOnAction(e -> {
                    Notificaciones notificacion = getTableView().getItems().get(getIndex());
                    control.eliminaNotificacion(notificacion);
                    actualizarTabla();
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                setGraphic(empty ? null : btnEliminar);
            }
        });

        tableNotificaciones.getColumns().addAll(idColumn, mensajeColumn, fechaColumn, accionesColumn);

        // Layout
        VBox vboxMain = new VBox(20, formPane, tableNotificaciones);
        vboxMain.setPadding(new Insets(20));
        vboxMain.setAlignment(Pos.CENTER);

        BorderPane borderPane = new BorderPane();
        borderPane.setTop(header);
        borderPane.setCenter(vboxMain);
        borderPane.setStyle("-fx-background-color: #eaf4f4;");

        Scene scene = new Scene(borderPane, 800, 600);
        stage.setScene(scene);
        stage.show();

        initialized = true;
    }

    // ✅ Método para mostrar la ventana con el ControlNotificaciones
    public void muestra(ControlPrincipalAdministrador controlPrincipalAdministrador) {
        if (!Platform.isFxApplicationThread()) {
            Platform.runLater(() -> muestra(controlPrincipalAdministrador));
            return;
        }

        // Asignar el control de notificaciones
        this.control = controlPrincipalAdministrador.getControlNotificaciones();
        initializeUI();
        actualizarTabla();
        stage.show();
    }

    // Método para actualizar la tabla de notificaciones
    private void actualizarTabla() {
        List<Notificaciones> notificaciones = control.obtenerTodasLasNotificaciones();
        ObservableList<Notificaciones> data = FXCollections.observableArrayList(notificaciones);
        tableNotificaciones.setItems(data);
    }

    // Método para mostrar alertas
    private void mostrarAlerta(String titulo, String mensaje) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }
}