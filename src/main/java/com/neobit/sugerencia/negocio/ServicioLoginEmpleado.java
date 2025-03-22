package com.neobit.sugerencia.negocio;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.neobit.sugerencia.datos.EmpleadoRepository;
import com.neobit.sugerencia.negocio.modelo.Empleado;

@Service
public class ServicioLoginEmpleado {

    @Autowired
    private EmpleadoRepository empleadoRepositorio;

    // Método para validar el login del empleado
    public boolean validarLogin(String usuario, String clave) {
        Empleado empleado = empleadoRepositorio.findByUsuario(usuario);
        return empleado != null && empleado.getClave().equals(clave);
    }

    // Método para enviar el correo de recuperación de contraseña
    public boolean enviarCorreoRecuperacion(String correo) {
        Empleado empleado = empleadoRepositorio.findByCorreo(correo);
        if (empleado != null) {
            // Aquí deberías implementar la lógica para enviar el correo de recuperación
            // Este es solo un ejemplo simple
            System.out.println("Correo de recuperación enviado a: " + correo);
            return true;
        }
        return false;
    }

    public boolean registrarNuevoUsuario(String nombre, String correo, String usuario, String clave) {
        if (nombre.isEmpty() || correo.isEmpty() || usuario.isEmpty() || clave.isEmpty()) {
            throw new IllegalArgumentException("Todos los campos son obligatorios.");
        }

        Empleado nuevoEmpleado = new Empleado(nombre, correo, usuario, clave);
        empleadoRepositorio.save(nuevoEmpleado);
        return true;
    }
}