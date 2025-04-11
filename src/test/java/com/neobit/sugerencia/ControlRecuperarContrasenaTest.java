package com.neobit.sugerencia;

import com.neobit.sugerencia.negocio.ServicioUsuario;
import com.neobit.sugerencia.negocio.modelo.Usuario;
import com.neobit.sugerencia.presentacion.login.ControlRecuperarContrasena;
import com.neobit.sugerencia.presentacion.login.VentanaLoginAdministrador;
import com.neobit.sugerencia.presentacion.login.VentanaLoginEmpleado;
import com.neobit.sugerencia.presentacion.login.VentanaRecuperarContrasena;
import com.neobit.sugerencia.negocio.modelo.Rol;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import javafx.stage.Stage;

import java.lang.reflect.Method;
import java.lang.reflect.Field;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class ControlRecuperarContrasenaTest {

    @Mock
    private VentanaRecuperarContrasena ventanaRecuperarContrasena;

    @Mock
    private VentanaLoginAdministrador ventanaLoginAdministrador;

    @Mock
    private VentanaLoginEmpleado ventanaLoginEmpleado;

    @Mock
    private ServicioUsuario servicioUsuario;

    @InjectMocks
    private ControlRecuperarContrasena controlRecuperarContrasena;

    @Test
    void recuperarContrasena_CorreoVacio_MuestraAlerta() {
        controlRecuperarContrasena.recuperarContrasena("");
        verify(ventanaRecuperarContrasena).mostrarAlerta("Error", "Por favor, ingrese su correo electrónico.");
        verifyNoInteractions(servicioUsuario);
    }

    @Test
    void recuperarContrasena_CorreoNoRegistrado_MuestraAlerta() {
        when(servicioUsuario.obtenerUsuarioPorCorreo("noexiste@test.com")).thenReturn(null);
        controlRecuperarContrasena.recuperarContrasena("noexiste@test.com");
        verify(ventanaRecuperarContrasena).mostrarAlerta("Error", "El correo no está registrado.");
    }

    @Test
    void recuperarContrasena_CorreoValido_ActualizaContrasena() throws Exception {
        // Arrange
        Usuario usuario = new Usuario();
        usuario.setCorreo("valido@test.com");
        
        // Usamos reflection para setear la clave ya que no hay setClave()
        Field claveField = Usuario.class.getDeclaredField("clave");
        claveField.setAccessible(true);
        claveField.set(usuario, "viejaContrasena");
        
        when(servicioUsuario.obtenerUsuarioPorCorreo("valido@test.com")).thenReturn(usuario);
        doNothing().when(servicioUsuario).registrarUsuario(usuario);
        
        // Act
        controlRecuperarContrasena.recuperarContrasena("valido@test.com");
        
        // Assert
        verify(servicioUsuario).registrarUsuario(usuario);
        verify(ventanaRecuperarContrasena).mostrarAlerta("Éxito", 
            "Se ha enviado una nueva contraseña temporal a tu correo.");
        
        // Verificamos con reflection
        String nuevaClave = (String) claveField.get(usuario);
        assertNotEquals("viejaContrasena", nuevaClave);
        assertEquals(8, nuevaClave.length());
    }

    @Test
    void recuperarContrasena_UsuarioAdministrador_RedirigeALoginAdministrador() {
        // Arrange
        Usuario admin = new Usuario();
        admin.setCorreo("admin@test.com");
        admin.setRol(Rol.ADMINISTRADOR); // Usando el enum Rol
        
        when(servicioUsuario.obtenerUsuarioPorCorreo("admin@test.com")).thenReturn(admin);
        doNothing().when(servicioUsuario).registrarUsuario(admin);
        
        // Act
        controlRecuperarContrasena.recuperarContrasena("admin@test.com");
        
        // Assert
        verify(ventanaLoginAdministrador).start(any(Stage.class));
        verify(ventanaLoginEmpleado, never()).start(any(Stage.class));
    }

    @Test
    void recuperarContrasena_UsuarioEmpleado_RedirigeALoginEmpleado() {
        // Arrange
        Usuario empleado = new Usuario();
        empleado.setCorreo("empleado@test.com");
        empleado.setRol(Rol.EMPLEADO); // Usando el enum Rol
        
        when(servicioUsuario.obtenerUsuarioPorCorreo("empleado@test.com")).thenReturn(empleado);
        doNothing().when(servicioUsuario).registrarUsuario(empleado);
        
        // Act
        controlRecuperarContrasena.recuperarContrasena("empleado@test.com");
        
        // Assert
        verify(ventanaLoginEmpleado).start(any(Stage.class));
        verify(ventanaLoginAdministrador, never()).start(any(Stage.class));
    }

    @Test
    void testGenerarContrasenaTemporal() throws Exception {
        Method method = ControlRecuperarContrasena.class.getDeclaredMethod("generarContrasenaTemporal");
        method.setAccessible(true);
        String contrasena = (String) method.invoke(controlRecuperarContrasena);
        
        assertNotNull(contrasena);
        assertEquals(8, contrasena.length());
        assertTrue(contrasena.matches("[A-Za-z0-9]+"));
    }
}