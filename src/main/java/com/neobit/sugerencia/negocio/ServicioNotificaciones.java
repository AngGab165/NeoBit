package com.neobit.sugerencia.negocio;

import com.neobit.sugerencia.negocio.modelo.Notificaciones;
import com.neobit.sugerencia.negocio.modelo.Empleado;
import com.neobit.sugerencia.datos.NotificacionesRepository;
import com.neobit.sugerencia.datos.EmpleadoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class ServicioNotificaciones {

    @Autowired
    private NotificacionesRepository repository;


    public Notificaciones crearNotificacion(String mensaje, LocalDateTime fecha) {
        Notificaciones notificacion = new Notificaciones(mensaje, fecha);

    @Autowired
    private EmpleadoRepository empleadoRepository; // Para obtener el Empleado

    public Notificaciones crearNotificacion(Long empleadoId, String tipo, String mensaje, Date fecha, String estado) {
        Notificaciones notificacion = new Notificaciones();
        
        // Buscar el empleado en la base de datos
        Empleado empleado = empleadoRepository.findById(empleadoId)
                .orElseThrow(() -> new IllegalArgumentException("Empleado no encontrado con ID: " + empleadoId));
        
        notificacion.setEmpleado(empleado); // Asignar el objeto Empleado
        notificacion.setTipo(tipo);
        notificacion.setMensaje(mensaje);
        notificacion.setFecha(fecha);
        notificacion.setEstado(estado);
        

        return repository.save(notificacion);
    }

    public List<Notificaciones> obtenerTodasLasNotificaciones() {
        return repository.findAll();
    }
    
    public List<Notificaciones> obtenerNotificacionesPorEmpleado(Long empleadoId) {
        return repository.findByEmpleado_Id(empleadoId); // Cambiado a buscar por Empleado
    }

    public Optional<Notificaciones> obtenerNotificacionPorId(Long id) {
        return repository.findById(id);
    }


    public Notificaciones actualizarNotificacion(Long id, String mensaje, LocalDateTime fecha) {

    public Notificaciones marcarComoLeida(Long id) {

        return repository.findById(id).map(notificacion -> {
            notificacion.setEstado("LEÍDA");
            return repository.save(notificacion);
        }).orElseThrow(() -> new IllegalArgumentException("Notificación no encontrada"));
    }

    public void eliminarNotificacion(Long id) {
        if (repository.existsById(id)) {
            repository.deleteById(id);
        } else {
            throw new IllegalArgumentException("La notificación con ID " + id + " no existe.");
        }
    }

    // Métodos específicos para los tipos de notificación
    public Notificaciones notificarSugerenciaAprobada(Long empleadoId, String mensaje) {
        return crearNotificacion(empleadoId, "APROBACIÓN", mensaje, new Date(), "NO LEÍDA");
    }

    public Notificaciones notificarSugerenciaRechazada(Long empleadoId, String mensaje) {
        return crearNotificacion(empleadoId, "RECHAZO", mensaje, new Date(), "NO LEÍDA");
    }

    public Notificaciones notificarImplementacionIniciada(Long empleadoId, String mensaje) {
        return crearNotificacion(empleadoId, "IMPLEMENTACIÓN", "Implementación iniciada: " + mensaje, new Date(), "NO LEÍDA");
    }

    public Notificaciones notificarImplementacionCompletada(Long empleadoId, String mensaje) {
        return crearNotificacion(empleadoId, "COMPLETADA", "Implementación completada: " + mensaje, new Date(), "NO LEÍDA");
    }
}
