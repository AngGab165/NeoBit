package com.neobit.sugerencia.presentacion.listarSugerencias;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.neobit.sugerencia.negocio.ServicioSugerencia;
import com.neobit.sugerencia.negocio.modelo.Sugerencia;
import com.neobit.sugerencia.presentacion.detallesSugerencia.ControlVerDetallesSugerencia;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Component
public class ControlListarSugerencias {

    private static final Logger log = LoggerFactory.getLogger(ControlListarSugerencias.class);

    @Autowired
    private ServicioSugerencia servicioSugerencia;

    @Autowired
    private VentanaListarSugerencias ventana;

    @Autowired
    private ControlVerDetallesSugerencia controlVerDetallesSugerencia;

    /**
     * Inicia el caso de uso
     */
    public void inicia() {
        List<Sugerencia> sugerencias = servicioSugerencia.recuperaSugerencias();

        for (Sugerencia sugerencia : sugerencias) {
            log.info("sugerencia: " + sugerencia);
        }

        ventana.muestra(this, sugerencias);
    }

    /**
     * Muestra los detalles de la sugerencia seleccionada
     * 
     * @param sugerencia La sugerencia seleccionada
     */
    public void muestraDetallesSugerencia(Sugerencia sugerencia) {
        controlVerDetallesSugerencia.inicia(sugerencia);
    }
}