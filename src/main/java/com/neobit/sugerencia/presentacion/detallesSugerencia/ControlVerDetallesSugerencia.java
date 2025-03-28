package com.neobit.sugerencia.presentacion.detallesSugerencia;

import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import com.neobit.sugerencia.negocio.ServicioComentario;
import com.neobit.sugerencia.negocio.modelo.Comentario;
import com.neobit.sugerencia.negocio.modelo.Sugerencia;

import javafx.application.Platform;

@Component
public class ControlVerDetallesSugerencia {

    @Autowired
    private ServicioComentario servicioComentario;

    @Autowired
    @Lazy
    private VentanaVerDetallesSugerencia ventana;

    private Sugerencia sugerencia;

    /**
     * Inicia el caso de uso
     * 
     * @param sugerencia La sugerencia cuyos detalles se van a mostrar
     */
    public void inicia(Sugerencia sugerencia) {
        this.sugerencia = sugerencia;
        ventana.muestra(sugerencia);
    }

    /**
     * Agrega un nuevo comentario a la sugerencia
     * 
     * @param textoComentario El texto del nuevo comentario
     */
    public void agregarComentario(String textoComentario) {
        if (sugerencia == null) {
            System.out.println("Error: No hay sugerencia seleccionada.");
            return;
        }

        // Crear un nuevo comentario con solo el texto
        Comentario comentario = new Comentario();
        comentario.setTexto(textoComentario); // Solo se guarda el texto
        comentario.setFecha(LocalDateTime.now()); // Fecha actual
        comentario.setSugerencia(sugerencia); // Asociamos la sugerencia

        // Guardar el comentario en la base de datos
        servicioComentario.guardar(comentario);

        System.out.println("Comentario guardado: " + comentario.getTexto());

        // Actualizar la lista de comentarios en la UI
        Platform.runLater(() -> {
            sugerencia.getComentarios().add(comentario);
            // Suponiendo que `ventana` tiene un método `actualizarComentarios`
            ventana.actualizarComentarios();
        });
    }

}