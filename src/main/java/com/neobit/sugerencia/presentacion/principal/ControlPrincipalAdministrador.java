package com.neobit.sugerencia.presentacion.principal;

import com.neobit.sugerencia.negocio.modelo.Rol;
import com.neobit.sugerencia.negocio.modelo.Usuario;
import com.neobit.sugerencia.presentacion.login.VentanaLoginAdministrador;
import com.neobit.sugerencia.presentacion.notificaciones.ControlNotificaciones;
import com.neobit.sugerencia.presentacion.notificaciones.VentanaNotificacionesAdministrador;
import com.neobit.sugerencia.presentacion.revisarSugerencias.VentanaRevisarSugerencias;
import com.neobit.sugerencia.presentacion.empleados.ControlEmpleados;
import com.neobit.sugerencia.presentacion.empleados.VentanaEmpleados;
import com.neobit.sugerencia.negocio.ServicioUsuario; // Asegúrate de que el servicio de usuario esté importado

import javafx.stage.Stage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

@Component
public class ControlPrincipalAdministrador {

    private final ControlNotificaciones controlNotificaciones;
    @Autowired
    @Lazy
    private VentanaPrincipalAdministrador ventanaPrincipalAdministrador;

    @Autowired
    @Lazy
    private VentanaEmpleados ventanaEmpleados; // Ventana para gestionar empleados

    @Autowired
    private ControlEmpleados controlEmpleados;

    @Autowired
    @Lazy
    private VentanaRevisarSugerencias ventanaRevisarSugerencias; // Ventana para revisar sugerencias

    @Autowired
    private VentanaNotificacionesAdministrador ventanaNotificaciones;

    @Autowired
    @Lazy
    private VentanaLoginAdministrador ventanaLoginAdministrador; // Ventana de login administrador

    @Autowired
    private ServicioUsuario servicioUsuario; // Servicio para manejar el registro de usuarios

    @Autowired
    public ControlPrincipalAdministrador(ControlNotificaciones controlNotificaciones,
            @Lazy VentanaNotificacionesAdministrador ventanaNotificacionesAdministrador) {
        this.controlNotificaciones = controlNotificaciones;
        this.ventanaNotificaciones = ventanaNotificaciones;
    }

    // Inicia la ventana principal del administrador
    public void inicia() {
        ventanaPrincipalAdministrador.mostrar();
    }

    // Método para mostrar la ventana de gestionar empleados
    public void muestraVentanaGestionEmpleados() {
        ventanaEmpleados.muestra(); // Llamar al método muestra()
                                    // en lugar de otros métodos
    }

    // Método para mostrar la ventana de revisar sugerencias
    public void muestraVentanaRevisarSugerencias() {
        ventanaRevisarSugerencias.muestra(); // Llamar al método muestra() en lugar de otros métodos
    }

    // Muestra la ventana para gestionar notificaciones
    public void muestraVentanaNotificacionesAdministrador() {
        ventanaNotificaciones.muestra(this); // Asegúrate de que VentanaNotificaciones tenga el método muestra()
    }

    /**
     * Método para registrar un administrador.
     * 
     * @param nombre     Nombre del administrador
     * @param correo     Correo electrónico del administrador
     * @param contrasena Contraseña del administrador
     */
    public void registrarAdministrador(String nombre, String correo, String contrasena) {
        // Asegúrate de que los campos no estén vacíos
        if (nombre == null || correo == null || contrasena == null ||
                nombre.trim().isEmpty() || correo.trim().isEmpty() || contrasena.trim().isEmpty()) {
            // Muestra algún mensaje de error si los campos están vacíos
            ventanaLoginAdministrador.mostrarMensajeError("Todos los campos son obligatorios.");
            return;
        }

        // Crea el objeto Usuario con la información proporcionada
        Usuario nuevoAdministrador = new Usuario();
        nuevoAdministrador.setNombre(nombre);
        nuevoAdministrador.setCorreo(correo);
        nuevoAdministrador.setContrasena(contrasena);
        nuevoAdministrador.setRol(Rol.ADMINISTRADOR); // Asignamos el rol de administrador

        try {
            // Guarda el usuario usando el servicio correspondiente
            servicioUsuario.registrarUsuario(nuevoAdministrador);

            // Muestra un mensaje de éxito
            ventanaLoginAdministrador.mostrarAlerta("Registro Exitoso", "El administrador ha sido registrado.");
        } catch (Exception e) {
            // Muestra un mensaje de error si ocurre un problema
            ventanaLoginAdministrador.mostrarMensajeError("Hubo un problema al registrar al administrador.");
        }
    }

    public ControlNotificaciones getControlNotificaciones() {
        return controlNotificaciones;
    }
}
