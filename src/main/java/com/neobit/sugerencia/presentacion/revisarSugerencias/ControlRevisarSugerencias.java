package com.neobit.sugerencia.presentacion.revisarSugerencias;

import com.neobit.sugerencia.negocio.ServicioSugerencia;
import com.neobit.sugerencia.negocio.modelo.Sugerencia;
import javafx.application.Platform;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ControlRevisarSugerencias {

    @Autowired
    private ServicioSugerencia servicioSugerencia;

    @Autowired
    private VentanaRevisarSugerencias ventanaRevisarSugerencias;

    /**
     * Inicia el caso de uso de revisión de sugerencias
     */
    public void inicia() {
        List<Sugerencia> sugerencias = servicioSugerencia.recuperaSugerencias();

        if (sugerencias == null || sugerencias.isEmpty()) {
            mostrarAlerta("Sin sugerencias", "No hay sugerencias para revisar en este momento.", AlertType.INFORMATION);
            return;
        }

        ventanaRevisarSugerencias.muestra(this, sugerencias);
    }

    /**
     * Cambia el estado de una sugerencia
     *
     * @param idSugerencia ID de la sugerencia a actualizar
     * @param nuevoEstado  Nuevo estado que se asignará a la sugerencia
     */
    public void cambiarEstadoSugerencia(Long idSugerencia, String nuevoEstado) {
        try {
            Sugerencia sugerencia = servicioSugerencia.buscaSugerenciaPorId(idSugerencia);

            if (sugerencia == null) {
                mostrarAlerta("Error", "No se encontró la sugerencia con ID: " + idSugerencia, AlertType.ERROR);
                return;
            }

            // Actualizar el estado
            sugerencia.setEstado(nuevoEstado);
            servicioSugerencia.actualizaSugerencia(sugerencia);

            mostrarAlerta("Éxito", "El estado se actualizó correctamente.", AlertType.INFORMATION);

            // Recargar las sugerencias actualizadas
            inicia();

        } catch (Exception e) {
            mostrarAlerta("Error", "No se pudo actualizar el estado: " + e.getMessage(), AlertType.ERROR);
        }
    }

    /**
     * Muestra una alerta en la interfaz gráfica
     *
     * @param titulo  Título de la alerta
     * @param mensaje Mensaje que se mostrará
     * @param tipo    Tipo de alerta (INFORMATION, ERROR, WARNING)
     */
    private void mostrarAlerta(String titulo, String mensaje, AlertType tipo) {
        Platform.runLater(() -> {
            Alert alerta = new Alert(tipo);
            alerta.setTitle(titulo);
            alerta.setHeaderText(null);
            alerta.setContentText(mensaje);
            alerta.showAndWait();
        });
    }

    public List<Sugerencia> obtenerTodasLasSugerencias() {
        return servicioSugerencia.recuperaSugerencias();
    }
}