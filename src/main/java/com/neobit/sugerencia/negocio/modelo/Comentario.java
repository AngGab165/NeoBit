package com.neobit.sugerencia.negocio.modelo;

import java.time.LocalDateTime;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

/**
 * Entidad de negocio Comentario
 */
@Entity
public class Comentario {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String texto;
    private String autor;
    private LocalDateTime fecha;

    @ManyToOne
    @JoinColumn(name = "sugerencia_id")
    private Sugerencia sugerencia;

    // Constructor vacío
    public Comentario() {
    }

    // Getters y Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTexto() {
        return texto;
    }

    public void setTexto(String texto) {
        this.texto = texto;
    }

    public String getAutor() {
        return autor;
    }

    public void setAutor(String autor) {
        this.autor = autor;
    }

    public LocalDateTime getFecha() {
        return fecha;
    }

    public void setFecha(LocalDateTime fecha) {
        this.fecha = fecha;
    }

    public Sugerencia getSugerencia() {
        return sugerencia;
    }

    public void setSugerencia(Sugerencia sugerencia) {
        this.sugerencia = sugerencia;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Comentario other = (Comentario) obj;
        return id != null && id.equals(other.id);
    }

    @Override
    public int hashCode() {
        return (int) (31 * (id != null ? id.hashCode() : 0));
    }

    @Override
    public String toString() {
        return "Comentario [id=" + id + ", texto=" + texto + ", autor=" + autor + ", fecha=" + fecha + ", sugerencia="
                + sugerencia + "]";
    }
}