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

import java.util.List;

@Component
public class VentanaRevisarSugerencias {

    private Stage stage;
    private TableView<Sugerencia> tablaSugerencias;
    @Autowired
    @Lazy
    private ControlRevisarSugerencias control;

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

        // Tabla de sugerencias
        tablaSugerencias = new TableView<>();
        TableColumn<Sugerencia, Long> columnaId = new TableColumn<>("ID");
        columnaId.setCellValueFactory(new PropertyValueFactory<>("id"));

        TableColumn<Sugerencia, String> columnaTitulo = new TableColumn<>("Título");
        columnaTitulo.setCellValueFactory(new PropertyValueFactory<>("titulo"));

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

        tablaSugerencias.getColumns().addAll(columnaId, columnaTitulo, columnaEstado, columnaAcciones);

        // Datos
        ObservableList<Sugerencia> datos = FXCollections.observableArrayList(sugerencias);
        tablaSugerencias.setItems(datos);

        // Estilo general de la tabla
        tablaSugerencias.setStyle("-fx-background-color: #ffffff;");
        tablaSugerencias.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        VBox layout = new VBox(20, tablaSugerencias);
        layout.setPadding(new Insets(20));
        layout.setStyle("-fx-background-color: #eaf4f4;");

        // Escena
        Scene escena = new Scene(layout, 700, 450);
        stage.setScene(escena);
        stage.show();
    }

    public void muestra() {
        if (control == null) {
            throw new IllegalStateException("El controlador no ha sido inicializado.");
        }

        List<Sugerencia> sugerencias = control.obtenerTodasLasSugerencias();
        muestra(control, sugerencias);
    }
}
