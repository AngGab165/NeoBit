package com.neobit.sugerencia.presentacion.login;

import javafx.stage.Stage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import com.neobit.sugerencia.negocio.ServicioUsuario;
import com.neobit.sugerencia.negocio.modelo.Usuario;
import com.neobit.sugerencia.negocio.modelo.Rol;

@Controller
public class ControlRegistroAdministrador {

    @Autowired
    private VentanaLoginAdministrador ventanaLoginAdministrador;

    @Autowired
    private ServicioUsuario servicioUsuario; // Servicio que maneja el registro del usuario

    /**
     * Registra un nuevo administrador en el sistema.
     * 
     * @param nombre     Nombre completo del administrador
     * @param correo     Correo electrónico del administrador
     * @param contrasena Contraseña del nuevo administrador
     * @param stage      Ventana actual que se cierra después del registro
     */
    public void registrarAdministrador(String nombre, String correo, String contrasena, String usuario, Stage stage) {
        if (!validarCampos(nombre, correo, usuario, contrasena)) {
            ventanaLoginAdministrador.mostrarAlerta("Error", "Todos los campos son obligatorios.");
            return;
        }

        // Crear objeto administrador
        Usuario nuevoAdministrador = new Usuario(null, nombre, usuario, correo, contrasena, Rol.ADMINISTRADOR);

        // Registrar en el servicio
        servicioUsuario.registrarUsuario(nuevoAdministrador);

        // Mostrar mensaje de éxito
        ventanaLoginAdministrador.mostrarAlerta("Registro Exitoso",
                "El administrador ha sido registrado correctamente.");

        // Cerrar la ventana de registro
        stage.close();
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
