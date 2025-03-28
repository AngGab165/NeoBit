package com.neobit.sugerencia.presentacion.revisarSugerencias;

import com.neobit.sugerencia.negocio.modelo.Prioridad;
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

        filtroComboBox = new ComboBox<>();
        filtroComboBox.getItems().addAll("Todos", "Alta", "Media", "Baja", "Más recientes", "Más antiguos", "Pendiente",
                "Rechazados", "Aprobados");
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

        TableColumn<Sugerencia, Prioridad> prioridadColumn = new TableColumn<>("Prioridad");
        prioridadColumn.setCellValueFactory(new PropertyValueFactory<>("prioridad"));

        TableColumn<Sugerencia, Void> columnaAcciones = new TableColumn<>("Acciones");
        columnaAcciones.setCellFactory(param -> new TableCell<>() {
            private final ComboBox<String> comboEstado = new ComboBox<>();
            private final Button btnActualizar = new Button("Actualizar");
            private final Button btnRecomendar = new Button("Recomendar");
            private final Button btnRetroalimentacion = new Button("Retroalimentación");

            {
                comboEstado.getItems().addAll("Aprobada", "En espera", "Rechazada");

                // Estilo del ComboBox y Botones
                comboEstado.setStyle(
                        "-fx-background-color: #ffffff;" +
                                "-fx-border-color: #006666;" +
                                "-fx-text-fill: #006666;");

                btnActualizar.setStyle(
                        "-fx-background-color: #006666;" +
                                "-fx-text-fill: #ffffff;" +
                                "-fx-padding: 5px 10px;" +
                                "-fx-border-radius: 5px;");

                btnRecomendar.setStyle(
                        "-fx-background-color: #ff9900;" +
                                "-fx-text-fill: #ffffff;" +
                                "-fx-padding: 5px 10px;" +
                                "-fx-border-radius: 5px;");

                btnRetroalimentacion.setStyle(
                        "-fx-background-color: #0099cc;" +
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

                btnRecomendar.setOnAction(e -> {
                    Sugerencia sugerencia = getTableView().getItems().get(getIndex());
                    Alert confirmacion = new Alert(Alert.AlertType.INFORMATION);
                    confirmacion.setTitle("Recomendar Sugerencia");
                    confirmacion.setHeaderText("Sugerencia recomendada");
                    confirmacion.setContentText("Has recomendado la sugerencia: " + sugerencia.getTitulo());
                    confirmacion.showAndWait();

                    // Lógica para manejar la recomendación
                    control.recomendarSugerencia(sugerencia.getId());
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

                    // Contenedor para los botones
                    VBox botonesBox = new VBox(10, btnActualizar, btnRecomendar, btnRetroalimentacion); // Espaciado
                                                                                                        // vertical
                    botonesBox.setAlignment(Pos.CENTER);

                    // Contenedor principal para el ComboBox y los botones
                    HBox accionesBox = new HBox(20, comboEstado, botonesBox); // Espaciado horizontal
                    accionesBox.setAlignment(Pos.CENTER_LEFT);

                    setGraphic(accionesBox);
                }
            }
        });

        tablaSugerencias.getColumns().addAll(columnaId, columnaTitulo, descripcionColumn, autorColumn,
                fechaCreacionColumn, ultimaActualizacionColumn, columnaEstado, prioridadColumn, columnaAcciones);

        // Datos
        sugerenciasOriginales = FXCollections.observableArrayList(sugerencias);
        tablaSugerencias.setItems(sugerenciasOriginales);

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
                        .sorted(Comparator.comparing(Sugerencia::getFechaCreacion).reversed())
                        .collect(Collectors.toList());
                break;
            case "Más antiguos":
                listaFiltrada = sugerenciasOriginales.stream()
                        .sorted(Comparator.comparing(Sugerencia::getFechaCreacion))
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
            case "Alta":
                listaFiltrada = sugerenciasOriginales.stream()
                        .filter(s -> s.getPrioridad() == Prioridad.ALTA)
                        .collect(Collectors.toList());
                break;
            case "Media":
                listaFiltrada = sugerenciasOriginales.stream()
                        .filter(s -> s.getPrioridad() == Prioridad.MEDIA)
                        .collect(Collectors.toList());
                break;
            case "Baja":
                listaFiltrada = sugerenciasOriginales.stream()
                        .filter(s -> s.getPrioridad() == Prioridad.BAJA)
                        .collect(Collectors.toList());
                break;
            default:
                listaFiltrada = sugerenciasOriginales;
                break;
        }

        tablaSugerencias.setItems(FXCollections.observableArrayList(listaFiltrada));
    }

    private void mostrarVentanaRetroalimentacion(Sugerencia sugerencia) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Enviar Retroalimentación");
        alert.setHeaderText("Retroalimentación para: " + sugerencia.getTitulo());

        TextArea txtRetroalimentacion = new TextArea();
        txtRetroalimentacion.setPromptText("Escribe aquí la retroalimentación...");
        txtRetroalimentacion.setWrapText(true);

        VBox content = new VBox(10, new Label("Escribe tu retroalimentación:"), txtRetroalimentacion);
        content.setPadding(new Insets(10));

        alert.getDialogPane().setContent(content);

        ButtonType enviarButton = new ButtonType("Enviar", ButtonBar.ButtonData.OK_DONE);
        ButtonType cancelarButton = new ButtonType("Cancelar", ButtonBar.ButtonData.CANCEL_CLOSE);
        alert.getButtonTypes().setAll(enviarButton, cancelarButton);

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