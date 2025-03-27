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
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    Sugerencia sugerencia = getTableView().getItems().get(getIndex());
                    comboEstado.setValue(sugerencia.getEstado()); // Muestra el estado actual
                    HBox accionesBox = new HBox(5, comboEstado, btnActualizar);
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
}
