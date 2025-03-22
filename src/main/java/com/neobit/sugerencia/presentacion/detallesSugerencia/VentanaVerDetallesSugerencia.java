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
    private ControlVerDetallesSugerencia control;
    private boolean initialized = false;

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
        if (initialized) {
            return;
        }

        // Crear UI solo si estamos en el hilo de JavaFX
        if (!Platform.isFxApplicationThread()) {
            Platform.runLater(this::initializeUI);
            return;
        }

        stage = new Stage();
        stage.setTitle("Detalles de la Sugerencia");

        // Crear componentes de la UI
        Label lblTitulo = new Label("Detalles de la Sugerencia");
        lblTitulo.setStyle("-fx-font-size: 24px; -fx-font-weight: bold;");

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
        vboxTop.getChildren().addAll(lblTitulo);

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
    public void muestra(ControlVerDetallesSugerencia control, Sugerencia sugerencia) {
        this.control = control;

        // Asegurarse de que la UI se inicie en el hilo de JavaFX
        if (!Platform.isFxApplicationThread()) {
            Platform.runLater(() -> this.muestra(control, sugerencia));
            return;
        }

        initializeUI(); // Inicializar la UI

        // Cargar los comentarios de la sugerencia en la tabla
        ObservableList<Comentario> data = FXCollections.observableArrayList(sugerencia.getComentarios());
        tableComentarios.setItems(data);

        stage.show(); // Mostrar la ventana
    }
}
