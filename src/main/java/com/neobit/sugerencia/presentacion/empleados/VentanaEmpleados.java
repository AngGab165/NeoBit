package com.neobit.sugerencia.presentacion.empleados;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import org.springframework.stereotype.Component;

import com.neobit.sugerencia.negocio.modelo.Empleado;

import java.util.List;

/**
 * Ventana de gestión de empleados
 */
@Component
public class VentanaEmpleados {

    private Stage stage; // Mantenemos la referencia a la ventana
    private TableView<Empleado> tableEmpleados;
    private ControlEmpleados control;
    private boolean initialized = false;

    /**
     * Constructor sin inicialización de la UI
     */
    public VentanaEmpleados() {
        // No inicializamos los componentes de JavaFX aquí
    }

    /**
     * Inicializa los componentes de la UI en el hilo de JavaFX
     */
    private void initializeUI() {
        // Asegura que la UI se cree solo si estamos en el hilo de JavaFX
        if (!Platform.isFxApplicationThread()) {
            Platform.runLater(this::initializeUI);
            return;
        }

        // Si la ventana ya ha sido inicializada, no hacemos nada
        if (initialized) {
            return;
        }

        // Crear un nuevo Stage (ventana) cada vez que se muestra
        stage = new Stage();
        stage.setTitle("Gestión de Empleados");

        // Cabecera
        Label lblTitulo = new Label("Gestión de Empleados");
        lblTitulo.setStyle("-fx-font-size: 24px; -fx-font-weight: bold;");
        VBox header = new VBox(10, lblTitulo);
        header.setAlignment(Pos.CENTER);
        header.setPadding(new Insets(20));

        // Formulario para agregar empleado
        GridPane formPane = new GridPane();
        formPane.setPadding(new Insets(20));
        formPane.setHgap(10);
        formPane.setVgap(10);

        Label lblNombre = new Label("Nombre:");
        TextField txtNombre = new TextField();
        Label lblCorreo = new Label("Correo Electrónico:");
        TextField txtCorreo = new TextField();
        Button btnAgregar = new Button("Agregar");
        btnAgregar.setOnAction(e -> {
            String nombre = txtNombre.getText();
            String correo = txtCorreo.getText();
            if (!nombre.isEmpty() && !correo.isEmpty()) {
                control.agregaEmpleado(nombre, correo);
                txtNombre.clear();
                txtCorreo.clear();
            }
        });

        formPane.add(lblNombre, 0, 0);
        formPane.add(txtNombre, 1, 0);
        formPane.add(lblCorreo, 0, 1);
        formPane.add(txtCorreo, 1, 1);
        formPane.add(btnAgregar, 1, 2);

        // Tabla de empleados
        tableEmpleados = new TableView<>();
        TableColumn<Empleado, Long> idColumn = new TableColumn<>("ID");
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));

        TableColumn<Empleado, String> nombreColumn = new TableColumn<>("Nombre");
        nombreColumn.setCellValueFactory(new PropertyValueFactory<>("nombre"));

        TableColumn<Empleado, String> correoColumn = new TableColumn<>("Correo");
        correoColumn.setCellValueFactory(new PropertyValueFactory<>("correo"));

        TableColumn<Empleado, Void> accionesColumn = new TableColumn<>("Acciones");
        accionesColumn.setCellFactory(param -> new TableCell<Empleado, Void>() {
            private final Button btnEliminar = new Button("Eliminar");

            {
                btnEliminar.setOnAction(e -> {
                    Empleado empleado = getTableView().getItems().get(getIndex());
                    control.eliminaEmpleado(empleado);
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    setGraphic(btnEliminar);
                }
            }
        });

        tableEmpleados.getColumns().addAll(idColumn, nombreColumn, correoColumn, accionesColumn);

        // Layout
        VBox vboxMain = new VBox(20, formPane, tableEmpleados);
        vboxMain.setPadding(new Insets(20));
        vboxMain.setAlignment(Pos.CENTER);

        BorderPane borderPane = new BorderPane();
        borderPane.setTop(header);
        borderPane.setCenter(vboxMain);

        Scene scene = new Scene(borderPane, 800, 600);

        // Evita error si no tienes un archivo CSS
        if (getClass().getResource("/css/styles.css") != null) {
            scene.getStylesheets().add(getClass().getResource("/css/styles.css").toExternalForm());
        }

        stage.setScene(scene);
        stage.show();

        initialized = true;
    }

    /**
     * Muestra la ventana y carga los empleados
     * 
     * @param control   El controlador asociado
     * @param empleados La lista de empleados a mostrar
     */
    public void muestra(ControlEmpleados control, List<Empleado> empleados) {
        if (control == null) {
            throw new IllegalArgumentException("El controlador no puede ser null");
        }

        this.control = control; // Asegúrate de que control esté correctamente asignado

        if (!Platform.isFxApplicationThread()) {
            Platform.runLater(() -> this.muestra(control, empleados));
            return;
        }

        // Inicializa la UI si es necesario
        initializeUI();

        // Cargar empleados
        ObservableList<Empleado> data = FXCollections.observableArrayList(empleados);
        tableEmpleados.setItems(data);

        stage.show();
    }

    public void muestra() {
        // Inicializar la ventana y obtener la lista de empleados si no se pasa una
        // lista
        if (!Platform.isFxApplicationThread()) {
            Platform.runLater(this::muestra);
            return;
        }

        // Llamada a initializeUI() para asegurarse de que los componentes de la
        // interfaz estén inicializados
        initializeUI();

        // Aquí puedes agregar lógica para obtener los empleados desde un servicio o
        // repositorio
        List<Empleado> empleados = control.obtenerEmpleados(); // Asumiendo que el controlador tiene este método

        // Cargar los empleados en la tabla
        ObservableList<Empleado> data = FXCollections.observableArrayList(empleados);
        tableEmpleados.setItems(data);

        stage.show();
    }
}
