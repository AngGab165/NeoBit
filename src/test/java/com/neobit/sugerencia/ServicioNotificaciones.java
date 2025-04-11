package com.neobit.sugerencia;

import com.neobit.sugerencia.negocio.ServicioEmpleado;
import com.neobit.sugerencia.negocio.ServicioNotificaciones;
import com.neobit.sugerencia.negocio.modelo.Empleado;
import com.neobit.sugerencia.negocio.modelo.Notificaciones;
import com.neobit.sugerencia.datos.NotificacionesRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ServicioNotificacionesTest {

    @Mock
    private NotificacionesRepository repository;

    @Mock
    private ServicioEmpleado servicioEmpleado;

    @InjectMocks
    private ServicioNotificaciones servicioNotificaciones;

    private Notificaciones notificacion;
    private Empleado empleado;
    private final LocalDateTime fechaActual = LocalDateTime.now();

    @BeforeEach
    void setUp() {
        empleado = new Empleado();
        empleado.setId(1L);
        empleado.setNombre("Juan Pérez");

        notificacion = new Notificaciones();
        notificacion.setId(1L);
        notificacion.setMensaje("Mensaje de prueba");
        notificacion.setTipo("ADMINISTRADOR");
        notificacion.setFecha(fechaActual);
        notificacion.setEstado("NO LEÍDA");
        notificacion.setEmpleado(empleado);
    }

    @Test
    void crearNotificacion_ConEmpleadoExistente_DevuelveNotificacionConEmpleado() {
        // Arrange
        when(servicioEmpleado.encuentraEmpleadoPorId(1L)).thenReturn(empleado);
        when(repository.save(any(Notificaciones.class))).thenReturn(notificacion);

        // Act
        Notificaciones resultado = servicioNotificaciones.crearNotificacion(
            1L, "ADMINISTRADOR", "Mensaje de prueba", fechaActual, "NO LEÍDA");

        // Assert
        assertNotNull(resultado);
        assertEquals(empleado, resultado.getEmpleado());
        verify(servicioEmpleado).encuentraEmpleadoPorId(1L);
        verify(repository).save(any(Notificaciones.class));
    }

    @Test
    void crearNotificacion_SinEmpleado_DevuelveNotificacionSinEmpleado() {
        // Arrange
        when(repository.save(any(Notificaciones.class))).thenReturn(notificacion);

        // Act
        Notificaciones resultado = servicioNotificaciones.crearNotificacion(
            null, "ADMINISTRADOR", "Mensaje de prueba", fechaActual, "NO LEÍDA");

        // Assert
        assertNotNull(resultado);
        assertNull(resultado.getEmpleado());
        verify(servicioEmpleado, never()).encuentraEmpleadoPorId(anyLong());
        verify(repository).save(any(Notificaciones.class));
    }

    @Test
    void obtenerTodasLasNotificaciones_DevuelveListaDeNotificaciones() {
        // Arrange
        List<Notificaciones> notificaciones = Arrays.asList(notificacion);
        when(repository.findAll()).thenReturn(notificaciones);

        // Act
        List<Notificaciones> resultado = servicioNotificaciones.obtenerTodasLasNotificaciones();

        // Assert
        assertEquals(1, resultado.size());
        verify(repository).findAll();
    }

    @Test
    void obtenerNotificacionPorId_NotificacionExistente_DevuelveNotificacion() {
        // Arrange
        when(repository.findById(1L)).thenReturn(Optional.of(notificacion));

        // Act
        Optional<Notificaciones> resultado = servicioNotificaciones.obtenerNotificacionPorId(1L);

        // Assert
        assertTrue(resultado.isPresent());
        assertEquals(notificacion, resultado.get());
        verify(repository).findById(1L);
    }

    @Test
    void obtenerNotificacionPorId_NotificacionNoExistente_DevuelveOptionalVacio() {
        // Arrange
        when(repository.findById(1L)).thenReturn(Optional.empty());

        // Act
        Optional<Notificaciones> resultado = servicioNotificaciones.obtenerNotificacionPorId(1L);

        // Assert
        assertTrue(resultado.isEmpty());
        verify(repository).findById(1L);
    }

    @Test
    void actualizarNotificacion_NotificacionExistente_ActualizaCampos() {
        // Arrange
        Notificaciones notificacionActualizada = new Notificaciones();
        notificacionActualizada.setId(1L);
        notificacionActualizada.setMensaje("Nuevo mensaje");
        notificacionActualizada.setFecha(fechaActual.plusDays(1));

        when(repository.findById(1L)).thenReturn(Optional.of(notificacion));
        when(repository.save(any(Notificaciones.class))).thenReturn(notificacionActualizada);

        // Act
        Notificaciones resultado = servicioNotificaciones.actualizarNotificacion(
            1L, "Nuevo mensaje", fechaActual.plusDays(1));

        // Assert
        assertEquals("Nuevo mensaje", resultado.getMensaje());
        assertEquals(fechaActual.plusDays(1), resultado.getFecha());
        verify(repository).findById(1L);
        verify(repository).save(any(Notificaciones.class));
    }

    @Test
    void actualizarNotificacion_NotificacionNoExistente_LanzaExcepcion() {
        // Arrange
        when(repository.findById(1L)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> {
            servicioNotificaciones.actualizarNotificacion(1L, "Nuevo mensaje", fechaActual);
        });
        verify(repository).findById(1L);
        verify(repository, never()).save(any(Notificaciones.class));
    }

    @Test
    void eliminarNotificacion_NotificacionExistente_EliminaCorrectamente() {
        // Arrange
        when(repository.existsById(1L)).thenReturn(true);

        // Act
        servicioNotificaciones.eliminarNotificacion(1L);

        // Assert
        verify(repository).existsById(1L);
        verify(repository).deleteById(1L);
    }

    @Test
    void eliminarNotificacion_NotificacionNoExistente_LanzaExcepcion() {
        // Arrange
        when(repository.existsById(1L)).thenReturn(false);

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> {
            servicioNotificaciones.eliminarNotificacion(1L);
        });
        verify(repository).existsById(1L);
        verify(repository, never()).deleteById(anyLong());
    }

    @Test
    void marcarComoLeida_NotificacionExistente_ActualizaEstado() {
        // Arrange
        Notificaciones notificacionLeida = new Notificaciones();
        notificacionLeida.setId(1L);
        notificacionLeida.setEstado("LEÍDA");

        when(repository.findById(1L)).thenReturn(Optional.of(notificacion));
        when(repository.save(any(Notificaciones.class))).thenReturn(notificacionLeida);

        // Act
        Notificaciones resultado = servicioNotificaciones.marcarComoLeida(1L);

        // Assert
        assertEquals("LEÍDA", resultado.getEstado());
        verify(repository).findById(1L);
        verify(repository).save(any(Notificaciones.class));
    }

    @Test
    void marcarComoLeida_NotificacionNoExistente_LanzaExcepcion() {
        // Arrange
        when(repository.findById(1L)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> {
            servicioNotificaciones.marcarComoLeida(1L);
        });
        verify(repository).findById(1L);
        verify(repository, never()).save(any(Notificaciones.class));
    }
}