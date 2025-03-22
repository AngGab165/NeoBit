package com.neobit.sugerencia;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.stage.Stage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.neobit.sugerencia.presentacion.principal.VentanaPrincipal;

@Component
public class JavaFXInitializer extends Application {

    @Autowired
    private VentanaPrincipal ventanaPrincipal;

    @Override
    public void start(Stage primaryStage) {
        Platform.runLater(() -> {
            ventanaPrincipal.start(primaryStage);
        });
    }

    public static void main(String[] args) {
        launch(args);
    }
}