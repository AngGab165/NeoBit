package com.neobit.sugerencia.presentacion.sugerencia;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.neobit.sugerencia.negocio.ServicioSugerencia;
import com.neobit.sugerencia.negocio.modelo.Prioridad;
import com.neobit.sugerencia.negocio.modelo.Sugerencia;
import java.time.LocalDate;
import java.util.List;

@Component
public class ControlSugerencias {

    @Autowired
    private ServicioSugerencia servicioSugerencia;

    @Autowired
    private VentanaSugerencias ventana;

    /**
     * Inicia el caso de uso
     */
    public void inicia() {
        List<Sugerencia> sugerencias = servicioSugerencia.recuperaSugerencias();
        ventana.muestra(this, sugerencias);
        actualizarContador(sugerencias.size()); // Actualiza el contador al iniciar
    }

    /**
     * Agrega una nueva sugerencia
     * 
     * @param titulo              El título de la sugerencia
     * @param descripcion         La descripción breve de la sugerencia
     * @param autor               El autor de la sugerencia
     * @param fechaCreacion       La fecha de creación de la sugerencia
     * @param ultimaActualizacion La última fecha de actualización de la sugerencia
     * @param prioridad           La prioridad de la sugerencia
     */
    public void agregaSugerencia(String titulo, String descripcion, String autor,
            LocalDate fechaCreacion, LocalDate ultimaActualizacion, Prioridad prioridad) {
        Sugerencia sugerencia = new Sugerencia(titulo, descripcion, autor, "Pendiente", "", fechaCreacion,
                ultimaActualizacion, prioridad);
        servicioSugerencia.agregaSugerencia(sugerencia);
        ventana.actualizarTabla();
        inicia(); // Recarga la tabla y el contador
    }

    /**
     * Elimina una sugerencia
     * 
     * @param sugerencia La sugerencia a eliminar
     */
    public void eliminaSugerencia(Sugerencia sugerencia) {
        servicioSugerencia.eliminaSugerencia(sugerencia);
        inicia(); // Recarga la tabla y el contador
    }

    /**
     * Obtiene todas las sugerencias
     * 
     * @return Lista de sugerencias
     */
    public List<Sugerencia> obtenerTodasLasSugerencias() {
        return servicioSugerencia.recuperaSugerencias();
    }

    /**
     * Actualiza el contador de sugerencias en la ventana
     * 
     * @param total El número total de sugerencias
     */
    private void actualizarContador(int total) {
        ventana.actualizarContador(total); // Llama al método de la ventana para actualizar el contador
    }
}