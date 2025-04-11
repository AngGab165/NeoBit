package com.neobit.sugerencia.negocio;

import com.neobit.sugerencia.negocio.modelo.Empleado;
import com.neobit.sugerencia.negocio.modelo.Notificaciones;
import com.neobit.sugerencia.datos.NotificacionesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class ServicioNotificaciones {

    @Autowired
    private NotificacionesRepository repository;

    @Autowired
    private ServicioEmpleado servicioEmpleado;
    public Notificaciones crearNotificacion(Long empleadoId, String tipo, String mensaje, LocalDateTime fecha, String estado) {
        Notificaciones notificacion = new Notificaciones();
        notificacion.setMensaje(mensaje);
        notificacion.setTipo(tipo);
        notificacion.setFecha(fecha);
        notificacion.setEstado(estado);
        
        if (empleadoId != null) {
            Empleado empleado = servicioEmpleado.encuentraEmpleadoPorId(empleadoId);
            if (empleado != null) {
                notificacion.setEmpleado(empleado);
            }
        }
        
        return repository.save(notificacion);
    }

    public List<Notificaciones> obtenerTodasLasNotificaciones() {
        return (List<Notificaciones>) repository.findAll();
    }

    public Optional<Notificaciones> obtenerNotificacionPorId(Long id) {
        return repository.findById(id);
    }

    public Notificaciones actualizarNotificacion(Long id, String mensaje, LocalDateTime fecha) {
        return repository.findById(id).map(notificacion -> {
            notificacion.setMensaje(mensaje);
            notificacion.setFecha(fecha);
            return repository.save(notificacion);
        }).orElseThrow(() -> new IllegalArgumentException("La notificación con ID " + id + " no existe."));
    }

    public void eliminarNotificacion(Long id) {
        if (repository.existsById(id)) {
            repository.deleteById(id); // Elimina la notificación si existe
        } else {
            throw new IllegalArgumentException("La notificación con ID " + id + " no existe.");
        }
    }

    public Notificaciones marcarComoLeida(Long id) {
        return repository.findById(id).map(notificacion -> {
            notificacion.setEstado("LEÍDA");
            return repository.save(notificacion);
        }).orElseThrow(() -> new IllegalArgumentException("Notificación no encontrada"));
    }
}