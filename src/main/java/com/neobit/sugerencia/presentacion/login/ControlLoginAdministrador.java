package com.neobit.sugerencia.presentacion.login;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Controller;

import com.neobit.sugerencia.negocio.ServicioUsuario;
import com.neobit.sugerencia.negocio.modelo.Usuario;
import com.neobit.sugerencia.negocio.modelo.Rol;

import javafx.stage.Stage;

@Controller
public class ControlLoginAdministrador {

    @Autowired
    @Lazy
    private ServicioUsuario servicioUsuario; // Servicio que maneja el registro y operaciones de usuario

    @Autowired
    @Lazy
    private VentanaRegistroAdministrador ventanaRegistroAdministrador; // Ventana de registro que muestra alertas

    /**
     * Registra un nuevo administrador en el sistema.
     * 
     * @param usuario    Nombre de usuario del administrador
     * @param nombre     Nombre completo del administrador
     * @param correo     Correo electrónico del administrador
     * @param contrasena Contraseña para el nuevo administrador
     * @param stage      La ventana actual que se cierra después de registrar al
     *                   administrador
     */
    public void registrarAdministrador(String usuario, String nombre, String correo, String contrasena, Stage stage) {
        if (!validarCamposVacios(usuario, nombre, correo, contrasena)) {
            // Mostrar mensaje de error si algún campo está vacío
            ventanaRegistroAdministrador.mostrarAlerta("Error", "Todos los campos son obligatorios.");
            return;
        }

        // Crear nuevo objeto Usuario con Rol de ADMINISTRADOR
        Usuario nuevoAdministrador = new Usuario();
        nuevoAdministrador.setUsuario(usuario); // Asignar el nombre de usuario
        nuevoAdministrador.setNombre(nombre); // Asignar nombre completo
        nuevoAdministrador.setCorreo(correo); // Asignar correo
        nuevoAdministrador.setContrasena(contrasena); // Asignar contraseña
        nuevoAdministrador.setRol(Rol.ADMINISTRADOR); // Asignar rol ADMINISTRADOR

        try {
            // Llamar al servicio para guardar el usuario
            servicioUsuario.registrarUsuario(nuevoAdministrador);

            // Mostrar mensaje de éxito
            ventanaRegistroAdministrador.mostrarAlerta("Registro Exitoso", "El administrador ha sido registrado.");
            stage.close(); // Cerrar la ventana de registro
        } catch (Exception e) {
            // Mostrar mensaje de error si hubo un problema al registrar
            ventanaRegistroAdministrador.mostrarAlerta("Error", "Hubo un problema al registrar al administrador.");
        }
    }

    /**
     * Valida que los campos de registro no estén vacíos.
     * 
     * @param usuario    Nombre de usuario del administrador
     * @param nombre     Nombre completo del administrador
     * @param correo     Correo electrónico del administrador
     * @param contrasena Contraseña del administrador
     * @return true si todos los campos son válidos, false si alguno está vacío
     */
    private boolean validarCamposVacios(String usuario, String nombre, String correo, String contrasena) {
        return !(usuario == null || usuario.trim().isEmpty() || nombre == null || nombre.trim().isEmpty()
                || correo == null || correo.trim().isEmpty() || contrasena == null || contrasena.trim().isEmpty());
    }
}
