package com.neobit.sugerencia.negocio.modelo;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
public class Notificaciones {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // Esto es la clave primaria correcta

    private String mensaje;
    private String estado;
    private String tipo;

    private String destinatario;

    private LocalDateTime fecha;
    @ManyToOne
    @JoinColumn(name = "usuario_id", nullable = true)
    private Usuario usuario;

    // Constructor vacío
    public Notificaciones() {
    }

    public Notificaciones(String mensaje, LocalDateTime fecha, Empleado empleado) {
        this.mensaje = mensaje;
        this.fecha = fecha;
        this.usuario = usuario;
    }

    // Getters y Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public String getMensaje() {
        return mensaje;
    }

    public void setMensaje(String mensaje) {
        this.mensaje = mensaje;
    }

    public LocalDateTime getFecha() {
        return fecha;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public void setFecha(LocalDateTime fecha) {
        this.fecha = fecha;
    }

    public String getDestinatario() {
        return destinatario;
    }

    public void setDestinatario(String autor) {
        this.destinatario = autor;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

}
