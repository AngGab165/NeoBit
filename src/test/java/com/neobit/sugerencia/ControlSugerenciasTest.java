package com.neobit.sugerencia;

import com.neobit.sugerencia.negocio.ServicioSugerencia;
import com.neobit.sugerencia.negocio.modelo.Sugerencia;
import com.neobit.sugerencia.presentacion.sugerencia.ControlSugerencias;
import com.neobit.sugerencia.presentacion.sugerencia.VentanaSugerencias;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.mockito.Mockito.*;

class ControlSugerenciasTest {

    @Mock
    private ServicioSugerencia servicioSugerencia;

    @Mock
    private VentanaSugerencias ventanaSugerencias;

    @InjectMocks
    private ControlSugerencias controlSugerencias;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testActualizarContadorConSugerencias() {
        // Simular sugerencias recuperadas
        List<Sugerencia> sugerencias = Arrays.asList(
                new Sugerencia("Titulo1", "Descripcion1", "Autor1", "Pendiente", "", (java.time.LocalDate) null, (java.time.LocalDate) null, (com.neobit.sugerencia.negocio.modelo.Prioridad) null),
                new Sugerencia("Titulo2", "Descripcion2", "Autor2", "Pendiente", "", (java.time.LocalDate) null, (java.time.LocalDate) null, (com.neobit.sugerencia.negocio.modelo.Prioridad) null)
        );

        // Simular el comportamiento del servicio
        when(servicioSugerencia.recuperaSugerencias()).thenReturn(sugerencias);

        // Ejecutar el método
        controlSugerencias.inicia();

        // Verificar que se actualizó el contador en la ventana
        verify(ventanaSugerencias).actualizarContador(2);
    }

    @Test
    void testActualizarContadorSinSugerencias() {
        // Simular que no hay sugerencias
        when(servicioSugerencia.recuperaSugerencias()).thenReturn(Collections.emptyList());

        // Ejecutar el método
        controlSugerencias.inicia();

        // Verificar que se actualizó el contador en la ventana
        verify(ventanaSugerencias).actualizarContador(0);
    }
}