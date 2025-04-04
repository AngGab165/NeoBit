package com.neobit.sugerencia.presentacion.usuarios;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import com.neobit.sugerencia.datos.UsuarioRepository;
import com.neobit.sugerencia.negocio.modelo.Usuario;
import com.neobit.sugerencia.negocio.modelo.Rol;

@Component
public class ControlUsuarioEmpleado {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private VentanaUsuarioEmpleado ventana;

    public void inicia() {
        ventana.muestra();
    }

    public void registrarUsuarioEmpleado(String nombre, String correo, String usuario, String contrasena) {
        // Validación de los parámetros
        if (nombre == null || nombre.isEmpty() || correo == null || correo.isEmpty() || usuario == null
                || usuario.isEmpty() || contrasena == null || contrasena.isEmpty()) {
            throw new IllegalArgumentException("Todos los campos son obligatorios.");
        }

        // Crear el objeto Usuario
        Usuario nuevoUsuario = new Usuario();
        nuevoUsuario.setNombre(nombre);
        nuevoUsuario.setCorreo(correo);
        nuevoUsuario.setUsuario(usuario.toLowerCase()); // Usar el parámetro 'usuario'

        // Asignar la contraseña tal como se recibe (sin encriptación)
        nuevoUsuario.setContrasena(contrasena); // Usar la contraseña tal cual se pasa al método

        nuevoUsuario.setRol(Rol.EMPLEADO); // Asignar el rol de EMPLEADO

        // Guardar el usuario en la base de datos
        usuarioRepository.save(nuevoUsuario);
    }

    public void eliminarUsuarioEmpleado(Usuario usuario) {
        usuarioRepository.delete(usuario); // Elimina el usuario de la base de datos
    }

    public List<Usuario> obtenerUsuarios() {
        return usuarioRepository.findAll(); // Recupera todos los usuarios de la base de datos
    }
}
