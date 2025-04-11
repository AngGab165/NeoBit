package com.neobit.sugerencia.negocio;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.neobit.sugerencia.negocio.modelo.RespuestaForo;
import com.neobit.sugerencia.negocio.modelo.TemaForo;
import com.neobit.sugerencia.datos.RespuestaForoRepository;
import com.neobit.sugerencia.datos.TemaForoRepository;

@Service
public class ForoService {

    @Autowired
    private RespuestaForoRepository respuestaForoRepository;

    @Autowired
    private TemaForoRepository temaForoRepository;

    @Autowired
    private NotificacionService notificacionService; // Servicio de notificaciones

    // Método para obtener todas las respuestas de un tema
    public List<RespuestaForo> obtenerRespuestasPorTema(TemaForo tema) {
        return respuestaForoRepository.findByTemaForo(tema);
    }

    // Método para agregar una respuesta
    public RespuestaForo agregarRespuesta(TemaForo tema, String administrador, String contenido) {
        // Crear una nueva respuesta con el administrador y contenido proporcionados
        RespuestaForo respuesta = new RespuestaForo(administrador, contenido, java.time.LocalDate.now(), tema);

        // Guardar la respuesta en la base de datos
        RespuestaForo respuestaGuardada = respuestaForoRepository.save(respuesta);

        // Actualizar la lista de respuestas del tema
        tema.getRespuestas().add(respuestaGuardada);
        temaForoRepository.save(tema);

        // Enviar notificación (opcional)
        notificacionService.enviarNotificacion("Nueva respuesta agregada al tema: " + tema.getTitulo());

        return respuestaGuardada;
    }

    // Método para obtener todos los temas
    public List<TemaForo> obtenerTodosLosTemas() {
        return temaForoRepository.findAll(); // Recupera todos los temas desde la base de datos
    }

    // Método para agregar un nuevo tema
    public TemaForo agregarTema(String titulo, String autor) {
        // Crear un nuevo tema con el título y autor proporcionados
        TemaForo tema = new TemaForo(titulo, autor, java.time.LocalDate.now());

        // Guardar el tema en la base de datos
        TemaForo temaGuardado = temaForoRepository.save(tema);

        // Enviar notificación (opcional)
        notificacionService.enviarNotificacion("Se ha creado un nuevo tema: " + titulo);

        return temaGuardado;
    }
}