package com.neobit.sugerencia.negocio.modelo;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Enumerated;
import jakarta.persistence.EnumType;

@Entity
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String usuario;
    private String nombre;
    private String correo;
    private String contrasena;

    @OneToMany(mappedBy = "autor", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Sugerencia> sugerencias = new ArrayList<>();

    @Enumerated(EnumType.STRING) // Usamos un Enum para el rol
    private Rol rol;

    // Constructor vacío necesario para JPA
    public Usuario() {
    }

    // Constructor con parámetros
    public Usuario(String usuario, String nombre, String correo, String contrasena, Rol rol) {
        this.nombre = nombre;
        this.usuario = usuario;
        this.correo = correo;
        this.contrasena = contrasena;
        this.rol = rol;
    }

    // Getters y Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public String getContrasena() {
        return contrasena;
    }

    public void setContrasena(String contrasena) {
        this.contrasena = contrasena;
    }

    public Rol getRol() {
        return rol;
    }

    public void setRol(Rol rol) {
        this.rol = rol;
    }

    public void setClave(String nuevaContrasena) {
        this.contrasena = nuevaContrasena;
    }

    // Implementa el setter
    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    // Getter
    public String getUsuario() {
        return usuario;
    }
}