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
import com.neobit.sugerencia.negocio.modelo.Empleado;
import com.neobit.sugerencia.negocio.modelo.Notificaciones;
import com.neobit.sugerencia.negocio.modelo.Usuario;
import com.neobit.sugerencia.negocio.ServicioUsuario;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Component
public class VentanaNotificacionesAdministrador {

    private Stage stage;
    private TableView<Notificaciones> tableNotificaciones;
    private TableView<Empleado> tableEmpleados;
    private ObservableList<Empleado> empleadosData;

    @Autowired
    private ControlNotificaciones control;

    @Autowired
    private ServicioUsuario servicioUsuario;

    private boolean initialized = false;

    public VentanaNotificacionesAdministrador() {
        // Constructor vacío requerido por Spring
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

        Label lblTitulo = new Label("Gestión de Notificaciones");
        lblTitulo.setStyle("-fx-font-size: 24px; -fx-font-weight: bold; -fx-text-fill: #006666;");

        Label lblEmpleados = new Label("Selección de Empleados");
        lblEmpleados.setStyle("-fx-font-size: 1rem; -fx-text-fill: #006666;");

        TextField txtBuscarEmpleado = new TextField();
        txtBuscarEmpleado.setPromptText("Buscar por nombre o ID...");
        txtBuscarEmpleado.textProperty().addListener((obs, oldVal, newVal) -> filtrarEmpleados(newVal));

        Button btnCargarEmpleados = new Button("Cargar Empleados");
        btnCargarEmpleados.setStyle("-fx-background-color: #006666; -fx-text-fill: #ffffff;");
        btnCargarEmpleados.setOnAction(e -> {
            cargarEmpleados();
            if (!tableEmpleados.getItems().isEmpty()) {
                mostrarAlerta("Éxito", "Empleados cargados correctamente");
            }
        });

        HBox filtrosPanel = new HBox(10, new Label("Filtros:"), txtBuscarEmpleado, btnCargarEmpleados);
        filtrosPanel.setAlignment(Pos.CENTER_LEFT);

        tableEmpleados = new TableView<>();
        tableEmpleados.setPlaceholder(new Label("No hay empleados disponibles. Haz clic en 'Cargar Empleados'"));

        TableColumn<Empleado, Long> idEmpleadoColumn = new TableColumn<>("ID");
        idEmpleadoColumn.setCellValueFactory(new PropertyValueFactory<>("id"));

        TableColumn<Empleado, String> nombreEmpleadoColumn = new TableColumn<>("Nombre");
        nombreEmpleadoColumn.setCellValueFactory(new PropertyValueFactory<>("nombre"));

        TableColumn<Empleado, String> correoEmpleadoColumn = new TableColumn<>("Correo");
        correoEmpleadoColumn.setCellValueFactory(new PropertyValueFactory<>("correo"));

        tableEmpleados.getColumns().addAll(idEmpleadoColumn, nombreEmpleadoColumn, correoEmpleadoColumn);
        tableEmpleados.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);

        VBox empleadosPanel = new VBox(10, lblEmpleados, filtrosPanel, tableEmpleados);
        empleadosPanel.setPadding(new Insets(10));
        empleadosPanel.setStyle("-fx-background-color: #f0f8ff; -fx-border-color: #006666; -fx-border-width: 1px;");

        GridPane formPane = new GridPane();
        formPane.setPadding(new Insets(20));
        formPane.setHgap(10);
        formPane.setVgap(10);
        formPane.setStyle(
                "-fx-background-color: #ffffff; -fx-border-color: #006666; -fx-border-width: 2px; -fx-border-radius: 10px;");

        Label lblMensaje = new Label("Mensaje:");
        lblMensaje.setStyle("-fx-font-size: 1rem; -fx-text-fill: #006666;");
        TextArea txtMensaje = new TextArea();
        txtMensaje.setPrefRowCount(3);

        Button btnEnviar = new Button("Enviar Notificación");
        btnEnviar.setStyle("-fx-background-color: #006666; -fx-text-fill: #ffffff;");
        btnEnviar.setOnAction(e -> {
            String mensaje = txtMensaje.getText().trim();

            if (mensaje.isEmpty()) {
                mostrarAlerta("Error", "Por favor, escriba un mensaje.");
                return;
            }

            List<Long> idsSeleccionados = new ArrayList<>();
            for (Empleado emp : tableEmpleados.getSelectionModel().getSelectedItems()) {
                idsSeleccionados.add(emp.getId());
            }

            if (idsSeleccionados.isEmpty()) {
                mostrarAlerta("Error", "Seleccione al menos un empleado.");
                return;
            }

            LocalDateTime fecha = LocalDateTime.now(); // ✅ Fecha automática

            control.enviarNotificaciones(idsSeleccionados, mensaje, fecha);
            mostrarAlerta("Éxito", "Notificaciones enviadas correctamente.");
            actualizarTabla();
            txtMensaje.clear();
        });

