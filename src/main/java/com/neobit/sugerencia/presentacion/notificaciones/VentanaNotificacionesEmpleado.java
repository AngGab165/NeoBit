package com.neobit.sugerencia.presentacion.notificaciones;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import com.neobit.sugerencia.negocio.modelo.Notificaciones;
import com.neobit.sugerencia.presentacion.principal.ControlPrincipalEmpleado;

import java.util.List;

@Component
public class VentanaNotificacionesEmpleado {

    private Stage stage;
    private TableView<Notificaciones> tableNotificaciones;

    @Autowired
    private ControlPrincipalEmpleado control;

    public void initializeUI() {
        if (!Platform.isFxApplicationThread()) {
            Platform.runLater(this::initializeUI);
            return;
        }

        stage = new Stage();
        stage.setTitle("Mis Notificaciones");

        Label lblTitulo = new Label("Notificaciones Recibidas");
        lblTitulo.setStyle("-fx-font-size: 20px; -fx-font-weight: bold; -fx-text-fill: #006666;");

        tableNotificaciones = new TableView<>();
        tableNotificaciones.setStyle("-fx-background-color: #ffffff;");

        TableColumn<Notificaciones, String> mensajeColumn = new TableColumn<>("Mensaje");
        mensajeColumn.setCellValueFactory(new PropertyValueFactory<>("mensaje"));

        TableColumn<Notificaciones, String> fechaColumn = new TableColumn<>("Fecha");
        fechaColumn.setCellValueFactory(new PropertyValueFactory<>("fecha"));

        tableNotificaciones.getColumns().addAll(mensajeColumn, fechaColumn);

        VBox vbox = new VBox(20, lblTitulo, tableNotificaciones);
        vbox.setPadding(new Insets(20));
        vbox.setAlignment(Pos.CENTER);

        BorderPane borderPane = new BorderPane();
        borderPane.setCenter(vbox);
        borderPane.setStyle("-fx-background-color: #eaf4f4;");

        Scene scene = new Scene(borderPane, 600, 400);
        stage.setScene(scene);
    }

    public void muestra() {
        this.control = control;
        initializeUI();
        actualizarTabla();
        stage.show();
    }

    private void actualizarTabla() {
        Long idEmpleado = control.getEmpleadoActual().getId();
        List<Notificaciones> notificaciones = control.getControlNotificaciones()
                .obtenerNotificacionesPorEmpleado(idEmpleado);

        ObservableList<Notificaciones> data = FXCollections.observableArrayList(notificaciones);
        tableNotificaciones.setItems(data);
    }
}
