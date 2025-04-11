package com.neobit.sugerencia.presentacion.detallesSugerencia;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;
//Si
import org.springframework.beans.factory.annotation.Autowired;
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

    private String nombreEmpleado;

    public void setNombreEmpleado(String nombreEmpleado) {
        this.nombreEmpleado = nombreEmpleado;
        System.out.println("NombreEmpleado establecido en VentanaVerDetallesSugerencia: " + nombreEmpleado);
    }

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
        lblTitulo.setStyle("-fx-font-size: 24px; -fx-font-weight: bold; -fx-text-fill: #006666;");
        VBox header = new VBox(10, lblTitulo);
        header.setAlignment(Pos.CENTER);
        header.setPadding(new Insets(20));
        header.setStyle("-fx-background-color: #F0F0F0;");

        // Agregar detalles de la sugerencia (título y descripción)
        Label lblTituloSugerencia = new Label("Título: ");
        lblTituloSugerencia.setStyle("-fx-font-size: 14px; -fx-text-fill: #006666;");
        Label lblDescripcionSugerencia = new Label("Descripción: ");
        lblDescripcionSugerencia.setStyle("-fx-font-size: 14px; -fx-text-fill: #006666;");

        // Estas etiquetas se actualizarán dinámicamente
        this.lblTituloValue = new Label();
        this.lblDescripcionValue = new Label();

        // Crear tabla de comentarios
        tableComentarios = new TableView<>();

        // Configurar columnas
        TableColumn<Comentario, String> autorColumn = new TableColumn<>("Autor");
        autorColumn.setCellValueFactory(new PropertyValueFactory<>("autor"));
        autorColumn.setPrefWidth(170);

        TableColumn<Comentario, String> fechaColumn = new TableColumn<>("Fecha");
        fechaColumn.setCellValueFactory(new PropertyValueFactory<>("fecha"));
        fechaColumn.setPrefWidth(180);

        TableColumn<Comentario, String> contenidoColumn = new TableColumn<>("Contenido");
        contenidoColumn.setCellValueFactory(new PropertyValueFactory<>("texto"));
        contenidoColumn.setPrefWidth(450);

        tableComentarios.getColumns().addAll(autorColumn, fechaColumn, contenidoColumn);

        // Crear área de texto para agregar nuevos comentarios
        TextArea txtNuevoComentario = new TextArea();
        txtNuevoComentario.setPromptText("Escribe tu comentario aquí...");
        txtNuevoComentario.setWrapText(true);

        Button btnAgregarComentario = new Button("Agregar Comentario");
        btnAgregarComentario.setStyle("-fx-background-color: #006666; -fx-text-fill: white; -fx-font-size: 14px;");
        btnAgregarComentario.setOnAction(e -> {
            String textoComentario = txtNuevoComentario.getText();
            if (textoComentario != null && textoComentario.length() >= 5) {
                control.setSugerencia(sugerencia);
                System.out.println("Agregando comentario con nombreEmpleado: " + nombreEmpleado);
                control.setNombreEmpleado(nombreEmpleado); // Cambia esto según el contexto
                control.agregarComentario(textoComentario, nombreEmpleado); // Agregar comentario usando el controlador

                txtNuevoComentario.clear(); // Limpiar campo de texto
            } else {
                // Mostrar mensaje de error si el comentario es demasiado corto
                System.out.println("El comentario debe tener al menos 5 caracteres.");
            }
        });

        Button btnCerrar = new Button("Cerrar");
        btnCerrar.setStyle("-fx-background-color: #006666; -fx-text-fill: white; -fx-font-size: 14px;");
        btnCerrar.setOnAction(e -> stage.close()); // Cerrar ventana

        // Diseño de la ventana
        VBox vboxTop = new VBox(10);
        vboxTop.setPadding(new Insets(10));
        vboxTop.getChildren().addAll(lblTitulo, lblTituloSugerencia, lblTituloValue, lblDescripcionSugerencia,
                lblDescripcionValue);
        vboxTop.setStyle("-fx-background-color: #eaf4f4;");

        VBox vboxBottom = new VBox(10);
        vboxBottom.setPadding(new Insets(10));
        vboxBottom.getChildren().addAll(txtNuevoComentario, btnAgregarComentario, btnCerrar);

        Text footerText = new Text("©2025 Derechos Reservados - Sistema Sugerencias - NeoBit");
        footerText.setStyle("-fx-fill: white; -fx-font-size: 12px;");
        HBox footer = new HBox(footerText);
        footer.setAlignment(Pos.CENTER);
        footer.setPadding(new Insets(10));
        footer.setStyle("-fx-background-color: #006666;");

        BorderPane borderPane = new BorderPane();
        borderPane.setTop(header);
        borderPane.setCenter(new VBox(10, vboxTop, tableComentarios));
        borderPane.setBottom(new VBox(vboxBottom, footer));

        Scene scene = new Scene(borderPane, 800, 600);
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
