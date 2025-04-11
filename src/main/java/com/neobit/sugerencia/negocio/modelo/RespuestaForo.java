package com.neobit.sugerencia.negocio.modelo;

import java.time.LocalDate;

import jakarta.persistence.*;

@Entity
public class RespuestaForo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String administrador; // Cambiado de "autor" a "administrador"
    private String contenido;
    private LocalDate fechaRespuesta;

    @ManyToOne
    @JoinColumn(name = "tema_foro_id", nullable = false)
    private TemaForo temaForo;

    public RespuestaForo() {
    }

    public RespuestaForo(String administrador, String contenido, LocalDate fechaRespuesta, TemaForo temaForo) {
        this.administrador = administrador;
        this.contenido = contenido;
        this.fechaRespuesta = fechaRespuesta;
        this.temaForo = temaForo;
    }

    public Long getId() {
        return id;
    }

    public String getAdministrador() {
        return administrador;
    }

    public void setAdministrador(String administrador) {
        this.administrador = administrador;
    }

    public String getContenido() {
        return contenido;
    }

    public void setContenido(String contenido) {
        this.contenido = contenido;
    }

    public LocalDate getFechaRespuesta() {
        return fechaRespuesta;
    }

    public void setFechaRespuesta(LocalDate fechaRespuesta) {
        this.fechaRespuesta = fechaRespuesta;
    }

    public TemaForo getTemaForo() {
        return temaForo;
    }

    public void setTemaForo(TemaForo temaForo) {
        this.temaForo = temaForo;
    }
}