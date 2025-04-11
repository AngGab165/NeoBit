package com.neobit.sugerencia.presentacion.usuarios;

import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import com.neobit.sugerencia.negocio.modelo.Rol;
import com.neobit.sugerencia.negocio.modelo.Usuario;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

@Component
public class VentanaUsuarioEmpleado {

    private Stage stage;

    @Autowired
    @Lazy
    private ControlUsuarioEmpleado control;

    TableView<Usuario> tableUsuarios;
    private ObservableList<Usuario> dataUsuarios;

    public void muestra() {
        if (!Platform.isFxApplicationThread()) {
            Platform.runLater(this::muestra);
            return;
        }

        if (stage == null) {
            stage = new Stage();
            stage.setTitle("Registrar Empleado");

            Label lblTitulo = new Label("Registro de Empleado");
            lblTitulo.setStyle("-fx-font-size: 2.5rem; -fx-font-weight: bold; -fx-text-fill: #006666;");

            GridPane formPane = new GridPane();
            formPane.setPadding(new Insets(20));
            formPane.setHgap(10);
            formPane.setVgap(10);

            Label lblNombre = new Label("Nombre:");
            TextField txtNombre = new TextField();
            Label lblCorreo = new Label("Correo:");
            TextField txtCorreo = new TextField();
            Label lblUsuario = new Label("Usuario:");
            TextField txtUsuario = new TextField();
            Label lblContrasena = new Label("Contraseña:");
            PasswordField txtContrasena = new PasswordField();

            Label lblError = new Label();
            lblError.setStyle("-fx-text-fill: red; -fx-font-size: 0.9rem;");

            String labelStyle = "-fx-font-size: 1rem; -fx-text-fill: #006666;";
            lblNombre.setStyle(labelStyle);
            lblCorreo.setStyle(labelStyle);
            lblUsuario.setStyle(labelStyle);
            lblContrasena.setStyle(labelStyle);

            Button btnRegistrar = new Button("Registrar");
            btnRegistrar.setStyle(
                    "-fx-background-color: #006666; -fx-text-fill: white; -fx-padding: 10px 20px; -fx-font-size: 1rem; -fx-border-radius: 5px;");
            btnRegistrar.setOnAction(e -> {
                String nombre = txtNombre.getText().trim();
                String correo = txtCorreo.getText().trim();
                String usuario = txtUsuario.getText().trim();
                String contrasena = txtContrasena.getText();

                if (nombre.isEmpty() || correo.isEmpty() || usuario.isEmpty() || contrasena.isEmpty()) {
                    lblError.setText("Todos los campos son obligatorios.");
                    return;
                }

                if (!correo.matches("^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$")) {
                    lblError.setText("Formato de correo inválido.");
                    return;
                }

                if (contrasena.length() < 8) {
                    lblError.setText("La contraseña debe tener al menos 8 caracteres.");
                    return;
                }

                // Verificar si el correo ya está registrado ANTES de intentar registrarlo
                if (control.existeCorreo(correo)) {
                    lblError.setText("El correo ya está en uso. Intente con otro.");
                    return;
                }

                // Registrar usuario ya que el correo no está duplicado
                control.registrarUsuarioEmpleado(nombre, correo, usuario, contrasena);

                // Limpiar campos después del registro exitoso
                txtNombre.clear();
                txtCorreo.clear();
                txtUsuario.clear();
                txtContrasena.clear();
                lblError.setText(""); // Limpiar mensaje de error

                actualizarTabla();
            });

            formPane.add(lblNombre, 0, 0);
            formPane.add(txtNombre, 1, 0);
            formPane.add(lblCorreo, 0, 1);
            formPane.add(txtCorreo, 1, 1);
            formPane.add(lblUsuario, 0, 2);
            formPane.add(txtUsuario, 1, 2);
            formPane.add(lblContrasena, 0, 3);
            formPane.add(txtContrasena, 1, 3);
            formPane.add(btnRegistrar, 1, 4);
            formPane.add(lblError, 1, 5);

            tableUsuarios = new TableView<>();
            tableUsuarios.setStyle("-fx-font-size: 1rem; -fx-background-color: #ffffff;");
            dataUsuarios = FXCollections.observableArrayList();

            TableColumn<Usuario, String> colNombre = new TableColumn<>("Nombre");
            colNombre.setCellValueFactory(new PropertyValueFactory<>("nombre"));

            TableColumn<Usuario, String> colCorreo = new TableColumn<>("Correo");
            colCorreo.setCellValueFactory(new PropertyValueFactory<>("correo"));

            TableColumn<Usuario, String> colUsuario = new TableColumn<>("Usuario");
            colUsuario.setCellValueFactory(new PropertyValueFactory<>("usuario"));

            TableColumn<Usuario, String> colEliminar = new TableColumn<>("Eliminar");
            colEliminar.setCellFactory(param -> new TableCell<>() {
                final Button btnEliminar = new Button("Eliminar");

                {
                    btnEliminar.setStyle("-fx-background-color: #cc0000; -fx-text-fill: white;");
                    btnEliminar.setOnAction(e -> {
                        Usuario usuarioSeleccionado = getTableView().getItems().get(getIndex());
                        control.eliminarUsuarioEmpleado(usuarioSeleccionado);
                        actualizarTabla();
                    });
                }

                @Override
                protected void updateItem(String item, boolean empty) {
                    super.updateItem(item, empty);
                    if (!empty) {
                        setGraphic(btnEliminar);
                    } else {
                        setGraphic(null);
                    }
                }
            });

            tableUsuarios.getColumns().addAll(colNombre, colCorreo, colUsuario, colEliminar);
            tableUsuarios.setItems(dataUsuarios);

            VBox layout = new VBox(10, lblTitulo, formPane, tableUsuarios);
            layout.setPadding(new Insets(20));
            layout.setAlignment(Pos.CENTER);
            layout.setStyle("-fx-background-color: #eaf4f4;");

            Text footerText = new Text("©2025 Derechos Reservados - Sistema Sugerencias - NeoBit");
            footerText.setStyle("-fx-font-size: 0.9rem; -fx-fill: #006666;");
            layout.getChildren().add(footerText);

            Scene scene = new Scene(layout, 600, 550);
            stage.setScene(scene);
        }

        stage.show();
        actualizarTabla();
    }

    private void actualizarTabla() {
        dataUsuarios.clear();
        for (Usuario u : control.obtenerUsuarios()) {
            if (u.getRol() != null && u.getRol() == Rol.EMPLEADO) {
                dataUsuarios.add(u);
            }
        }
    }
}