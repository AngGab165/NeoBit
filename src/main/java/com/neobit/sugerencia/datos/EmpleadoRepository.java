package com.neobit.sugerencia.datos;

import org.springframework.data.jpa.repository.JpaRepository;

import com.neobit.sugerencia.negocio.modelo.Empleado;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository

public interface EmpleadoRepository extends JpaRepository<Empleado, Long> {
    Empleado findByNombre(String nombre);
    List<Empleado> findAll(); // Ya viene implementado por JpaRepository

    Empleado findById(long id);
    Empleado findByUsuario(String usuario);

    Empleado findByCorreo(String correo);
}
