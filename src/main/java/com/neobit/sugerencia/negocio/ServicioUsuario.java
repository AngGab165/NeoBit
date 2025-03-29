package com.neobit.sugerencia.negocio;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.neobit.sugerencia.datos.UsuarioRepository;
import com.neobit.sugerencia.negocio.modelo.Rol;
import com.neobit.sugerencia.negocio.modelo.Usuario;

import java.util.Optional;

@Service
public class ServicioUsuario {

    @Autowired
    private UsuarioRepository usuarioRepository;

    /**
     * Valida las credenciales de un usuario.
     *
     * @param correo     El correo del usuario.
     * @param contraseña La contraseña del usuario.
     * @return El usuario si las credenciales son válidas, o null si no lo son.
     */
    public Usuario validarLogin(String usuario, String contrasena) {
        Optional<Usuario> usuarioOpt = usuarioRepository.findByUsuarioAndContrasena(usuario, contrasena);
        return usuarioOpt.orElse(null); // Devuelve el usuario si existe, o null si no
    }

    /**
     * Verifica si un usuario es administrador.
     *
     * @param usuario El usuario a verificar.
     * @return true si el usuario es administrador, false en caso contrario.
     */
    public boolean esAdministrador(Usuario usuario) {
        return usuario != null && usuario.getRol() == Rol.ADMINISTRADOR;
    }

    /**
     * Verifica si un usuario es empleado.
     *
     * @param usuario El usuario a verificar.
     * @return true si el usuario es empleado, false en caso contrario.
     */
    public boolean esEmpleado(Usuario usuario) {
        return usuario != null && usuario.getRol() == Rol.EMPLEADO;
    }

    /**
     * Obtiene un usuario por su correo.
     *
     * @param correo El correo del usuario.
     * @return El usuario si existe, o null si no.
     */
    public Usuario obtenerUsuarioPorCorreo(String correo) {
        return usuarioRepository.findByCorreo(correo).orElse(null);
    }

    /**
     * Registra un nuevo usuario.
     *
     * @param usuario El usuario a registrar.
     */
    public void registrarUsuario(Usuario usuario) {
        usuarioRepository.save(usuario);
    }

    public boolean existeCorreo(String correo) {
        return usuarioRepository.existsByCorreo(correo);
    }

    public Usuario buscarUsuarioPorNombre(String usuario) {
        // Aquí deberías consultar la BD con una consulta SQL real
        return usuarioRepository.findByUsuario(usuario); // Simulación
    }

    public boolean existeUsuario(String usuario) {
        return buscarUsuarioPorNombre(usuario) != null;
    }
}