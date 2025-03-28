package com.neobit.sugerencia.presentacion.notificaciones;

import com.neobit.sugerencia.negocio.modelo.Notificaciones;
import com.neobit.sugerencia.datos.NotificacionesRepository;
import com.neobit.sugerencia.negocio.ServicioNotificaciones;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

@Controller
public class ControlNotificaciones {

    @Autowired
    private ServicioNotificaciones servicio;

    @Autowired
    private NotificacionesRepository notificacionesRepository;

    public void agregaNotificacion(String mensaje, LocalDateTime fecha) {
        servicio.crearNotificacion(mensaje, fecha);
    }

    public List<Notificaciones> obtenerTodasLasNotificaciones() {
        return servicio.obtenerTodasLasNotificaciones();
    }

    public List<Notificaciones> obtenerNotificacionesPorEmpleado(Long idEmpleado) {
        // Aquí se asume que hay una relación en la base de datos entre 'Empleado' y
        // 'Notificaciones'.
        return notificacionesRepository.findByEmpleadoId(idEmpleado); // Esto depende de tu implementación.
    }

    public void eliminaNotificacion(Notificaciones notificacion) {
        servicio.eliminarNotificacion(notificacion.getId());
    }
}