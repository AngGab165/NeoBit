package com.neobit.sugerencia.presentacion.sugerencia;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.neobit.sugerencia.negocio.ServicioSugerencia;
import com.neobit.sugerencia.negocio.modelo.Empleado;
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
    }

    /**
     * Agrega una nueva sugerencia
     * 
     * @param titulo              El título de la sugerencia
     * @param descripcion         La descripción breve de la sugerencia
     * @param autor               El autor de la sugerencia
     * @param ultimaActualizacion
     * @param fechaCreacion
     */
    public void agregaSugerencia(String titulo, String descripcion, String autor,
            LocalDate fechaCreacion, LocalDate ultimaActualizacion, Prioridad prioridad) {
        // Aquí se asigna el empleado al crear la sugerencia
        Sugerencia sugerencia = new Sugerencia(titulo, descripcion, autor, "Pendiente", "", fechaCreacion,
                ultimaActualizacion, prioridad); // Asignación del empleado
        servicioSugerencia.agregaSugerencia(sugerencia);
        ventana.actualizarTabla();
        inicia();
    }

    /**
     * Elimina una sugerencia
     * 
     * @param sugerencia La sugerencia a eliminar
     */
    public void eliminaSugerencia(Sugerencia sugerencia) {
        servicioSugerencia.eliminaSugerencia(sugerencia);
        inicia();
    }

    public List<Sugerencia> obtenerTodasLasSugerencias() {
        return servicioSugerencia.recuperaSugerencias();
    }
}