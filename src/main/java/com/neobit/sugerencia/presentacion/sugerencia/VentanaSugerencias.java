package com.neobit.sugerencia.presentacion.sugerencia;

// Importaciones necesarias
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import com.neobit.sugerencia.negocio.modelo.Prioridad;
import com.neobit.sugerencia.negocio.modelo.Sugerencia;
import com.neobit.sugerencia.presentacion.detallesSugerencia.ControlVerDetallesSugerencia;
import com.neobit.sugerencia.presentacion.detallesSugerencia.VentanaVerDetallesSugerencia;

import java.time.LocalDate;
import java.util.List;

@Component
public class VentanaSugerencias {

    private Stage stage;
    private TableView<Sugerencia> tableSugerencias;
    private Label contadorSugerenciasLabel; // Etiqueta para mostrar el contador
    @Autowired
    @Lazy
    private ControlSugerencias control;
    @Autowired
    @Lazy
    private VentanaVerDetallesSugerencia ventanaVerDetalles;
    private boolean initialized = false;

    @Autowired
    @Lazy
    private ControlVerDetallesSugerencia controlVerDetalles;

    private String nombreEmpleado;

    /**
     * Constructor sin inicializar UI
     */
    public VentanaSugerencias() {
        // No inicializar los componentes de JavaFX en el constructor
    }

