package com.neobit.sugerencia.negocio.modelo;

import jakarta.persistence.*;
import java.util.Date;

@Entity
public class Notificaciones {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String mensaje;

    @Temporal(TemporalType.DATE) // Corrección para manejar solo la fecha sin hora
    private Date fecha;

    // Constructor vacío
    public Notificaciones() {
    }

    // Constructor completo
    public Notificaciones(String mensaje, Date fecha) {
        this.mensaje = mensaje;
        this.fecha = fecha;
    }

    // Getters y Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getMensaje() {
        return mensaje;
    }

    public void setMensaje(String mensaje) {
        this.mensaje = mensaje;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }
}