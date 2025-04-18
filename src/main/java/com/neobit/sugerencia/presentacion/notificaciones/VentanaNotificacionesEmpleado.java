package com.neobit.sugerencia.presentacion.notificaciones;

import javafx.util.Duration;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.scene.control.*;
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
        /* */

        // Nuevas columnas según requerimientos
        TableColumn<Notificaciones, String> tipoColumn = new TableColumn<>("Tipo");
        tipoColumn.setCellValueFactory(new PropertyValueFactory<>("tipo"));

        TableColumn<Notificaciones, String> mensajeColumn = new TableColumn<>("Mensaje");
        mensajeColumn.setCellValueFactory(new PropertyValueFactory<>("mensaje"));

        TableColumn<Notificaciones, String> fechaColumn = new TableColumn<>("Fecha");
        fechaColumn.setCellValueFactory(new PropertyValueFactory<>("fecha"));

        TableColumn<Notificaciones, String> estadoColumn = new TableColumn<>("Estado");
        estadoColumn.setCellValueFactory(new PropertyValueFactory<>("estado"));

        tableNotificaciones.getColumns().add(tipoColumn);
        tableNotificaciones.getColumns().add(mensajeColumn);
        tableNotificaciones.getColumns().add(fechaColumn);
        tableNotificaciones.getColumns().add(estadoColumn);

        // Botón para marcar como leída
        Button btnMarcarLeida = new Button("Marcar como leída");
        btnMarcarLeida.setOnAction(e -> {
            Notificaciones seleccionada = tableNotificaciones.getSelectionModel().getSelectedItem();
            if (seleccionada != null) {
                control.getControlNotificaciones().marcarComoLeida(seleccionada.getId());
                actualizarTabla();
            }
        });

        VBox vbox = new VBox(20, lblTitulo, tableNotificaciones, btnMarcarLeida);
        vbox.setPadding(new Insets(20));
        vbox.setAlignment(Pos.CENTER);

        // Footer con derechos reservados
        Label footerText = new Label("©2025 Derechos Reservados - Sistema Sugerencias - NeoBit");
        footerText.setStyle("-fx-text-fill: white; -fx-font-size: 12px;");
        HBox footer = new HBox(footerText);
        footer.setAlignment(Pos.CENTER);
        footer.setPadding(new Insets(10));
        footer.setStyle("-fx-background-color: #006666;");

        BorderPane borderPane = new BorderPane();
        borderPane.setCenter(vbox);
        borderPane.setBottom(footer);
        borderPane.setStyle("-fx-background-color: #eaf4f4;");

        Scene scene = new Scene(borderPane, 800, 500); // Aumentado el tamaño para más columnas
        stage.setScene(scene);
    }

    private void configurarActualizacionAutomatica() {
        Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(5), e -> actualizarTabla()));
        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.play();
    }

    // Modificar el método muestra()
    public void muestra() {
        initializeUI();
        actualizarTabla();
        configurarActualizacionAutomatica(); // Agregar esta línea
        stage.show();
    }

    public void actualizarTabla() {
        if (control.getUsuarioActual() == null) {
            System.out.println("Advertencia: No hay usuario actual. No se pueden cargar notificaciones.");
            return;
        }

        Long idUsuario = control.getUsuarioActual().getId();
        System.out.println("Usuario actual ID: " + idUsuario); // <-- para depuración

        List<Notificaciones> notificaciones = control.getControlNotificaciones()
                .obtenerNotificacionesPorUsuario(idUsuario);

        ObservableList<Notificaciones> data = FXCollections.observableArrayList(notificaciones);
        tableNotificaciones.setItems(data);
    }

}
