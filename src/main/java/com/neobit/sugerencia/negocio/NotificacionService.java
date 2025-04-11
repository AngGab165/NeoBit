package com.neobit.sugerencia.negocio;

import javafx.application.Platform;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import org.springframework.stereotype.Service;

@Service
public class NotificacionService {

    public void enviarNotificacion(String mensaje) {
        // Ejecutar en el hilo de JavaFX
        Platform.runLater(() -> {
            Alert alerta = new Alert(AlertType.INFORMATION);
            alerta.setTitle("Notificación");
            alerta.setHeaderText(null);
            alerta.setContentText(mensaje);
            alerta.showAndWait();
        });
    }
}