package com.neobit.sugerencia.presentacion.notificaciones;

import com.neobit.sugerencia.negocio.modelo.Notificaciones;
import com.neobit.sugerencia.datos.NotificacionesRepository;
import com.neobit.sugerencia.negocio.ServicioNotificaciones;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import java.util.Date;
import java.util.List;

@Controller
public class ControlNotificaciones {

    @Autowired
    private ServicioNotificaciones servicio;
    private ServicioNotificaciones servicioNotificaciones;

    @Autowired
    private NotificacionesRepository notificacionesRepository;

    public void crearNotificacionEjemplo() {
        String tipo = "APROBADA";
        String mensaje = "Sugerencia aprobada";
        Date fecha = new Date();
        String estado = "NO LEÍDA";
    
        servicioNotificaciones.crearNotificacion(null, tipo, mensaje, fecha, estado);
    }
    
    public void agregaNotificacion(String mensaje, Date fecha, Long empleadoId) {
        servicioNotificaciones.crearNotificacion(
            empleadoId,
            "ADMINISTRADOR", // Tipo de notificación
            mensaje,
            fecha,
            "NO LEÍDA"
        );
    }

    public void marcarComoLeida(Long idNotificacion) {
        servicioNotificaciones.marcarComoLeida(idNotificacion);
    }

    public List<Notificaciones> obtenerTodasLasNotificaciones() {
        return servicio.obtenerTodasLasNotificaciones();
    }

    public List<Notificaciones> obtenerNotificacionesPorEmpleado(Long idEmpleado) {
        // Aquí se asume que hay una relación en la base de datos entre 'Empleado' y
        // 'Notificaciones'.
        return notificacionesRepository.findByEmpleado_Id(idEmpleado); // Esto depende de tu implementación.
    }

    public void eliminaNotificacion(Notificaciones notificacion) {
        servicio.eliminarNotificacion(notificacion.getId());
        servicioNotificaciones.eliminarNotificacion(notificacion.getId());
    }
}