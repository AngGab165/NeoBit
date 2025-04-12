package com.neobit.sugerencia.presentacion.foro;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.*;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.neobit.sugerencia.negocio.modelo.RespuestaForo;
import com.neobit.sugerencia.negocio.modelo.TemaForo;
import com.neobit.sugerencia.negocio.ForoService;

@Component
public class VentanaForo {

    @Autowired
    private ForoService foroService;

    private Stage stage;

    public void mostrar() {
        stage = new Stage();
        stage.setTitle("Foro Interno");

        // Encabezado
        Label lblTitulo = new Label("Foro Interno");
        lblTitulo.setStyle("-fx-font-size: 24px; -fx-font-weight: bold; -fx-text-fill: #006666;");
        VBox header = new VBox(lblTitulo);
        header.setAlignment(Pos.CENTER);
        header.setPadding(new Insets(20));
        header.setStyle("-fx-background-color: #F0F0F0;");

        // Tabla de temas
        TableView<TemaForo> tablaTemas = new TableView<>();
        tablaTemas.setStyle("-fx-border-color: #006666;");
        ObservableList<TemaForo> temas = FXCollections.observableArrayList(foroService.obtenerTodosLosTemas());

        TableColumn<TemaForo, String> columnaTitulo = new TableColumn<>("Título");
        columnaTitulo.setCellValueFactory(new PropertyValueFactory<>("titulo"));

        TableColumn<TemaForo, String> columnaAutor = new TableColumn<>("Autor");
        columnaAutor.setCellValueFactory(new PropertyValueFactory<>("autor"));

        TableColumn<TemaForo, String> columnaFecha = new TableColumn<>("Fecha");
        columnaFecha.setCellValueFactory(new PropertyValueFactory<>("fechaCreacion"));

        TableColumn<TemaForo, Integer> columnaRespuestas = new TableColumn<>("Respuestas");
        columnaRespuestas.setCellValueFactory(
                data -> new javafx.beans.property.SimpleIntegerProperty(data.getValue().getNumeroRespuestas())
                        .asObject());

        tablaTemas.getColumns().addAll(columnaTitulo, columnaAutor, columnaFecha, columnaRespuestas);
        tablaTemas.setItems(temas);

        // Tabla de respuestas
        TableView<RespuestaForo> tablaRespuestas = new TableView<>();
        tablaRespuestas.setStyle("-fx-border-color: #006666;");
        ObservableList<RespuestaForo> respuestas = FXCollections.observableArrayList();

        TableColumn<RespuestaForo, String> columnaAdministrador = new TableColumn<>("Administrador");
        columnaAdministrador.setCellValueFactory(new PropertyValueFactory<>("administrador"));

        TableColumn<RespuestaForo, String> columnaContenido = new TableColumn<>("Contenido");
        columnaContenido.setCellValueFactory(new PropertyValueFactory<>("contenido"));

        TableColumn<RespuestaForo, String> columnaFechaRespuesta = new TableColumn<>("Fecha");
        columnaFechaRespuesta.setCellValueFactory(new PropertyValueFactory<>("fechaRespuesta"));

        tablaRespuestas.getColumns().addAll(columnaAdministrador, columnaContenido, columnaFechaRespuesta);
        tablaRespuestas.setItems(respuestas);

        // Formulario para agregar nuevos temas
        TextField txtTitulo = new TextField();
        txtTitulo.setPromptText("Título");
        txtTitulo.setStyle("-fx-border-color: #006666; -fx-background-color: white;");

        TextField txtAutor = new TextField();
        txtAutor.setPromptText("Autor");
        txtAutor.setStyle("-fx-border-color: #006666; -fx-background-color: white;");

        Button btnAgregar = new Button("Agregar Tema");
        btnAgregar.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white;");
        btnAgregar.setOnAction(e -> {
            String titulo = txtTitulo.getText();
            String autor = txtAutor.getText();

            if (!titulo.isEmpty() && !autor.isEmpty()) {
                TemaForo nuevoTema = foroService.agregarTema(titulo, autor);
                temas.add(nuevoTema);
                txtTitulo.clear();
                txtAutor.clear();
            } else {
                Alert alerta = new Alert(Alert.AlertType.WARNING, "Por favor, complete todos los campos.");
                alerta.showAndWait();
            }
        });

        // Botón para responder a un tema
        Button btnResponder = new Button("Responder");
        btnResponder.setStyle("-fx-background-color: #2196F3; -fx-text-fill: white;");
        btnResponder.setOnAction(e -> {
            TemaForo temaSeleccionado = tablaTemas.getSelectionModel().getSelectedItem();
            if (temaSeleccionado != null) {
                TextInputDialog dialog = new TextInputDialog();
                dialog.setTitle("Responder al Tema");
                dialog.setHeaderText("Agregar una respuesta al tema: " + temaSeleccionado.getTitulo());
                dialog.setContentText("Escribe tu respuesta:");

                dialog.showAndWait().ifPresent(contenido -> {
                    TextInputDialog adminDialog = new TextInputDialog();
                    adminDialog.setTitle("Nombre del Administrador");
                    adminDialog.setHeaderText("Ingrese el nombre del administrador:");
                    adminDialog.setContentText("Administrador:");

                    adminDialog.showAndWait().ifPresent(administrador -> {
                        foroService.agregarRespuesta(temaSeleccionado, administrador, contenido);
                        respuestas.setAll(foroService.obtenerRespuestasPorTema(temaSeleccionado));
                        tablaTemas.refresh();
                    });
                });
            } else {
                Alert alerta = new Alert(Alert.AlertType.WARNING, "Por favor, selecciona un tema.");
                alerta.showAndWait();
            }
        });

        // Actualizar respuestas al seleccionar un tema
        tablaTemas.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                respuestas.setAll(foroService.obtenerRespuestasPorTema(newSelection));
            }
        });

        // Diseño del formulario
        HBox formulario = new HBox(10, txtTitulo, txtAutor, btnAgregar);
        formulario.setAlignment(Pos.CENTER);
        formulario.setPadding(new Insets(10));

        // Footer con derechos reservados
        Text footerText = new Text("©2025 Derechos Reservados - Sistema Sugerencias - NeoBit");
        footerText.setStyle("-fx-fill: white; -fx-font-size: 12px;");
        HBox footer = new HBox(footerText);
        footer.setAlignment(Pos.CENTER);
        footer.setPadding(new Insets(10));
        footer.setStyle("-fx-background-color: #006666;");

        // Diseño principal
        VBox vbox = new VBox(15, tablaTemas, tablaRespuestas, formulario, btnResponder);
        vbox.setPadding(new Insets(20));
        vbox.setAlignment(Pos.CENTER);
        vbox.setStyle("-fx-background-color: #eaf4f4;");

        BorderPane root = new BorderPane();
        root.setTop(header);
        root.setCenter(vbox);
        root.setBottom(footer);

        Scene scene = new Scene(root, 900, 600);
        stage.setScene(scene);
        stage.show();
    }
}
