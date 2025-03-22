package com.neobit.sugerencia.presentacion.principal;

import org.springframework.stereotype.Component;

import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

@Component
public class VentanaAdministrador {

    // Método que muestra la ventana de administrador con las opciones
    public void muestra(ControlAdministrador controlAdministrador) {
        Stage stage = new Stage();
        stage.setTitle("Ventana Administrador");

        // Crear los botones para las opciones
        Button btnRevisarSugerencias = new Button("Revisar Sugerencias");
        Button btnGestionarEmpleados = new Button("Gestionar Empleados");

        // Definir las acciones de los botones
        btnRevisarSugerencias.setOnAction(e -> controlAdministrador.muestraVentanaRevisarSugerencias());
        btnGestionarEmpleados.setOnAction(e -> controlAdministrador.muestraVentanaGestionEmpleados());

        // Organizar los botones en un VBox
        VBox vbox = new VBox(20, btnRevisarSugerencias, btnGestionarEmpleados);
        vbox.setStyle("-fx-padding: 20px; -fx-alignment: center;");

        // Crear la escena y establecerla en el escenario
        Scene scene = new Scene(vbox, 300, 200);
        stage.setScene(scene);
        stage.show();
    }

    public void muestra() {
        muestra(new ControlAdministrador());
    }
}