        formPane.add(lblMensaje, 0, 0);
        formPane.add(txtMensaje, 1, 0);
        formPane.add(btnEnviar, 1, 1);

        tableNotificaciones = new TableView<>();
        tableNotificaciones.setStyle(
                "-fx-background-color: #ffffff; -fx-border-color: #006666; -fx-border-width: 2px; -fx-border-radius: 10px;");

        TableColumn<Notificaciones, Long> idColumn = new TableColumn<>("ID");
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));

        TableColumn<Notificaciones, String> mensajeColumn = new TableColumn<>("Mensaje");
        mensajeColumn.setCellValueFactory(new PropertyValueFactory<>("mensaje"));

        TableColumn<Notificaciones, String> fechaColumn = new TableColumn<>("Fecha");
        fechaColumn.setCellValueFactory(new PropertyValueFactory<>("fecha"));

        TableColumn<Notificaciones, Void> accionesColumn = new TableColumn<>("Acciones");
        accionesColumn.setCellFactory(param -> new TableCell<>() {
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

        VBox vboxMain = new VBox(20, formPane, empleadosPanel, tableNotificaciones);
        vboxMain.setPadding(new Insets(20));
        vboxMain.setAlignment(Pos.CENTER);

        Label footerText = new Label("©2025 Derechos Reservados - Sistema Sugerencias - NeoBit");
        footerText.setStyle("-fx-text-fill: white; -fx-font-size: 12px;");
        HBox footer = new HBox(footerText);
        footer.setAlignment(Pos.CENTER);
        footer.setPadding(new Insets(10));
        footer.setStyle("-fx-background-color: #006666;");

        BorderPane borderPane = new BorderPane();
        borderPane.setTop(new VBox(10, lblTitulo));
        borderPane.setCenter(vboxMain);
        borderPane.setBottom(footer);
        borderPane.setStyle("-fx-background-color: #eaf4f4;");

        Scene scene = new Scene(borderPane, 900, 700);
        stage.setScene(scene);
        initialized = true;
    }

    private void cargarEmpleados() {
        try {
            // Obtén la lista de usuarios (empleados) desde el servicio
            List<Usuario> usuarios = servicioUsuario.obtenerEmpleados(); // Aquí obtienes los usuarios
            if (usuarios.isEmpty()) {
                mostrarAlerta("Información", "No hay empleados registrados en el sistema.");
            } else {
                // Mapeamos los usuarios a la tabla de empleados
                empleadosData = FXCollections.observableArrayList(usuarios.stream()
                        .map(usuario -> {
                            Empleado empleado = new Empleado(usuario.getNombre(), usuario.getCorreo());
                            empleado.setId(usuario.getId()); // Aquí le pones el ID
                            return empleado;
                        }) // trabajar con
                           // 'Empleado'
                        .toList());
                tableEmpleados.setItems(empleadosData);
            }
        } catch (Exception e) {
            mostrarAlerta("Error", "No se pudieron cargar los empleados: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void filtrarEmpleados(String filtro) {
        if (empleadosData == null)
            return;

        if (filtro == null || filtro.isEmpty()) {
            tableEmpleados.setItems(empleadosData);
        } else {
            ObservableList<Empleado> empleadosFiltrados = FXCollections.observableArrayList();
            for (Empleado empleado : empleadosData) {
                boolean coincidePorNombre = empleado.getNombre().toLowerCase().contains(filtro.toLowerCase());
                boolean coincidePorId = String.valueOf(empleado.getId()).contains(filtro);
                if (coincidePorNombre || coincidePorId) {
                    empleadosFiltrados.add(empleado);
                }
            }
            tableEmpleados.setItems(empleadosFiltrados);
        }
    }

    public void muestra() {
        if (!Platform.isFxApplicationThread()) {
            Platform.runLater(this::muestra);
            return;
        }
        initializeUI();
        actualizarTabla();
        stage.show();
    }

    private void actualizarTabla() {
        List<Notificaciones> notificaciones = control.obtenerTodasLasNotificaciones();
        ObservableList<Notificaciones> data = FXCollections.observableArrayList(notificaciones);
        tableNotificaciones.setItems(data);
    }

    private void mostrarAlerta(String titulo, String mensaje) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }
}
