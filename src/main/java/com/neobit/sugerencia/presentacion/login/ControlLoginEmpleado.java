package com.neobit.sugerencia.presentacion.login;

import com.neobit.sugerencia.negocio.ServicioLoginEmpleado;
import com.neobit.sugerencia.negocio.modelo.Empleado;
import com.neobit.sugerencia.negocio.modelo.Rol;
import com.neobit.sugerencia.negocio.modelo.Usuario;
import com.neobit.sugerencia.presentacion.detallesSugerencia.ControlVerDetallesSugerencia;
import com.neobit.sugerencia.presentacion.principal.ControlPrincipalEmpleado;
import com.neobit.sugerencia.presentacion.principal.VentanaPrincipalEmpleado;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

@Component
public class ControlLoginEmpleado {

    @Autowired
    private VentanaLoginEmpleado ventanaLoginEmpleado;

    @Autowired
    private ServicioLoginEmpleado servicioLoginEmpleado;

    @Autowired
    @Lazy
    private VentanaPrincipalEmpleado ventanaPrincipalEmpleado;

    private static String nombreEmpleado;

    @Autowired
    private ControlPrincipalEmpleado controlPrincipalEmpleado;

    /**
     * Realiza el login del empleado.
     *
     * @param usuario Nombre de usuario
     * @param clave   Contraseña
     */
    public void loginEmpleado(String usuario, String contrasena) {
        if (!validarCamposVacios(usuario, contrasena)) {
            ventanaLoginEmpleado.mostrarMensajeError("Usuario y contraseña son requeridos.");
            return;
        }

        boolean loginValido = servicioLoginEmpleado.validarLogin(usuario, contrasena);
        if (loginValido) {
            Usuario usuarioEncontrado = servicioLoginEmpleado.obtenerUsuarioPorNombreUsuario(usuario);
            if (usuarioEncontrado != null && usuarioEncontrado.getRol() == Rol.EMPLEADO) {
                controlPrincipalEmpleado.setUsuarioActual(usuarioEncontrado);
                nombreEmpleado = usuarioEncontrado.getNombre();
                System.out.println("Nombre del empleado establecido en ControlLoginEmpleado: " + nombreEmpleado);
                Usuario usuarioLogueado = new Usuario();
                usuarioLogueado.setNombre(nombreEmpleado);
                usuarioLogueado.setRol(Rol.EMPLEADO);
                ventanaPrincipalEmpleado.setUsuario(usuarioLogueado);

                // Aquí ya está guardado el nombre del autor, entonces podemos proceder con el
                // login
                ventanaLoginEmpleado.cerrar();
                ventanaPrincipalEmpleado.mostrar();
            } else {
                ventanaLoginEmpleado.mostrarMensajeError("No se encontró el usuario o no tiene rol de EMPLEADO.");
            }
        } else {
            ventanaLoginEmpleado.mostrarMensajeError("Usuario o contraseña incorrectos.");
        }
    }

    /**
     * Recupera la contraseña del empleado.
     *
     * @param correo Correo electrónico del empleado
     */
    public void recuperarContrasena(String correo) {
        if (!validarCamposVacios(correo)) {
            ventanaLoginEmpleado.mostrarMensajeError("Por favor ingresa tu correo electrónico.");
            return;
        }

        boolean correoEnviado = servicioLoginEmpleado.enviarCorreoRecuperacion(correo);
        if (correoEnviado) {
            ventanaLoginEmpleado.mostrarMensajeError("Te hemos enviado un enlace para recuperar tu contraseña.");
        } else {
            ventanaLoginEmpleado.mostrarMensajeError("No se pudo enviar el correo. Intenta de nuevo.");
        }
    }

    /**
     * Registra un nuevo empleado.
     *
     * @param nombre     Nombre del empleado
     * @param correo     Correo electrónico
     * @param usuario    Nombre de usuario
     * @param contraseña Contraseña
     */
    public void registrarUsuario(String nombre, String correo, String usuario, String contrasena) {
        if (!validarCamposVacios(nombre, correo, usuario, contrasena)) {
            ventanaLoginEmpleado.mostrarMensajeError("Todos los campos son requeridos.");
            return;
        }

        boolean registrado = servicioLoginEmpleado.registrarNuevoUsuario(nombre, correo, usuario, contrasena);
        if (registrado) {
            ventanaLoginEmpleado.mostrarMensajeError("Registro exitoso. Ahora puedes iniciar sesión.");
        } else {
            ventanaLoginEmpleado.mostrarMensajeError("Hubo un error al registrar el usuario.");
        }
    }

    public static String getNombreEmpleado() {
        return nombreEmpleado;
    }

    /**
     * Valida que los campos no estén vacíos.
     *
     * @param campos Campos a validar
     * @return true si todos los campos están llenos, false en caso contrario
     */
    private boolean validarCamposVacios(String... campos) {
        for (String campo : campos) {
            if (campo == null || campo.isEmpty()) {
                return false;
            }
        }
        return true;
    }
}