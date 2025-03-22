package com.neobit.sugerencia.presentacion.login;

import com.neobit.sugerencia.negocio.ServicioUsuario;
import com.neobit.sugerencia.negocio.modelo.Usuario;
import javafx.stage.Stage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import java.util.Random;

@Component
public class ControlRecuperarContrasena {

    @Autowired
    @Lazy
    private VentanaRecuperarContrasena ventanaRecuperarContrasena;

    @Autowired
    private VentanaLoginAdministrador ventanaLoginAdministrador;

    @Autowired
    private VentanaLoginEmpleado ventanaLoginEmpleado;

    @Autowired
    private ServicioUsuario servicioUsuario;

    // Método para recuperar la contraseña, reutilizado por ambos roles
    public void recuperarContrasena(String correo) {
        if (correo.isEmpty()) {
            ventanaRecuperarContrasena.mostrarAlerta("Error", "Por favor, ingrese su correo electrónico.");
            return;
        }

        // Buscar al usuario por correo
        Usuario usuario = servicioUsuario.obtenerUsuarioPorCorreo(correo);

        if (usuario == null) {
            ventanaRecuperarContrasena.mostrarAlerta("Error", "El correo no está registrado.");
            return;
        }

        // Generar nueva contraseña temporal
        String nuevaContrasena = generarContrasenaTemporal();
        usuario.setClave(nuevaContrasena);
        servicioUsuario.registrarUsuario(usuario); // Actualiza la contraseña en la base de datos

        // Simulación de envío de correo (puedes adaptarlo a tu lógica real)
        System.out.println("Nueva contraseña enviada a: " + correo);
        System.out.println("Nueva contraseña temporal: " + nuevaContrasena);

        ventanaRecuperarContrasena.mostrarAlerta(
                "Éxito",
                "Se ha enviado una nueva contraseña temporal a tu correo.");

        // Redirigir a la ventana de login según el rol del usuario
        redirigirLogin(usuario);
    }

    // Método para generar una contraseña temporal aleatoria
    private String generarContrasenaTemporal() {
        String caracteres = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        StringBuilder nuevaContrasena = new StringBuilder();
        Random random = new Random();

        for (int i = 0; i < 8; i++) {
            int indice = random.nextInt(caracteres.length());
            nuevaContrasena.append(caracteres.charAt(indice));
        }

        return nuevaContrasena.toString();
    }

    // Método para redirigir al login correspondiente según el rol del usuario
    private void redirigirLogin(Usuario usuario) {
        if (usuario.getRol().equalsIgnoreCase("ADMINISTRADOR")) {
            // Si el rol es Administrador, abrir la ventana de login del administrador
            ventanaLoginAdministrador.start(new Stage());
        } else if (usuario.getRol().equalsIgnoreCase("EMPLEADO")) {
            // Si el rol es Empleado, abrir la ventana de login del empleado
            ventanaLoginEmpleado.start(new Stage());
        }
    }
}