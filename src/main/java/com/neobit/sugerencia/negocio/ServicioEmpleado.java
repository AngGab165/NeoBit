package com.neobit.sugerencia.negocio;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.neobit.sugerencia.datos.EmpleadoRepository;
import com.neobit.sugerencia.negocio.modelo.Empleado;
import jakarta.annotation.PostConstruct;
import jakarta.transaction.Transactional;

@Service
/**
 * Servicio relacionado con los empleados
 */
public class ServicioEmpleado {

    @Autowired
    private EmpleadoRepository empleadoRepository;

    /**
     * Recupera todos los empleados
     * 
     * @return lista de empleados
     */
    public List<Empleado> recuperaEmpleados() {
        return empleadoRepository.findAll(); // Devuelve directamente la lista de empleados
    }

    @Transactional
    public Empleado agregaEmpleado(Empleado empleado) {
        return empleadoRepository.save(empleado);
    }

    @Transactional
    public Empleado editaEmpleado(Empleado empleado) {
        return empleadoRepository.save(empleado);
    }

    @Transactional
    public void eliminaEmpleado(Empleado empleado) {
        empleadoRepository.delete(empleado);
    }

    public Empleado encuentraEmpleadoPorNombre(String nombre) {
        return empleadoRepository.findByNombre(nombre);
    }

    public Empleado encuentraEmpleadoPorId(Long id) {
        return empleadoRepository.findById(id).orElse(null);
    }

}