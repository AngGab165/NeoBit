package com.neobit.sugerencia.datos;

import com.neobit.sugerencia.negocio.modelo.Notificaciones;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NotificacionesRepository extends JpaRepository<Notificaciones, Long> {
}
