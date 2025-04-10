package com.neobit.sugerencia.presentacion.principal;

import com.neobit.sugerencia.negocio.ServicioUsuario;
import com.neobit.sugerencia.negocio.modelo.Empleado;
import com.neobit.sugerencia.negocio.modelo.Notificaciones;
import com.neobit.sugerencia.negocio.modelo.Usuario;
import com.neobit.sugerencia.presentacion.empleados.VentanaEmpleados;
import com.neobit.sugerencia.presentacion.sugerencia.ControlSugerencias;
import com.neobit.sugerencia.presentacion.sugerencia.VentanaSugerencias;
import com.neobit.sugerencia.presentacion.notificaciones.ControlNotificaciones;

import com.neobit.sugerencia.presentacion.notificaciones.VentanaNotificacionesEmpleado;
import com.neobit.sugerencia.presentacion.revisarSugerencias.VentanaRevisarSugerencias;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;
import javafx.stage.Stage;

@Component
public class ControlPrincipalEmpleado {

    private Empleado empleadoActual;
    // Asegúrate de que estas ventanas estén correctamente definidas e implementadas
    @Autowired
    private VentanaPrincipalEmpleado ventanaPrincipalEmpleado;

    @Autowired
    private VentanaEmpleados ventanaEmpleados;

    @Autowired
    private VentanaSugerencias ventanaSugerencias;

    @Autowired
    @Lazy
    private VentanaNotificacionesEmpleado ventanaNotificaciones;

    @Autowired
    @Lazy
    private VentanaRevisarSugerencias ventanaRevisarSugerencias;

    @Autowired
    private ControlSugerencias controlSugerencias;

    @Autowired
    private ControlNotificaciones controlNotificaciones;

    private String nombreEmpleado;

    public void setNombreEmpleado(String nombreEmpleado) {
        this.nombreEmpleado = nombreEmpleado;
    }

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
        if (nombreEmpleado != null) {
            ventanaRevisarSugerencias.setNombreAdministrador(nombreEmpleado); // Pasamos el nombre al controlador
                                                                              // de esa ventana
        }
        ventanaRevisarSugerencias.muestra(); // Mostrar la ventana
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

    // Método para establecer el empleado actual
    public void setEmpleadoActual(Empleado empleado) {
        this.empleadoActual = empleado;
    }

    // Método para obtener el empleado actual
    public Empleado getEmpleadoActual() {
        return empleadoActual;
    }

    // Método para obtener las notificaciones del empleado actual
    public List<Notificaciones> obtenerNotificacionesEmpleado() {
        if (empleadoActual != null) {
            return controlNotificaciones.obtenerNotificacionesPorEmpleado(empleadoActual.getId());
        }
        return List.of(); // Si no hay empleado, retorna una lista vacía
    }

    // Método para actualizar las notificaciones
    public void actualizarNotificacionesEmpleado() {
        if (empleadoActual != null) {
            List<Notificaciones> notificaciones = controlNotificaciones
                    .obtenerNotificacionesPorEmpleado(empleadoActual.getId());
            System.out.println("Notificaciones actualizadas: " + notificaciones.size());
        }
    }

    public ControlNotificaciones getControlNotificaciones() {
        return controlNotificaciones;
    }
}
