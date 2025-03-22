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
import org.springframework.stereotype.Component;
import com.neobit.sugerencia.negocio.modelo.Notificaciones;
import com.neobit.sugerencia.presentacion.principal.ControlPrincipalEmpleado;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Component
public class VentanaNotificaciones {

    private Stage stage;
    private TableView<Notificaciones> tableNotificaciones;
    @Autowired
    private ControlNotificaciones control;
    private boolean initialized = false;

    public VentanaNotificaciones() {
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
        stage.setTitle("Gestión de Notificaciones");

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
                SimpleDateFormat formato = new SimpleDateFormat("yyyy-MM-dd");
                Date fecha = formato.parse(txtFecha.getText());
                if (!mensaje.isEmpty()) {
                    control.agregaNotificacion(mensaje, fecha);
                    actualizarTabla(); // Actualizar tabla automáticamente
                    txtMensaje.clear();
                    txtFecha.clear();
                }
            } catch (ParseException ex) {
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
                    actualizarTabla(); // Actualizar tabla tras eliminar
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

    /**
     * Muestra la ventana y carga las notificaciones
     * 
     * @param control        El controlador asociado
     * @param notificaciones La lista de notificaciones a mostrar
     */
    public void muestra(ControlNotificaciones control, List<Notificaciones> notificaciones) {
        this.control = control;

        if (!Platform.isFxApplicationThread()) {
            Platform.runLater(() -> this.muestra(control, notificaciones));
            return;
        }

        initializeUI();

        ObservableList<Notificaciones> data = FXCollections.observableArrayList(notificaciones);
        tableNotificaciones.setItems(data);

        stage.show();
    }

    // Método para actualizar la tabla
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

    // Implementación del método muestra(ControlPrincipalEmpleado)
    public void muestra(ControlPrincipalEmpleado controlPrincipalEmpleado) {
        // Implementación para mostrar la ventana de notificaciones en el contexto de la
        // ventana principal del empleado
        if (!Platform.isFxApplicationThread()) {
            Platform.runLater(() -> muestra(controlPrincipalEmpleado));
            return;
        }

        // Llamamos al controlador de la ventana principal para cargar las
        // notificaciones
        List<Notificaciones> notificaciones = controlPrincipalEmpleado.obtenerNotificaciones();
        this.muestra(controlPrincipalEmpleado.getControlNotificaciones(), notificaciones);
    }

    // Implementación del método muestra() sin argumentos
    public void muestra() {
        // Este método puede ser usado para mostrar la ventana sin pasar parámetros
        if (!Platform.isFxApplicationThread()) {
            Platform.runLater(this::muestra);
            return;
        }

        // Se puede mostrar la ventana sin notificaciones inicialmente
        List<Notificaciones> notificaciones = control.obtenerTodasLasNotificaciones();
        this.muestra(control, notificaciones);
    }
}