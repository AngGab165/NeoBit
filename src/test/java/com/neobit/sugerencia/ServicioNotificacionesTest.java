package com.neobit.sugerencia;

import com.neobit.sugerencia.negocio.ServicioNotificaciones;
import com.neobit.sugerencia.negocio.ServicioUsuario;
import com.neobit.sugerencia.negocio.modelo.Notificaciones;
import com.neobit.sugerencia.negocio.modelo.Usuario;
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
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ServicioNotificacionesTest {

    @Mock
    private NotificacionesRepository repository;

    @Mock
    private ServicioUsuario servicioUsuario;

    @InjectMocks
    private ServicioNotificaciones servicioNotificaciones;

    private Notificaciones notificacion;
    private Usuario usuario;
    private final LocalDateTime fechaActual = LocalDateTime.now();

    @BeforeEach
    void setUp() {
        usuario = new Usuario();
        usuario.setId(1L);
        usuario.setNombre("Juan Pérez");

        notificacion = new Notificaciones();
        notificacion.setId(1L);
        notificacion.setMensaje("Mensaje de prueba");
        notificacion.setTipo("ADMINISTRADOR");
        notificacion.setFecha(fechaActual);
        notificacion.setEstado("NO LEÍDA");
        notificacion.setUsuario(usuario);
    }

    @Test
    void crearNotificacion_ConUsuarioExistente_DevuelveNotificacionConUsuario() {
        when(servicioUsuario.obtenerUsuarioPorId(1L)).thenReturn(usuario);
        when(repository.save(any(Notificaciones.class))).thenReturn(notificacion);

        Notificaciones resultado = servicioNotificaciones.crearNotificacion(
                1L, "ADMINISTRADOR", "Mensaje de prueba", fechaActual, "NO LEÍDA");

        assertNotNull(resultado);
        assertEquals(usuario, resultado.getUsuario());
        verify(servicioUsuario).obtenerUsuarioPorId(1L);
        verify(repository).save(any(Notificaciones.class));
    }

    @Test
    void crearNotificacion_SinUsuario_DevuelveNotificacionSinUsuario() {
        // Arrange
        Notificaciones notificacionSinUsuario = new Notificaciones();
        notificacionSinUsuario.setId(1L);
        notificacionSinUsuario.setMensaje("Mensaje de prueba");
        notificacionSinUsuario.setTipo("ADMINISTRADOR");
        notificacionSinUsuario.setFecha(fechaActual);
        notificacionSinUsuario.setEstado("NO LEÍDA");
        notificacionSinUsuario.setUsuario(null); // Usuario explícitamente nulo

        when(repository.save(any(Notificaciones.class))).thenReturn(notificacionSinUsuario);

        // Act
        Notificaciones resultado = servicioNotificaciones.crearNotificacion(
                null, "ADMINISTRADOR", "Mensaje de prueba", fechaActual, "NO LEÍDA");

        // Assert
        assertNotNull(resultado);
        assertNull(resultado.getUsuario());
        verify(servicioUsuario, never()).obtenerUsuarioPorId(anyLong());
        verify(repository).save(any(Notificaciones.class));
    }

    @Test
    void obtenerTodasLasNotificaciones_DevuelveListaDeNotificaciones() {
        List<Notificaciones> notificaciones = Arrays.asList(notificacion);
        when(repository.findAll()).thenReturn(notificaciones);

        List<Notificaciones> resultado = servicioNotificaciones.obtenerTodasLasNotificaciones();

        assertEquals(1, resultado.size());
        verify(repository).findAll();
    }

    @Test
    void obtenerNotificacionPorId_NotificacionExistente_DevuelveNotificacion() {
        when(repository.findById(1L)).thenReturn(Optional.of(notificacion));

        Optional<Notificaciones> resultado = servicioNotificaciones.obtenerNotificacionPorId(1L);

        assertTrue(resultado.isPresent());
        assertEquals(notificacion, resultado.get());
        verify(repository).findById(1L);
    }

    @Test
    void obtenerNotificacionPorId_NotificacionNoExistente_DevuelveOptionalVacio() {
        when(repository.findById(1L)).thenReturn(Optional.empty());

        Optional<Notificaciones> resultado = servicioNotificaciones.obtenerNotificacionPorId(1L);

        assertTrue(resultado.isEmpty());
        verify(repository).findById(1L);
    }

    @Test
    void actualizarNotificacion_NotificacionExistente_ActualizaCampos() {
        Notificaciones notificacionActualizada = new Notificaciones();
        notificacionActualizada.setId(1L);
        notificacionActualizada.setMensaje("Nuevo mensaje");
        notificacionActualizada.setFecha(fechaActual.plusDays(1));

        when(repository.findById(1L)).thenReturn(Optional.of(notificacion));
        when(repository.save(any(Notificaciones.class))).thenReturn(notificacionActualizada);

        Notificaciones resultado = servicioNotificaciones.actualizarNotificacion(
                1L, "Nuevo mensaje", fechaActual.plusDays(1));

        assertEquals("Nuevo mensaje", resultado.getMensaje());
        assertEquals(fechaActual.plusDays(1), resultado.getFecha());
        verify(repository).findById(1L);
        verify(repository).save(any(Notificaciones.class));
    }

    @Test
    void actualizarNotificacion_NotificacionNoExistente_LanzaExcepcion() {
        when(repository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(IllegalArgumentException.class, () -> {
            servicioNotificaciones.actualizarNotificacion(1L, "Nuevo mensaje", fechaActual);
        });
        verify(repository).findById(1L);
        verify(repository, never()).save(any(Notificaciones.class));
    }

    @Test
    void eliminarNotificacion_NotificacionExistente_EliminaCorrectamente() {
        when(repository.existsById(1L)).thenReturn(true);

        servicioNotificaciones.eliminarNotificacion(1L);

        verify(repository).existsById(1L);
        verify(repository).deleteById(1L);
    }

    @Test
    void eliminarNotificacion_NotificacionNoExistente_LanzaExcepcion() {
        when(repository.existsById(1L)).thenReturn(false);

        assertThrows(IllegalArgumentException.class, () -> {
            servicioNotificaciones.eliminarNotificacion(1L);
        });
        verify(repository).existsById(1L);
        verify(repository, never()).deleteById(anyLong());
    }

    @Test
    void marcarComoLeida_NotificacionExistente_ActualizaEstado() {
        Notificaciones notificacionLeida = new Notificaciones();
        notificacionLeida.setId(1L);
        notificacionLeida.setEstado("LEÍDA");

        when(repository.findById(1L)).thenReturn(Optional.of(notificacion));
        when(repository.save(any(Notificaciones.class))).thenReturn(notificacionLeida);

        Notificaciones resultado = servicioNotificaciones.marcarComoLeida(1L);

        assertEquals("LEÍDA", resultado.getEstado());
        verify(repository).findById(1L);
        verify(repository).save(any(Notificaciones.class));
    }

    @Test
    void marcarComoLeida_NotificacionNoExistente_LanzaExcepcion() {
        when(repository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(IllegalArgumentException.class, () -> {
            servicioNotificaciones.marcarComoLeida(1L);
        });
        verify(repository).findById(1L);
        verify(repository, never()).save(any(Notificaciones.class));
    }
}
