package com.neobit.sugerencia.negocio.modelo;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.*;

@Entity
public class TemaForo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String titulo;
    private String autor;
    private LocalDate fechaCreacion;

    @OneToMany(mappedBy = "temaForo", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<RespuestaForo> respuestas = new ArrayList<>();

    public TemaForo() {
    }

    public TemaForo(String titulo, String autor, LocalDate fechaCreacion) {
        this.titulo = titulo;
        this.autor = autor;
        this.fechaCreacion = fechaCreacion;
    }

    public Long getId() {
        return id;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getAutor() {
        return autor;
    }

    public void setAutor(String autor) {
        this.autor = autor;
    }

    public LocalDate getFechaCreacion() {
        return fechaCreacion;
    }

    public void setFechaCreacion(LocalDate fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }

    public List<RespuestaForo> getRespuestas() {
        return respuestas;
    }

    public int getNumeroRespuestas() {
        return respuestas.size();
    }
}