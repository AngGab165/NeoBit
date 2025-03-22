package com.neobit.sugerencia.presentacion.principal;

import com.neobit.sugerencia.negocio.modelo.Notificaciones;
import com.neobit.sugerencia.presentacion.empleados.VentanaEmpleados;
import com.neobit.sugerencia.presentacion.sugerencia.ControlSugerencias;
import com.neobit.sugerencia.presentacion.sugerencia.VentanaSugerencias;
import com.neobit.sugerencia.presentacion.notificaciones.ControlNotificaciones;
import com.neobit.sugerencia.presentacion.notificaciones.VentanaNotificaciones;
import com.neobit.sugerencia.presentacion.revisarSugerencias.VentanaRevisarSugerencias;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;
import javafx.stage.Stage;

@Component
public class ControlPrincipalEmpleado {

    // Asegúrate de que estas ventanas estén correctamente definidas e implementadas
    @Autowired
    private VentanaPrincipalEmpleado ventanaPrincipalEmpleado;

    @Autowired
    private VentanaEmpleados ventanaEmpleados;

    @Autowired
    private VentanaSugerencias ventanaSugerencias;

    @Autowired
    private VentanaNotificaciones ventanaNotificaciones;

    @Autowired
    @Lazy
    private VentanaRevisarSugerencias ventanaRevisarSugerencias;

    @Autowired
    private ControlSugerencias controlSugerencias;

    // Inicia la aplicación y muestra la ventana principal
    public void inicia(Stage primaryStage) {
        ventanaPrincipalEmpleado.setControl(this); // Asegúrate de que esta clase tenga el método setControl
        ventanaPrincipalEmpleado.muestra(primaryStage); // Llamar al método muestra() con el Stage adecuado
    }

    // Muestra la ventana para gestionar empleados
    public void muestraVentanaEmpleados() {
        ventanaEmpleados.muestra(); // Asegúrate de que VentanaEmpleados tenga el método muestra()
    }

    // Muestra la ventana para gestionar sugerencias
    public void muestraVentanaSugerencias() {
        controlSugerencias.inicia(); // Asegúrate de que ControlSugerencias tenga el método inicia()
    }

    // Muestra la ventana para gestionar notificaciones
    public void muestraVentanaNotificaciones() {
        ventanaNotificaciones.muestra(); // Asegúrate de que VentanaNotificaciones tenga el método muestra()
    }

    // Muestra la ventana para revisar sugerencias (Administrador)
    public void muestraVentanaRevisarSugerencias() {
        ventanaRevisarSugerencias.muestra(); // Asegúrate de que VentanaRevisarSugerencias tenga el método muestra()
    }

    public void inicia() {
        Stage stage = new Stage(); // Crear una nueva ventana (Stage)
        inicia(stage); // Llamar al método inicia(Stage) ya definido
    }

    public ControlNotificaciones getControlNotificaciones() {
        return new ControlNotificaciones(); // Retorna una nueva instancia del controlador de notificaciones
    }

    public List<Notificaciones> obtenerNotificaciones() {
        ControlNotificaciones controlNotificaciones = getControlNotificaciones();
        return controlNotificaciones.obtenerTodasLasNotificaciones(); // Retorna la lista de notificaciones del
                                                                      // controlador
    }

}
