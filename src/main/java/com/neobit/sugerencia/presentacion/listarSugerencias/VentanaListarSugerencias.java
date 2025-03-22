package com.neobit.sugerencia.presentacion.listarSugerencias;

import java.util.List;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import org.springframework.stereotype.Component;

import com.neobit.sugerencia.negocio.modelo.Sugerencia;

/**
 * Ventana para listar sugerencias usando JavaFX
 */
@Component
public class VentanaListarSugerencias {

    private Stage stage;
    private TableView<Sugerencia> tableSugerencias;
    private ControlListarSugerencias control;
    private boolean initialized = false;

    /**
     * Constructor without UI initialization
     */
    public VentanaListarSugerencias() {
        // Don't initialize JavaFX components in constructor
    }

    /**
     * Initialize UI components on the JavaFX application thread
     */
    private void initializeUI() {
        if (initialized) {
            return;
        }

        // Create UI only if we're on JavaFX thread
        if (!Platform.isFxApplicationThread()) {
            Platform.runLater(this::initializeUI);
            return;
        }

        stage = new Stage();
        stage.setTitle("Sistema de sugerencias");

        // Create UI components
        Label lblTitulo = new Label("Sistema de sugerencias");
        lblTitulo.setStyle("-fx-font-size: 24px; -fx-font-weight: bold;");

        // Create menu bar
        MenuBar menuBar = new MenuBar();
        Menu menuArchivo = new Menu("Archivo");
        MenuItem menuItemCerrar = new MenuItem("Cerrar");
        menuItemCerrar.setOnAction(e -> stage.close());
        menuArchivo.getItems().add(menuItemCerrar);
        menuBar.getMenus().add(menuArchivo);

        // Create table
        tableSugerencias = new TableView<>();

        // Configure columns
        TableColumn<Sugerencia, Long> idColumn = new TableColumn<>("ID");
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));

        TableColumn<Sugerencia, String> tituloColumn = new TableColumn<>("Título");
        tituloColumn.setCellValueFactory(new PropertyValueFactory<>("titulo"));

        TableColumn<Sugerencia, String> descripcionColumn = new TableColumn<>("Descripción Breve");
        descripcionColumn.setCellValueFactory(new PropertyValueFactory<>("descripcionBreve"));

        TableColumn<Sugerencia, String> autorColumn = new TableColumn<>("Autor");
        autorColumn.setCellValueFactory(new PropertyValueFactory<>("autor"));

        TableColumn<Sugerencia, String> estadoColumn = new TableColumn<>("Estado");
        estadoColumn.setCellValueFactory(new PropertyValueFactory<>("estado"));

        tableSugerencias.getColumns().addAll(idColumn, tituloColumn, descripcionColumn, autorColumn, estadoColumn);

        Button btnVerDetalles = new Button("Ver Detalles");
        btnVerDetalles.setOnAction(e -> {
            Sugerencia sugerenciaSeleccionada = tableSugerencias.getSelectionModel().getSelectedItem();
            if (sugerenciaSeleccionada != null) {
                control.muestraDetallesSugerencia(sugerenciaSeleccionada);
            }
        });

        Button btnCerrar = new Button("Cerrar");
        btnCerrar.setOnAction(e -> stage.close());

        // Layout
        VBox vboxTop = new VBox(10);
        vboxTop.setPadding(new Insets(10));
        vboxTop.getChildren().addAll(lblTitulo, menuBar);

        HBox hboxBottom = new HBox(10);
        hboxBottom.setPadding(new Insets(10));
        hboxBottom.getChildren().addAll(btnVerDetalles, btnCerrar);

        BorderPane borderPane = new BorderPane();
        borderPane.setTop(vboxTop);
        borderPane.setCenter(tableSugerencias);
        borderPane.setBottom(hboxBottom);

        Scene scene = new Scene(borderPane, 600, 400);
        stage.setScene(scene);

        initialized = true;
    }

    /**
     * Muestra la ventana y carga las sugerencias
     * 
     * @param control     El controlador asociado
     * @param sugerencias La lista de sugerencias a mostrar
     */
    public void muestra(ControlListarSugerencias control, List<Sugerencia> sugerencias) {
        this.control = control;

        if (!Platform.isFxApplicationThread()) {
            Platform.runLater(() -> this.muestra(control, sugerencias));
            return;
        }

        initializeUI();

        ObservableList<Sugerencia> data = FXCollections.observableArrayList(sugerencias);
        tableSugerencias.setItems(data);

        stage.show();
    }
}