package com.neobit.sugerencia;

import com.neobit.sugerencia.datos.NotificacionesRepository;
import com.neobit.sugerencia.negocio.ServicioNotificaciones;
import com.neobit.sugerencia.negocio.modelo.Notificaciones;
import com.neobit.sugerencia.presentacion.notificaciones.ControlNotificaciones;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

public class ControlNotificacionesTest {

    @InjectMocks
    private ControlNotificaciones control;

    @Mock
    private ServicioNotificaciones servicioNotificaciones;

    @Mock
    private ServicioNotificaciones servicio;

    @Mock
    private NotificacionesRepository notificacionesRepository;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testEnviarNotificaciones() {
        List<Long> empleados = Arrays.asList(1L, 2L);
        String mensaje = "Hola";
        LocalDateTime fecha = LocalDateTime.now();

        control.enviarNotificaciones(empleados, mensaje, fecha);

        verify(servicio, times(2)).crearNotificacion(anyLong(), eq("ADMINISTRADOR"), eq(mensaje), eq(fecha), eq("NO LEÍDA"));
    }

    @Test
    public void testCrearNotificacionEjemplo() {
        control.crearNotificacionEjemplo();
        verify(servicioNotificaciones).crearNotificacion(null, "APROBADA", "Sugerencia aprobada", any(), eq("NO LEÍDA"));
    }

    @Test
    public void testAgregaNotificacion() {
        Long empleadoId = 1L;
        String mensaje = "Mensaje";
        LocalDateTime fecha = LocalDateTime.now();

        control.agregaNotificacion(mensaje, fecha, empleadoId);

        verify(servicioNotificaciones).crearNotificacion(
                eq(empleadoId),
                eq("ADMINISTRADOR"),
                eq(mensaje),
                eq(fecha),
                eq("NO LEÍDA")
        );
    }

    @Test
    public void testMarcarComoLeida() {
        Long id = 5L;
        control.marcarComoLeida(id);
        verify(servicioNotificaciones).marcarComoLeida(id);
    }

    @Test
    public void testObtenerTodasLasNotificaciones() {
        List<Notificaciones> notificaciones = Collections.singletonList(new Notificaciones());
        when(servicio.obtenerTodasLasNotificaciones()).thenReturn(notificaciones);

        List<Notificaciones> resultado = control.obtenerTodasLasNotificaciones();
        assertEquals(1, resultado.size());
    }

    @Test
    public void testObtenerNotificacionesPorEmpleado() {
        Long idEmpleado = 2L;
        List<Notificaciones> notificaciones = Collections.singletonList(new Notificaciones());
        when(notificacionesRepository.findByEmpleadoId(idEmpleado)).thenReturn(notificaciones);

        List<Notificaciones> resultado = control.obtenerNotificacionesPorEmpleado(idEmpleado);
        assertEquals(1, resultado.size());
    }

    @Test
    public void testEliminarNotificacion() {
        Notificaciones notif = new Notificaciones();
        notif.setId(10L);

        control.eliminaNotificacion(notif);

        verify(servicio).eliminarNotificacion(10L);
        verify(servicioNotificaciones).eliminarNotificacion(10L);
    }
}