    /**
     * Inicializa los componentes UI directamente
     */
    @SuppressWarnings("unchecked")
    public void initializeUI() {
        if (initialized) {
            return;
        }

        // Inicialización de la ventana principal
        stage = new Stage();
        stage.setTitle("Gestión de Sugerencias");

        // Header
        Label lblTitulo = new Label("Gestión de Sugerencias");
        lblTitulo.setStyle("-fx-font-size: 24px; -fx-font-weight: bold; -fx-text-fill: #006666;");
        VBox header = new VBox(10, lblTitulo);
        header.setAlignment(Pos.CENTER);
        header.setPadding(new Insets(20));
        header.setStyle("-fx-background-color: #F0F0F0;");

        // Contador de sugerencias
        contadorSugerenciasLabel = new Label("Total de sugerencias enviadas: 0");
        contadorSugerenciasLabel.setStyle("-fx-font-size: 16px; -fx-text-fill: #333333;");

        // Formulario para agregar sugerencia
        GridPane formPane = new GridPane();
        formPane.setPadding(new Insets(20));
        formPane.setHgap(10);
        formPane.setVgap(10);

        Label lblTituloSugerencia = new Label("Título:");
        lblTituloSugerencia.setStyle("-fx-font-size: 14px; -fx-text-fill: #006666;");
        TextField txtTitulo = new TextField();
        txtTitulo.setStyle("-fx-background-color: #FFFFFF; -fx-border-color: #006666;");

        Label lblDescripcion = new Label("Descripción Breve:");
        lblDescripcion.setStyle("-fx-font-size: 14px; -fx-text-fill: #006666;");
        TextField txtDescripcion = new TextField();
        txtDescripcion.setStyle("-fx-background-color: #FFFFFF; -fx-border-color: #006666;");

        Label lblAutor = new Label("Autor:");
        lblAutor.setStyle("-fx-font-size: 14px; -fx-text-fill: #006666;");
        TextField txtAutor = new TextField();
        txtAutor.setStyle("-fx-background-color: #FFFFFF; -fx-border-color: #006666;");

        Label lblPrioridad = new Label("Prioridad:");
        lblPrioridad.setStyle("-fx-font-size: 14px; -fx-text-fill: #006666;");
        ComboBox<Prioridad> comboPrioridad = new ComboBox<>();
        comboPrioridad.getItems().addAll(Prioridad.values());
        comboPrioridad.setPromptText("Selecciona una prioridad");
        comboPrioridad.setValue(Prioridad.BAJA); // Valor por defecto
        comboPrioridad.setStyle("-fx-background-color: #FFFFFF; -fx-border-color: #006666;");

        Button btnAgregar = new Button("Agregar Sugerencia");
        btnAgregar.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white;");
        btnAgregar.setOnAction(e -> {
            String titulo = txtTitulo.getText();
            String descripcion = txtDescripcion.getText();
            String autor = txtAutor.getText();
            Prioridad prioridad = comboPrioridad.getValue();
            LocalDate fechaCreacion = LocalDate.now();
            LocalDate ultimaActualizacion = LocalDate.now();
            control.agregaSugerencia(titulo, descripcion, autor, ultimaActualizacion, fechaCreacion, prioridad);
            actualizarTabla();
        });

        formPane.add(lblTituloSugerencia, 0, 0);
        formPane.add(txtTitulo, 1, 0);
        formPane.add(lblDescripcion, 0, 1);
        formPane.add(txtDescripcion, 1, 1);
        formPane.add(lblAutor, 0, 2);
        formPane.add(txtAutor, 1, 2);
        formPane.add(lblPrioridad, 0, 3);
        formPane.add(comboPrioridad, 1, 3);
        formPane.add(btnAgregar, 1, 4);

        // Tabla de sugerencias
        tableSugerencias = new TableView<>();
        tableSugerencias.setStyle("-fx-border-color: #006666;");
        TableColumn<Sugerencia, Long> idColumn = new TableColumn<>("ID");
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));

        TableColumn<Sugerencia, String> tituloColumn = new TableColumn<>("Título");
        tituloColumn.setCellValueFactory(new PropertyValueFactory<>("titulo"));

        TableColumn<Sugerencia, String> descripcionColumn = new TableColumn<>("Descripción Breve");
        descripcionColumn.setCellValueFactory(new PropertyValueFactory<>("descripcionBreve"));

        TableColumn<Sugerencia, String> autorColumn = new TableColumn<>("Autor");
        autorColumn.setCellValueFactory(new PropertyValueFactory<>("autor"));

        TableColumn<Sugerencia, LocalDate> fechaCreacionColumn = new TableColumn<>("Fecha Creación");
        fechaCreacionColumn.setCellValueFactory(new PropertyValueFactory<>("fechaCreacion"));

        TableColumn<Sugerencia, LocalDate> ultimaActualizacionColumn = new TableColumn<>("Última Actualización");
        ultimaActualizacionColumn.setCellValueFactory(new PropertyValueFactory<>("ultimaActualizacion"));

        TableColumn<Sugerencia, String> estadoColumn = new TableColumn<>("Estado");
        estadoColumn.setCellValueFactory(new PropertyValueFactory<>("estado"));

        TableColumn<Sugerencia, Prioridad> prioridadColumn = new TableColumn<>("Prioridad");
        prioridadColumn.setCellValueFactory(new PropertyValueFactory<>("prioridad"));
        tableSugerencias.getColumns().add(prioridadColumn);

        TableColumn<Sugerencia, Void> accionesColumn = new TableColumn<>("Acciones");
        accionesColumn.setCellFactory(param -> new TableCell<Sugerencia, Void>() {
            private final Button btnVerDetalles = new Button("Ver Detalles");
            private final Button btnEliminar = new Button("Eliminar");
            {
                btnVerDetalles.setStyle("-fx-background-color: #2196F3; -fx-text-fill: white;");
                btnVerDetalles.setOnAction(e -> {
                    Sugerencia sugerencia = getTableView().getItems().get(getIndex());
                    System.out.println(
                            "Antes de abrir detalles - NombreEmpleado en VentanaSugerencias: " + nombreEmpleado);
                    controlVerDetalles.setNombreEmpleado(nombreEmpleado); // Pasar el nombre del empleado
                    controlVerDetalles.inicia(sugerencia); // Abrir ventana de detalles

                });
                btnEliminar.setStyle("-fx-background-color: #F44336; -fx-text-fill: white;");
                btnEliminar.setOnAction(e -> {
                    Sugerencia sugerencia = getTableView().getItems().get(getIndex());
                    control.eliminaSugerencia(sugerencia);
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    VBox vbox = new VBox(5, btnVerDetalles, btnEliminar);
                    setGraphic(vbox);
                }
            }
        });

        tableSugerencias.getColumns().addAll(idColumn, tituloColumn, descripcionColumn, autorColumn,
                fechaCreacionColumn, ultimaActualizacionColumn, estadoColumn, accionesColumn);

        // Layout
        VBox vboxMain = new VBox(20, contadorSugerenciasLabel, formPane, tableSugerencias);
        vboxMain.setPadding(new Insets(20));
        vboxMain.setAlignment(Pos.CENTER);
        vboxMain.setStyle("-fx-background-color: #eaf4f4;");

        // Footer con derechos reservados
        Text footerText = new Text("©2025 Derechos Reservados - Sistema Sugerencias - NeoBit");
        footerText.setStyle("-fx-fill: white; -fx-font-size: 12px;");
        HBox footer = new HBox(footerText);
        footer.setAlignment(Pos.CENTER);
        footer.setPadding(new Insets(10));
        footer.setStyle("-fx-background-color: #006666;");

        BorderPane borderPane = new BorderPane();
        borderPane.setTop(header);
        borderPane.setCenter(vboxMain);
        borderPane.setBottom(footer);

        Scene scene = new Scene(borderPane, 800, 600);
        stage.setScene(scene);
        stage.show();

        initialized = true;
    }

    /**
     * Muestra la ventana y carga las sugerencias
     * 
     * @param control     El controlador asociado
     * @param sugerencias La lista de sugerencias a mostrar
     */
    public void muestra(ControlSugerencias control, List<Sugerencia> sugerencias) {
        this.control = control;

        // Llamada directa a initializeUI
        initializeUI();

        ObservableList<Sugerencia> data = FXCollections.observableArrayList(sugerencias);
        tableSugerencias.setItems(data);

        // Actualizar el contador
        contadorSugerenciasLabel.setText("Total de sugerencias: " + sugerencias.size());

        stage.show();
    }

    public void actualizarTabla() {
        if (control != null) {
            List<Sugerencia> sugerencias = control.obtenerTodasLasSugerencias();
            ObservableList<Sugerencia> data = FXCollections.observableArrayList(sugerencias);
            tableSugerencias.setItems(data);

            // Actualizar el contador
            contadorSugerenciasLabel.setText("Total de sugerencias: " + sugerencias.size());
        }
    }

    public void muestra() {
        // Llamada directa a initializeUI
        initializeUI();

        // Cargar sugerencias desde el controlador
        if (control != null) {
            List<Sugerencia> sugerencias = control.obtenerTodasLasSugerencias();
            ObservableList<Sugerencia> data = FXCollections.observableArrayList(sugerencias);
            tableSugerencias.setItems(data);

            // Actualizar el contador
            contadorSugerenciasLabel.setText("Total de sugerencias: " + sugerencias.size());
        }

        stage.show();
    }

    public void mostrar() {
        muestra();

    }

    public void setNombreEmpleado(String nombreEmpleado) {
        this.nombreEmpleado = nombreEmpleado;
    }

    public void actualizarContador(int total) {
        contadorSugerenciasLabel.setText("Total de sugerencias: " + total);
    }

}
