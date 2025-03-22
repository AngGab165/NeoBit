package com.neobit.sugerencia.presentacion.notificaciones ;

import com.neobit.sugerencia.negocio.modelo.Notificaciones;
import com.neobit.sugerencia.negocio.ServicioNotificaciones;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import java.util.Date;
import java.util.List;

@Controller
public class ControlNotificaciones {

    @Autowired
    private ServicioNotificaciones servicio;

    public void agregaNotificacion(String mensaje, Date fecha) {
        servicio.crearNotificacion(mensaje, fecha);
    }

    public List<Notificaciones> obtenerTodasLasNotificaciones() {
        return servicio.obtenerTodasLasNotificaciones();
    }

    public void eliminaNotificacion(Notificaciones notificacion) {
        servicio.eliminarNotificacion(notificacion.getId());
    }
}