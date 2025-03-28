package com.neobit.sugerencia.presentacion.detallesSugerencia;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import com.neobit.sugerencia.negocio.modelo.Comentario;
import com.neobit.sugerencia.negocio.modelo.Sugerencia;

/**
 * Ventana para ver detalles de una sugerencia usando JavaFX
 */
@Component
public class VentanaVerDetallesSugerencia {

    private Stage stage;
    private TableView<Comentario> tableComentarios;
    @Autowired
    private ControlVerDetallesSugerencia control;
    private boolean initialized = false;
    private Label lblTituloValue;
    private Label lblDescripcionValue;

    private Sugerencia sugerencia;

    /**
     * Constructor sin inicialización de la UI
     */
    public VentanaVerDetallesSugerencia() {
        // No inicializamos la UI en el constructor
    }

    /**
     * Inicializa los componentes de la UI en el hilo de la aplicación JavaFX
     */
    private void initializeUI() {
        // Evitar reinicialización si ya ha sido inicializado
        if (initialized) {
            return;
        }

        // Asegúrate de que se ejecute en el hilo de JavaFX si no estás ya en él
        if (Platform.isFxApplicationThread()) {
            crearUI(); // Crear la interfaz si ya estamos en el hilo de JavaFX
        } else {
            Platform.runLater(this::crearUI); // Ejecutar en el hilo de JavaFX si no estamos en él
        }
    }

    private void crearUI() {
        // Crear UI solo si estamos en el hilo de JavaFX
        if (initialized) {
            return;
        }

        this.lblTituloValue = new Label();
        this.lblDescripcionValue = new Label();
        this.tableComentarios = new TableView<>();

        // Inicialización de la ventana (UI)
        stage = new Stage();
        stage.setTitle("Detalles de la Sugerencia");

        // Crear componentes de la UI
        Label lblTitulo = new Label("Detalles de la Sugerencia");
        lblTitulo.setStyle("-fx-font-size: 24px; -fx-font-weight: bold;");

        // Agregar detalles de la sugerencia (título y descripción)
        Label lblTituloSugerencia = new Label("Título: ");
        Label lblDescripcionSugerencia = new Label("Descripción: ");

        // Estas etiquetas se actualizarán dinámicamente
        this.lblTituloValue = new Label();
        this.lblDescripcionValue = new Label();

        // Crear tabla de comentarios
        tableComentarios = new TableView<>();

        // Configurar columnas
        TableColumn<Comentario, String> autorColumn = new TableColumn<>("Autor");
        autorColumn.setCellValueFactory(new PropertyValueFactory<>("autor"));

        TableColumn<Comentario, String> fechaColumn = new TableColumn<>("Fecha");
        fechaColumn.setCellValueFactory(new PropertyValueFactory<>("fecha"));

        TableColumn<Comentario, String> contenidoColumn = new TableColumn<>("Contenido");
        contenidoColumn.setCellValueFactory(new PropertyValueFactory<>("texto"));

        tableComentarios.getColumns().addAll(autorColumn, fechaColumn, contenidoColumn);

        // Crear área de texto para agregar nuevos comentarios
        TextArea txtNuevoComentario = new TextArea();
        txtNuevoComentario.setPromptText("Escribe tu comentario aquí...");
        txtNuevoComentario.setWrapText(true);

        Button btnAgregarComentario = new Button("Agregar Comentario");
        btnAgregarComentario.setOnAction(e -> {
            String textoComentario = txtNuevoComentario.getText();
            if (textoComentario != null && textoComentario.length() >= 5) {
                control.agregarComentario(textoComentario); // Llamada al controlador para agregar el comentario
                txtNuevoComentario.clear(); // Limpiar campo de texto
            } else {
                // Mostrar mensaje de error si el comentario es demasiado corto
                System.out.println("El comentario debe tener al menos 5 caracteres.");
            }
        });

        Button btnCerrar = new Button("Cerrar");
        btnCerrar.setOnAction(e -> stage.close()); // Cerrar ventana

        // Diseño de la ventana
        VBox vboxTop = new VBox(10);
        vboxTop.setPadding(new Insets(10));
        vboxTop.getChildren().addAll(lblTitulo, lblTituloSugerencia, lblTituloValue, lblDescripcionSugerencia,
                lblDescripcionValue);

        VBox vboxBottom = new VBox(10);
        vboxBottom.setPadding(new Insets(10));
        vboxBottom.getChildren().addAll(txtNuevoComentario, btnAgregarComentario, btnCerrar);

        BorderPane borderPane = new BorderPane();
        borderPane.setTop(vboxTop);
        borderPane.setCenter(tableComentarios);
        borderPane.setBottom(vboxBottom);

        Scene scene = new Scene(borderPane, 600, 400);
        stage.setScene(scene);

        initialized = true;
    }

    /**
     * Muestra la ventana y carga los comentarios
     * 
     * @param control    El controlador asociado
     * @param sugerencia La sugerencia cuyos detalles se van a mostrar
     */
    public void muestra(Sugerencia sugerencia) {
        if (sugerencia == null) {
            System.out.println("Error: No hay sugerencia seleccionada.");
            return;
        }

        this.sugerencia = sugerencia; // Asignar sugerencia al atributo de clase

        System.out.println("Mostrando ventana de detalles para: " + sugerencia.getTitulo());

        if (!Platform.isFxApplicationThread()) {
            Platform.runLater(() -> this.muestra(sugerencia));
            return;
        }

        initializeUI();

        lblTituloValue.setText(sugerencia.getTitulo());
        lblDescripcionValue.setText(sugerencia.getDescripcionBreve());

        actualizarComentarios(); // Asegurar que se cargan los comentarios de la sugerencia
        stage.show();
    }

    public void actualizarComentarios() {
        if (!Platform.isFxApplicationThread()) {
            Platform.runLater(this::actualizarComentarios);
            return;
        }

        // Solo actualizar la tabla si hay comentarios
        if (sugerencia != null && sugerencia.getComentarios() != null) {
            ObservableList<Comentario> data = FXCollections.observableArrayList(sugerencia.getComentarios());

            // Actualizar la tabla con los comentarios más recientes
            tableComentarios.setItems(data);
            tableComentarios.refresh(); // Forzar actualización visual
        }
    }

}
