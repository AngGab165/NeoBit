package com.neobit.sugerencia.presentacion.revisarSugerencias;

import com.neobit.sugerencia.negocio.ServicioSugerencia;
import com.neobit.sugerencia.negocio.modelo.Sugerencia;
import javafx.application.Platform;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

//si
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

    public void enviarRetroalimentacion(Long id, String retroalimentacion) {
        try {
            // Buscar la sugerencia por ID
            Sugerencia sugerencia = servicioSugerencia.buscaSugerenciaPorId(id);

            if (sugerencia == null) {
                mostrarAlerta("Error", "No se encontró la sugerencia con ID: " + id, AlertType.ERROR);
                return;
            }

            // Guardar la retroalimentación en la sugerencia
            sugerencia.setRetroalimentacion(retroalimentacion);

            // Actualizar la sugerencia en la base de datos
            servicioSugerencia.actualizaSugerencia(sugerencia);

            mostrarAlerta("Éxito", "Retroalimentación enviada correctamente.", AlertType.INFORMATION);
        } catch (Exception e) {
            mostrarAlerta("Error", "No se pudo enviar la retroalimentación: " + e.getMessage(), AlertType.ERROR);
        }
    }

    public void recomendarSugerencia(Long id) {
        try {
            Sugerencia sugerencia = servicioSugerencia.buscaSugerenciaPorId(id);

            if (sugerencia == null) {
                mostrarAlerta("Error", "No se encontró la sugerencia con ID: " + id, AlertType.ERROR);
                return;
            }

            // Aquí podrías marcar la sugerencia como recomendada en la BD si es necesario
            servicioSugerencia.recomendarSugerencia(id);

            // Enviar notificación al empleado
            servicioSugerencia.enviarNotificacion(sugerencia.getAutor(),
                    "Tu sugerencia '" + sugerencia.getTitulo() + "' ha sido recomendada.");

            mostrarAlerta("Éxito", "Sugerencia recomendada y notificación enviada.", AlertType.INFORMATION);

        } catch (Exception e) {
            mostrarAlerta("Error", "No se pudo recomendar la sugerencia: " + e.getMessage(), AlertType.ERROR);
        }
    }

}