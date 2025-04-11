package com.neobit.sugerencia;

import com.neobit.sugerencia.presentacion.principal.VentanaPrincipal;
import com.neobit.sugerencia.presentacion.login.VentanaLoginEmpleado;
import com.neobit.sugerencia.presentacion.login.VentanaLoginAdministrador;
import com.neobit.sugerencia.negocio.ForoService;
import com.neobit.sugerencia.negocio.modelo.TemaForo;
import javafx.scene.control.TextInputDialog;
import javafx.stage.Stage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Controller;

@Controller
public class MyController {

    @Autowired
    private VentanaPrincipal ventanaPrincipal;

    @Autowired
    @Lazy
    private VentanaLoginEmpleado ventanaLoginEmpleado;

    @Autowired
    private VentanaLoginAdministrador ventanaLoginAdministrador;

    @Autowired
    private ForoService foroService;

    public void initialize(Stage primaryStage) {
        ventanaPrincipal.start(primaryStage); // Muestra la ventana principal
    }

    public void muestraLoginEmpleado() {
        System.out.println("Mostrando ventana de login para Empleado.");
        Stage stage = new Stage();
        ventanaLoginEmpleado.start(stage); // Abre la ventana de login
    }

    public void muestraLoginAdministrador() {
        System.out.println("Mostrando ventana de login para Administrador.");
        Stage stage = new Stage();
        ventanaLoginAdministrador.start(stage); // Abre la ventana de login
    }

    // Método para manejar la lógica de "Responder al Tema"
    public void responderAlTema(TemaForo tema) {
        // Solicitar la respuesta
        TextInputDialog respuestaDialog = new TextInputDialog();
        respuestaDialog.setTitle("Responder al Tema");
        respuestaDialog.setHeaderText("Agregar una respuesta al tema: " + tema.getTitulo());
        respuestaDialog.setContentText("Escribe tu respuesta:");

        respuestaDialog.showAndWait().ifPresent(respuesta -> {
            // Solicitar el nombre del administrador
            TextInputDialog administradorDialog = new TextInputDialog();
            administradorDialog.setTitle("Nombre del Administrador");
            administradorDialog.setHeaderText("Ingrese el nombre del administrador");
            administradorDialog.setContentText("Nombre:");

            administradorDialog.showAndWait().ifPresent(nombreAdministrador -> {
                // Combinar el nombre del administrador con el prefijo "Administrador"
                String autor = "Administrador " + nombreAdministrador;

                // Llamar al servicio para guardar la respuesta
                foroService.agregarRespuesta(tema, autor, respuesta);

                // Actualizar la tabla o interfaz gráfica
                System.out.println("Respuesta agregada por: " + autor);
            });
        });
    }
}