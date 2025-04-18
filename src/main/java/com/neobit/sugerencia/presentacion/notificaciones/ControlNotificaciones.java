package com.neobit.sugerencia.presentacion.notificaciones;

import com.neobit.sugerencia.negocio.modelo.Notificaciones;
import com.neobit.sugerencia.datos.NotificacionesRepository;
import com.neobit.sugerencia.negocio.ServicioNotificaciones;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import java.time.LocalDateTime;
import java.util.List;

@Controller
public class ControlNotificaciones {

    @Autowired
    private ServicioNotificaciones servicio;
    private ServicioNotificaciones servicioNotificaciones;

    @Autowired
    private NotificacionesRepository notificacionesRepository;

    // Método modificado para aceptar lista de empleados
    public void enviarNotificaciones(List<Long> idsUsuarios, String mensaje, LocalDateTime fecha) {
        for (Long idUsuario : idsUsuarios) {
            try {
                servicio.crearNotificacion(
                        idUsuario,
                        "ADMINISTRADOR", // Esto setea el campo 'tipo'
                        mensaje,
                        fecha,
                        "NO LEÍDA");
            } catch (Exception e) {
                System.err.println("Error al enviar notificación al usuario ID: " + idUsuario);
                e.printStackTrace();
            }
        }
    }

    public void crearNotificacionEjemplo() {
        String tipo = "APROBADA";
        String mensaje = "Sugerencia aprobada";
        LocalDateTime fecha = LocalDateTime.now();
        String estado = "NO LEÍDA";

        servicioNotificaciones.crearNotificacion(null, tipo, mensaje, fecha, estado);
    }

    public void agregaNotificacion(String mensaje, LocalDateTime fecha, Long empleadoId) {
        servicioNotificaciones.crearNotificacion(
                empleadoId,
                "ADMINISTRADOR", // Tipo de notificación
                mensaje,
                fecha,
                "NO LEÍDA");
    }

    public void marcarComoLeida(Long idNotificacion) {
        servicioNotificaciones.marcarComoLeida(idNotificacion);
    }

    public List<Notificaciones> obtenerTodasLasNotificaciones() {
        return servicio.obtenerTodasLasNotificaciones();
    }

    public List<Notificaciones> obtenerNotificacionesPorUsuario(Long usuarioId) {
        return servicio.obtenerNotificacionesPorUsuario(usuarioId); // Cambiado a usuarioId
    }

    public void eliminaNotificacion(Notificaciones notificacion) {
        servicio.eliminarNotificacion(notificacion.getId());
        servicioNotificaciones.eliminarNotificacion(notificacion.getId());
    }
}
