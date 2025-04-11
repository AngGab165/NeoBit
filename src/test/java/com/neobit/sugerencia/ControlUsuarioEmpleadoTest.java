package com.neobit.sugerencia;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.neobit.sugerencia.datos.UsuarioRepository;
import com.neobit.sugerencia.negocio.ServicioUsuario;
import com.neobit.sugerencia.negocio.modelo.Rol;
import com.neobit.sugerencia.negocio.modelo.Usuario;
import com.neobit.sugerencia.presentacion.usuarios.ControlUsuarioEmpleado;
import com.neobit.sugerencia.presentacion.usuarios.VentanaUsuarioEmpleado;

class ControlUsuarioEmpleadoTest {

    @Mock
    private UsuarioRepository usuarioRepository;

    @Mock
    private VentanaUsuarioEmpleado ventana;

    @Mock
    private ServicioUsuario servicioUsuario;

    @InjectMocks
    private ControlUsuarioEmpleado controlUsuarioEmpleado;

    @BeforeEach
    void setUp() {
        // Initialize mocks
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testRegistrarUsuarioEmpleado_ValidInput_SavesUserCorrectly() {
        // Arrange
        String nombre = "Juan Perez";
        String correo = "juan@example.com";
        String usuario = "juanp";
        String contrasena = "password123";

        // Mock the behavior of servicioUsuario.existeCorreo to return false
        when(servicioUsuario.existeCorreo(correo)).thenReturn(false);

        // Act
        controlUsuarioEmpleado.registrarUsuarioEmpleado(nombre, correo, usuario, contrasena);

        // Assert
        // Verify that usuarioRepository.save was called once with the correct Usuario
        // object
        verify(usuarioRepository, times(1)).save(argThat(user -> user.getNombre().equals(nombre) &&
                user.getCorreo().equals(correo) &&
                user.getUsuario().equals(usuario.toLowerCase()) &&
                user.getContrasena().equals(contrasena) &&
                user.getRol() == Rol.EMPLEADO));
    }

    @Test
    void testRegistrarUsuarioEmpleado_NullNombre_ThrowsIllegalArgumentException() {
        // Arrange
        String nombre = null;
        String correo = "juan@example.com";
        String usuario = "juanp";
        String contrasena = "password123";

        // Act & Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            controlUsuarioEmpleado.registrarUsuarioEmpleado(nombre, correo, usuario, contrasena);
        });
        assertEquals("Todos los campos son obligatorios.", exception.getMessage());

        // Verify that usuarioRepository.save was never called
        verify(usuarioRepository, never()).save(any(Usuario.class));
    }

    @Test
    void testRegistrarUsuarioEmpleado_EmptyCorreo_ThrowsIllegalArgumentException() {
        // Arrange
        String nombre = "Juan Perez";
        String correo = "";
        String usuario = "juanp";
        String contrasena = "password123";

        // Act & Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            controlUsuarioEmpleado.registrarUsuarioEmpleado(nombre, correo, usuario, contrasena);
        });
        assertEquals("Todos los campos son obligatorios.", exception.getMessage());

        // Verify that usuarioRepository.save was never called
        verify(usuarioRepository, never()).save(any(Usuario.class));
    }

    @Test
    void testRegistrarUsuarioEmpleado_NullUsuario_ThrowsIllegalArgumentException() {
        // Arrange
        String nombre = "Juan Perez";
        String correo = "juan@example.com";
        String usuario = null;
        String contrasena = "password123";

        // Act & Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            controlUsuarioEmpleado.registrarUsuarioEmpleado(nombre, correo, usuario, contrasena);
        });
        assertEquals("Todos los campos son obligatorios.", exception.getMessage());

        // Verify that usuarioRepository.save was never called
        verify(usuarioRepository, never()).save(any(Usuario.class));
    }

    @Test
    void testRegistrarUsuarioEmpleado_EmptyContrasena_ThrowsIllegalArgumentException() {
        // Arrange
        String nombre = "Juan Perez";
        String correo = "juan@example.com";
        String usuario = "juanp";
        String contrasena = "";

        // Act & Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            controlUsuarioEmpleado.registrarUsuarioEmpleado(nombre, correo, usuario, contrasena);
        });
        assertEquals("Todos los campos son obligatorios.", exception.getMessage());

        // Verify that usuarioRepository.save was never called
        verify(usuarioRepository, never()).save(any(Usuario.class));
    }

    @Test
    void testObtenerUsuarios_ReturnsListOfUsuarios() {
        // Arrange
        Usuario usuario1 = new Usuario();
        usuario1.setNombre("Juan Perez");
        usuario1.setCorreo("juan@example.com");
        usuario1.setUsuario("juanp");
        usuario1.setContrasena("password123");
        usuario1.setRol(Rol.EMPLEADO);

        Usuario usuario2 = new Usuario();
        usuario2.setNombre("Maria Lopez");
        usuario2.setCorreo("maria@example.com");
        usuario2.setUsuario("marial");
        usuario2.setContrasena("password456");
        usuario2.setRol(Rol.EMPLEADO);

        List<Usuario> usuarios = Arrays.asList(usuario1, usuario2);
        when(usuarioRepository.findAll()).thenReturn(usuarios);

        // Act
        List<Usuario> result = controlUsuarioEmpleado.obtenerUsuarios();

        // Assert
        assertEquals(2, result.size());
        assertEquals("Juan Perez", result.get(0).getNombre());
        assertEquals("Maria Lopez", result.get(1).getNombre());
        verify(usuarioRepository, times(1)).findAll();
    }

    @Test
    void testExisteCorreo_CallsServicioUsuario() {
        // Arrange
        String correo = "juan@example.com";
        when(servicioUsuario.existeCorreo(correo)).thenReturn(true);

        // Act
        boolean result = controlUsuarioEmpleado.existeCorreo(correo);

        // Assert
        assertTrue(result);
        verify(servicioUsuario, times(1)).existeCorreo(correo);
    }
}