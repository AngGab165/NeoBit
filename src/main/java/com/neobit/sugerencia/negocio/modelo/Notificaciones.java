package com.neobit.sugerencia.negocio.modelo;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
public class Notificaciones {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;  // Esto es la clave primaria correcta

    private String mensaje;
    private String estado;
    private String tipo;

    // Relación con Empleado (destinatario)
    @ManyToOne
    @JoinColumn(name = "empleado_id", nullable = false) // Enlace correcto con la FK
    private Empleado empleado;


    private String destinatario;

    private LocalDateTime fecha;

    @Temporal(TemporalType.TIMESTAMP)
    private Date fecha;


    // Constructor vacío
    public Notificaciones() {
    }

    // Constructor completo

    public Notificaciones(String mensaje, LocalDateTime fecha) {

    public Notificaciones(String mensaje, Date fecha, Empleado empleado) {

        this.mensaje = mensaje;
        this.fecha = fecha;
        this.empleado = empleado;
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

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public Date getFecha() {

        return fecha;
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
}

    public Empleado getEmpleado() {
        return empleado;
    }

    public void setEmpleado(Empleado empleado) {
        this.empleado = empleado;
    }
}


