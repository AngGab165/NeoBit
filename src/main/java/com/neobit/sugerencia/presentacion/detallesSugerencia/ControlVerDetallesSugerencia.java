package com.neobit.sugerencia.presentacion.detallesSugerencia;

import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import com.neobit.sugerencia.negocio.ServicioComentario;
import com.neobit.sugerencia.negocio.modelo.Comentario;
import com.neobit.sugerencia.negocio.modelo.Sugerencia;
import com.neobit.sugerencia.presentacion.login.ControlLoginEmpleado;

import javafx.application.Platform;

@Component
public class ControlVerDetallesSugerencia {

    @Autowired
    private ServicioComentario servicioComentario;

    @Autowired
    @Lazy
    private VentanaVerDetallesSugerencia ventana;

    private Sugerencia sugerencia;

    private String nombreEmpleado;

    public void setNombreEmpleado(String nombreEmpleado) {
        this.nombreEmpleado = nombreEmpleado;
    }

    /**
     * Inicia el caso de uso
     * 
     * @param sugerencia La sugerencia cuyos detalles se van a mostrar
     */
    public void inicia(Sugerencia sugerencia) {
        this.sugerencia = sugerencia;
        ventana.setNombreEmpleado(nombreEmpleado);
        ventana.muestra(sugerencia);
    }

    /**
     * Agrega un nuevo comentario a la sugerencia
     * 
     * @param textoComentario El texto del nuevo comentario
     * @param nombreEmpleado  El nombre del empleado que realiza el comentario
     */
    public void agregarComentario(String textoComentario, String nombreEmpleadoParam) {
        if (sugerencia == null) {
            System.out.println("Error: No hay sugerencia seleccionada.");
            return;
        }

        try {
            Comentario comentario = new Comentario();
            comentario.setTexto(textoComentario);
            comentario.setFecha(LocalDateTime.now());
            comentario.setSugerencia(sugerencia);

            comentario.setAutor(this.nombreEmpleado != null ? this.nombreEmpleado : nombreEmpleadoParam);
            comentario.setAutor(nombreEmpleado);
            System.out.println("Autor asignado al comentario: " + nombreEmpleado);
            servicioComentario.guardar(comentario);

            System.out.println("Comentario guardado: " + comentario.getTexto() + " por " + comentario.getAutor());

            Platform.runLater(() -> {
                sugerencia.getComentarios().add(comentario);
                ventana.actualizarComentarios();
            });

        } catch (Exception ex) {
            ex.printStackTrace(); // Ver el error exacto
            System.out.println("Error al guardar el comentario: " + ex.getMessage());
        }
    }

    public String getNombreEmpleado() {
        return nombreEmpleado;
    }

    public void setSugerencia(Sugerencia sugerencia) {
        this.sugerencia = sugerencia;
    }
}