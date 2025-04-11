package com.neobit.sugerencia.negocio;

import com.neobit.sugerencia.datos.RespuestaForoRepository;
import com.neobit.sugerencia.datos.TemaForoRepository;
import com.neobit.sugerencia.negocio.modelo.RespuestaForo;
import com.neobit.sugerencia.negocio.modelo.TemaForo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class ForoServiceTest {

    @Mock
    private RespuestaForoRepository respuestaForoRepository;

    @Mock
    private TemaForoRepository temaForoRepository;

    @Mock
    private NotificacionService notificacionService;

    @InjectMocks
    private ForoService foroService;

    private TemaForo tema;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        tema = new TemaForo("Título del Tema", "Autor del Tema", LocalDate.now());
    }

    @Test
    void testAgregarRespuesta() {
        // Configurar datos simulados
        RespuestaForo respuesta = new RespuestaForo("Administrador Admin", "Contenido de prueba", LocalDate.now(), tema);
        when(respuestaForoRepository.save(any(RespuestaForo.class))).thenReturn(respuesta);

        // Llamar al método
        RespuestaForo resultado = foroService.agregarRespuesta(tema, "Admin", "Contenido de prueba");

        // Verificar resultados
        assertEquals("Administrador Admin", resultado.getAutor());
        assertEquals("Contenido de prueba", resultado.getContenido());

        // Verificar interacciones con los repositorios
        verify(respuestaForoRepository, times(1)).save(any(RespuestaForo.class));
        verify(temaForoRepository, times(1)).save(tema);
        verify(notificacionService, times(1)).enviarNotificacion(anyString());
    }
}