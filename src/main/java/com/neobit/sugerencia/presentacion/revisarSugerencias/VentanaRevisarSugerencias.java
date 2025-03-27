package com.neobit.sugerencia.presentacion.revisarSugerencias;

import com.neobit.sugerencia.negocio.modelo.Sugerencia;
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

import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class VentanaRevisarSugerencias {

    private Stage stage;
    private TableView<Sugerencia> tablaSugerencias;
    @Autowired
    @Lazy
    private ControlRevisarSugerencias control;
    private ObservableList<Sugerencia> sugerenciasOriginales;
    private ComboBox<String> filtroComboBox;

    /**
     * Muestra la ventana con la lista de sugerencias.
     */
    public void muestra(ControlRevisarSugerencias control, List<Sugerencia> sugerencias) {
        this.control = control;

        if (!Platform.isFxApplicationThread()) {
            Platform.runLater(() -> muestra(control, sugerencias));
            return;
        }

        stage = new Stage();
        stage.setTitle("Revisar Sugerencias");

        // ComboBox de filtrado
        filtroComboBox = new ComboBox<>();
        filtroComboBox.getItems().addAll("Todos", "Más recientes", "Más antiguos", "Pendiente", "Rechazados",
                "Aprobados");
        filtroComboBox.setValue("Todos");
        filtroComboBox.setOnAction(e -> aplicarFiltro());

        filtroComboBox.setStyle("-fx-background-color: #ffffff; -fx-border-color: #006666; -fx-text-fill: #006666;");

        // Tabla de sugerencias
        tablaSugerencias = new TableView<>();
        TableColumn<Sugerencia, Long> columnaId = new TableColumn<>("ID");
        columnaId.setCellValueFactory(new PropertyValueFactory<>("id"));

        TableColumn<Sugerencia, String> columnaTitulo = new TableColumn<>("Título");
        columnaTitulo.setCellValueFactory(new PropertyValueFactory<>("titulo"));

        TableColumn<Sugerencia, String> descripcionColumn = new TableColumn<>("Descripción");
        descripcionColumn.setCellValueFactory(new PropertyValueFactory<>("descripcionBreve"));

        TableColumn<Sugerencia, String> autorColumn = new TableColumn<>("Autor");
        autorColumn.setCellValueFactory(new PropertyValueFactory<>("autor"));

        TableColumn<Sugerencia, LocalDate> fechaCreacionColumn = new TableColumn<>("Fecha Creación");
        fechaCreacionColumn.setCellValueFactory(new PropertyValueFactory<>("fechaCreacion"));

        TableColumn<Sugerencia, LocalDate> ultimaActualizacionColumn = new TableColumn<>("Última Actualización");
        ultimaActualizacionColumn.setCellValueFactory(new PropertyValueFactory<>("ultimaActualizacion"));

        TableColumn<Sugerencia, String> columnaEstado = new TableColumn<>("Estado");
        columnaEstado.setCellValueFactory(new PropertyValueFactory<>("estado"));

        TableColumn<Sugerencia, Void> columnaAcciones = new TableColumn<>("Acciones");
        columnaAcciones.setCellFactory(param -> new TableCell<>() {
            private final ComboBox<String> comboEstado = new ComboBox<>();
            private final Button btnActualizar = new Button("Actualizar");
            private final Button btnRetroalimentacion = new Button("Retroalimentación");

            {
                comboEstado.getItems().addAll("Aprobada", "En espera", "Rechazada");

                // Estilo del ComboBox y Botón
                comboEstado.setStyle(
                        "-fx-background-color: #ffffff;" +
                                "-fx-border-color: #006666;" +
                                "-fx-text-fill: #006666;");

                btnActualizar.setStyle(
                        "-fx-background-color: #006666;" +
                                "-fx-text-fill: #ffffff;" +
                                "-fx-padding: 5px 10px;" +
                                "-fx-border-radius: 5px;");

                btnActualizar.setOnAction(e -> {
                    Sugerencia sugerencia = getTableView().getItems().get(getIndex());
                    String nuevoEstado = comboEstado.getValue();

                    if (nuevoEstado != null && !nuevoEstado.equals(sugerencia.getEstado())) {
                        Alert confirmacion = new Alert(Alert.AlertType.CONFIRMATION);
                        confirmacion.setTitle("Confirmar cambio de estado");
                        confirmacion.setHeaderText("¿Estás seguro de cambiar el estado?");
                        confirmacion.setContentText("Sugerencia: " + sugerencia.getTitulo());

                        confirmacion.showAndWait().ifPresent(response -> {
                            if (response == ButtonType.OK) {
                                control.cambiarEstadoSugerencia(sugerencia.getId(), nuevoEstado);
                                sugerencia.setEstado(nuevoEstado); // Actualiza localmente la tabla
                                getTableView().refresh();
                            }
                        });
                    }
                });

                btnRetroalimentacion.setOnAction(e -> {
                    Sugerencia sugerencia = getTableView().getItems().get(getIndex());
                    mostrarVentanaRetroalimentacion(sugerencia);
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    Sugerencia sugerencia = getTableView().getItems().get(getIndex());
                    comboEstado.setValue(sugerencia.getEstado()); // Muestra el estado actual
                    HBox accionesBox = new HBox(5, comboEstado, btnActualizar, btnRetroalimentacion);
                    accionesBox.setAlignment(Pos.CENTER);
                    setGraphic(accionesBox);
                }
            }
        });

        tablaSugerencias.getColumns().addAll(columnaId, columnaTitulo, descripcionColumn, autorColumn,
                fechaCreacionColumn, ultimaActualizacionColumn, columnaEstado, columnaAcciones);

        // Datos
        ObservableList<Sugerencia> datos = FXCollections.observableArrayList(sugerencias);
        tablaSugerencias.setItems(datos);

        // Estilo general de la tabla
        tablaSugerencias.setStyle("-fx-background-color: #ffffff;");
        tablaSugerencias.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        VBox layout = new VBox(10, filtroComboBox, tablaSugerencias);
        layout.setPadding(new Insets(20));
        layout.setStyle("-fx-background-color: #eaf4f4;");

        // Escena
        Scene escena = new Scene(layout, 800, 600);
        stage.setScene(escena);
        stage.show();

        aplicarFiltro();
    }

    public void muestra() {
        if (control == null) {
            throw new IllegalStateException("El controlador no ha sido inicializado.");
        }

        List<Sugerencia> sugerencias = control.obtenerTodasLasSugerencias();
        muestra(control, sugerencias);
    }

    private void aplicarFiltro() {
        String filtro = filtroComboBox.getValue();
        List<Sugerencia> listaFiltrada;

        switch (filtro) {
            case "Más recientes":
                listaFiltrada = sugerenciasOriginales.stream()
                        .sorted(Comparator.comparing(Sugerencia::getId).reversed())
                        .collect(Collectors.toList());
                break;
            case "Más antiguos":
                listaFiltrada = sugerenciasOriginales.stream()
                        .sorted(Comparator.comparing(Sugerencia::getId))
                        .collect(Collectors.toList());
                break;
            case "Pendiente":
                listaFiltrada = sugerenciasOriginales.stream()
                        .filter(s -> "En espera".equals(s.getEstado()))
                        .collect(Collectors.toList());
                break;
            case "Rechazados":
                listaFiltrada = sugerenciasOriginales.stream()
                        .filter(s -> "Rechazada".equals(s.getEstado()))
                        .collect(Collectors.toList());
                break;
            case "Aprobados":
                listaFiltrada = sugerenciasOriginales.stream()
                        .filter(s -> "Aprobada".equals(s.getEstado()))
                        .collect(Collectors.toList());
                break;
            default:
                listaFiltrada = sugerenciasOriginales;
                break;
        }

        tablaSugerencias.setItems(FXCollections.observableArrayList(listaFiltrada));
    }

    /**
     * Muestra una ventana para enviar retroalimentación sobre una sugerencia.
     *
     * @param sugerencia La sugerencia a la que se le enviará la retroalimentación.
     */
    private void mostrarVentanaRetroalimentacion(Sugerencia sugerencia) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Enviar Retroalimentación");
        alert.setHeaderText("Retroalimentación para: " + sugerencia.getTitulo());

        // Crear un área de texto para la retroalimentación
        TextArea txtRetroalimentacion = new TextArea();
        txtRetroalimentacion.setPromptText("Escribe aquí la retroalimentación...");
        txtRetroalimentacion.setWrapText(true);

        // Contenedor para el área de texto
        VBox content = new VBox(10, new Label("Escribe tu retroalimentación:"), txtRetroalimentacion);
        content.setPadding(new Insets(10));

        // Agregar contenido al Alert
        alert.getDialogPane().setContent(content);

        // Botones
        ButtonType enviarButton = new ButtonType("Enviar", ButtonBar.ButtonData.OK_DONE);
        ButtonType cancelarButton = new ButtonType("Cancelar", ButtonBar.ButtonData.CANCEL_CLOSE);
        alert.getButtonTypes().setAll(enviarButton, cancelarButton);

        // Mostrar ventana y obtener la respuesta del usuario
        alert.showAndWait().ifPresent(response -> {
            if (response == enviarButton) {
                String retroalimentacion = txtRetroalimentacion.getText().trim();
                if (!retroalimentacion.isEmpty()) {
                    control.enviarRetroalimentacion(sugerencia.getId(), retroalimentacion);
                    Alert confirmacion = new Alert(Alert.AlertType.INFORMATION,
                            "Retroalimentación enviada correctamente.", ButtonType.OK);
                    confirmacion.showAndWait();
                } else {
                    Alert error = new Alert(Alert.AlertType.ERROR, "La retroalimentación no puede estar vacía.",
                            ButtonType.OK);
                    error.showAndWait();
                }
            }
        });
    }

}
