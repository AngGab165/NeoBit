package com.neobit.sugerencia.datos;

import com.neobit.sugerencia.negocio.modelo.Empleado;
import com.neobit.sugerencia.negocio.modelo.Rol;
import com.neobit.sugerencia.negocio.modelo.Usuario;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

// Repositorio para la entidad Usuario
@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {

    Optional<Usuario> findByCorreoAndContrasena(String correo, String contrasena);

    Optional<Empleado> findEmpleadoByCorreo(String correo);

    Optional<Usuario> findByCorreo(String correo);

    boolean existsByCorreo(String correo);

    Usuario findByUsuario(String usuario);

    Optional<Usuario> findByUsuarioAndContrasena(String usuario, String contrasena);

    List<Usuario> findByRol(Rol empleado);
}