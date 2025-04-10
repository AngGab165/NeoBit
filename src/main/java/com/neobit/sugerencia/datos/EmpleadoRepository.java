package com.neobit.sugerencia.datos;

import org.springframework.data.jpa.repository.JpaRepository;

import com.neobit.sugerencia.negocio.modelo.Empleado;

public interface EmpleadoRepository extends JpaRepository<Empleado, Long> {

    Empleado findByNombre(String nombre);

    Empleado findByUsuario(String usuario);

    Empleado findByCorreo(String correo);

}