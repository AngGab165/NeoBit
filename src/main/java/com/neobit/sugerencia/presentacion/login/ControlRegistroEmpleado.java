package com.neobit.sugerencia.presentacion.login;

import com.neobit.sugerencia.presentacion.principal.ControlPrincipalEmpleado;
import com.neobit.sugerencia.negocio.ServicioUsuario;
import com.neobit.sugerencia.negocio.modelo.Rol;
import com.neobit.sugerencia.negocio.modelo.Usuario;

import javafx.stage.Stage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

@Controller
public class ControlRegistroEmpleado {

    @Autowired
    private VentanaRegistroEmpleado ventanaRegistroEmpleado;

    @Autowired
    private ControlPrincipalEmpleado controlPrincipalEmpleado;

    @Autowired
    private ServicioUsuario servicioUsuario;

    /**
     * Muestra la ventana de registro para empleados.
     */
    public void mostrarVentanaRegistro() {
        ventanaRegistroEmpleado.mostrar();
    }

    /**
     * Registra un nuevo empleado en el sistema.
     * 
     * @param usuario    Nombre de usuario del empleado
     * @param nombre     Nombre completo del empleado
     * @param correo     Correo electrónico del empleado
     * @param contrasena Contraseña del nuevo empleado
     * @param stage      Ventana actual que se cierra después del registro
     */
    public void registrarEmpleado(String usuario, String nombre, String correo, String contrasena, Stage stage) {
        if (!validarCampos(usuario, nombre, correo, contrasena)) {
            ventanaRegistroEmpleado.mostrarAlerta("Error", "Todos los campos son obligatorios.");
            return;
        }

        if (servicioUsuario.existeCorreo(correo)) {
            ventanaRegistroEmpleado.mostrarAlerta("Error", "El correo ya está registrado.");
            return;
        }

        // Crear el usuario con rol EMPLEADO
        Usuario nuevoEmpleado = new Usuario(nombre, usuario, correo, contrasena, Rol.EMPLEADO);

        // Registrar el empleado en la base de datos
        servicioUsuario.registrarUsuario(nuevoEmpleado);

        // Mostrar mensaje de éxito
        ventanaRegistroEmpleado.mostrarAlerta("Registro Exitoso", "El empleado ha sido registrado correctamente.");

        // Cerrar la ventana de registro
        stage.close();

        // Redirigir a la ventana principal del empleado
        controlPrincipalEmpleado.inicia();
    }

    /**
     * Valida que los campos de entrada no estén vacíos.
     *
     * @param valores Lista de valores a validar
     * @return true si todos los valores son válidos, false si alguno está vacío
     */
    private boolean validarCampos(String... valores) {
        for (String valor : valores) {
            if (valor == null || valor.trim().isEmpty()) {
                return false;
            }
        }
        return true;
    }
}
