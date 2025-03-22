package com.neobit.sugerencia.presentacion.empleados;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.neobit.sugerencia.negocio.ServicioEmpleado;
import com.neobit.sugerencia.negocio.modelo.Empleado;

import java.util.List;

@Component
public class ControlEmpleados {

    @Autowired
    private ServicioEmpleado servicioEmpleado;

    @Autowired
    private VentanaEmpleados ventana;

    /**
     * Inicia el caso de uso
     */
    public void inicia() {
        List<Empleado> empleados = servicioEmpleado.recuperaEmpleados();
        ventana.muestra(this, empleados);
    }

    /**
     * Agrega un nuevo empleado
     * 
     * @param nombre El nombre del empleado
     * @param correo El correo del empleado
     */
    public void agregaEmpleado(String nombre, String correo) {
        Empleado empleado = new Empleado(nombre, correo);
        servicioEmpleado.agregaEmpleado(empleado);
        inicia();
    }

    /**
     * Elimina un empleado
     * 
     * @param empleado El empleado a eliminar
     */
    public void eliminaEmpleado(Empleado empleado) {
        servicioEmpleado.eliminaEmpleado(empleado);
        inicia();
    }

    public List<Empleado> obtenerEmpleados() {
        // Llama al servicioEmpleado para obtener la lista de empleados desde la base de
        // datos
        return servicioEmpleado.recuperaEmpleados();
    }

}