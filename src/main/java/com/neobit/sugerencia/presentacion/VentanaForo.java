package com.neobit.sugerencia.presentacion;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
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

        // Tabla de temas
        TableView<TemaForo> tablaTemas = new TableView<>();
        ObservableList<TemaForo> temas = FXCollections.observableArrayList(foroService.obtenerTodosLosTemas());

        TableColumn<TemaForo, String> columnaTitulo = new TableColumn<>("Título");
        columnaTitulo.setCellValueFactory(new PropertyValueFactory<>("titulo"));

        TableColumn<TemaForo, String> columnaAutor = new TableColumn<>("Autor");
        columnaAutor.setCellValueFactory(new PropertyValueFactory<>("autor"));

        TableColumn<TemaForo, String> columnaFecha = new TableColumn<>("Fecha");
        columnaFecha.setCellValueFactory(new PropertyValueFactory<>("fechaCreacion"));

        TableColumn<TemaForo, Integer> columnaRespuestas = new TableColumn<>("Respuestas");
        columnaRespuestas.setCellValueFactory(data ->
                new javafx.beans.property.SimpleIntegerProperty(data.getValue().getNumeroRespuestas()).asObject()
        );

        tablaTemas.getColumns().addAll(columnaTitulo, columnaAutor, columnaFecha, columnaRespuestas);
        tablaTemas.setItems(temas);

        // Tabla de respuestas
        TableView<RespuestaForo> tablaRespuestas = new TableView<>();
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

        TextField txtAutor = new TextField();
        txtAutor.setPromptText("Autor");

        Button btnAgregar = new Button("Agregar Tema");
        btnAgregar.setOnAction(e -> {
            String titulo = txtTitulo.getText();
            String autor = txtAutor.getText();

            if (!titulo.isEmpty() && !autor.isEmpty()) {
                // Agregar el nuevo tema y obtener el tema guardado
                TemaForo nuevoTema = foroService.agregarTema(titulo, autor);

                // Agregar el nuevo tema directamente a la lista observable
                temas.add(nuevoTema);

                // Limpiar los campos del formulario
                txtTitulo.clear();
                txtAutor.clear();
            } else {
                Alert alerta = new Alert(Alert.AlertType.WARNING, "Por favor, complete todos los campos.");
                alerta.showAndWait();
            }
        });

        // Botón para responder a un tema
        Button btnResponder = new Button("Responder");
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
                        // Agregar la respuesta al tema
                        foroService.agregarRespuesta(temaSeleccionado, administrador, contenido);

                        // Actualizar la tabla de respuestas
                        respuestas.setAll(foroService.obtenerRespuestasPorTema(temaSeleccionado));

                        // Actualizar la tabla de temas para reflejar el número de respuestas
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

        // Diseño principal
        VBox vbox = new VBox(10, tablaTemas, tablaRespuestas, formulario, btnResponder);
        vbox.setPadding(new Insets(10));
        vbox.setAlignment(Pos.CENTER);

        BorderPane root = new BorderPane();
        root.setCenter(vbox);

        Scene scene = new Scene(root, 800, 600);
        stage.setScene(scene);
        stage.show();
    }
}