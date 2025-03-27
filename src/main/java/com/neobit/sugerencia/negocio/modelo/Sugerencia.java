package com.neobit.sugerencia.negocio.modelo;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.*;

@Entity
public class Sugerencia {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String titulo;
    private String descripcionBreve;
    private String autor;
    private String estado;
    private String estadoAnterior;

    @Column(name = "FECHA_CREACION")
    private LocalDate fechaCreacion;
    @Column(name = "ULTIMA_ACTUALIZACION")
    private LocalDate ultimaActualizacion;

    @OneToMany(mappedBy = "sugerencia", fetch = FetchType.EAGER, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Comentario> comentarios = new ArrayList<>();

    public Sugerencia() {
    }

    // Constructor completo
    public Sugerencia(String titulo, String descripcionBreve, String autor, String estado,
            String estadoAnterior, LocalDate fechaCreacion, LocalDate ultimaActualizacion) {
        this.titulo = titulo;
        this.descripcionBreve = descripcionBreve;
        this.autor = autor;
        this.estado = estado;
        this.estadoAnterior = estadoAnterior;
        this.fechaCreacion = fechaCreacion;
        this.ultimaActualizacion = ultimaActualizacion;
    }

    public Sugerencia(String titulo, String descripcionBreve, String autor, String estado, String estadoAnterior,
            String fechaCreacion, String ultimaActualizacion, Object comentarios) {
        this.titulo = titulo;
        this.descripcionBreve = descripcionBreve;
        this.autor = autor;
        this.estado = estado;
        this.estadoAnterior = estadoAnterior;

        // Asignación de fechas. Convertir las fechas de String a LocalDate
        this.fechaCreacion = LocalDate.parse(fechaCreacion); // Suponiendo que las fechas se pasan en formato ISO
                                                             // (yyyy-MM-dd)
        this.ultimaActualizacion = LocalDate.parse(ultimaActualizacion); // Similar a la fecha de creación

        // Empleado y comentarios se deben asignar correctamente dependiendo de su tipo.
        // Aquí supongo que son objetos.
        // Estos deberían ser instancias válidas, por lo que se haría algo como esto:

        this.comentarios = (List<Comentario>) comentarios; // Asegúrate de que el objeto comentarios sea una lista de
                                                           // Comentario
    }

    // Getters y Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getDescripcionBreve() {
        return descripcionBreve;
    }

    public void setDescripcionBreve(String descripcionBreve) {
        this.descripcionBreve = descripcionBreve;
    }

    public String getAutor() {
        return autor;
    }

    public void setAutor(String autor) {
        this.autor = autor;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estadoAnterior = this.estado;
        this.estado = estado;
        this.ultimaActualizacion = LocalDate.now();
    }

    public String getEstadoAnterior() {
        return estadoAnterior;
    }

    public void setEstadoAnterior(String estadoAnterior) {
        this.estadoAnterior = estadoAnterior;
    }

    public LocalDate getFechaCreacion() {
        return fechaCreacion;
    }

    public void setFechaCreacion(LocalDate fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }

    public LocalDate getUltimaActualizacion() {
        return ultimaActualizacion;
    }

    public void setUltimaActualizacion(LocalDate ultimaActualizacion) {
        this.ultimaActualizacion = ultimaActualizacion;
    }

    public List<Comentario> getComentarios() {
        return comentarios;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null || getClass() != obj.getClass())
            return false;
        Sugerencia other = (Sugerencia) obj;
        return id != null && id.equals(other.id);
    }

    @Override
    public int hashCode() {
        return (int) (31 * (id != null ? id.hashCode() : 0));
    }

    @Override
    public String toString() {
        return "Sugerencia [id=" + id + ", titulo=" + titulo + ", autor=" + autor
                + ", estado=" + estado + ", comentarios=" + comentarios.size() + "]";
    }
}