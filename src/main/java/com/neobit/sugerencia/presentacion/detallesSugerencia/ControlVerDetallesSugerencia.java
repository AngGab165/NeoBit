package com.neobit.sugerencia.presentacion.detallesSugerencia;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.neobit.sugerencia.negocio.ServicioComentario;
import com.neobit.sugerencia.negocio.modelo.Comentario;
import com.neobit.sugerencia.negocio.modelo.Sugerencia;

@Component
public class ControlVerDetallesSugerencia {

    @Autowired
    private ServicioComentario servicioComentario;

    @Autowired
    private VentanaVerDetallesSugerencia ventana;

    private Sugerencia sugerencia;

    /**
     * Inicia el caso de uso
     * 
     * @param sugerencia La sugerencia cuyos detalles se van a mostrar
     */
    public void inicia(Sugerencia sugerencia) {
        this.sugerencia = sugerencia;
        ventana.muestra(this, sugerencia);
    }

    /**
     * Agrega un nuevo comentario a la sugerencia
     * 
     * @param textoComentario El texto del nuevo comentario
     */
    public void agregarComentario(String textoComentario) {
        Comentario nuevoComentario = new Comentario();
        nuevoComentario.setTexto(textoComentario);
        nuevoComentario.setAutor("Usuario Actual"); // Reemplazar con el autor real
        nuevoComentario.setFecha("Fecha Actual"); // Reemplazar con la fecha actual

        servicioComentario.agregarComentario(sugerencia, nuevoComentario);
        sugerencia.getComentarios().add(nuevoComentario);
        ventana.muestra(this, sugerencia);
    }
}