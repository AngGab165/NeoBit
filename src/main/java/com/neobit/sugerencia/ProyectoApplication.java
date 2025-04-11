package com.neobit.sugerencia;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.stage.Stage;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ConfigurableApplicationContext;

@SpringBootApplication
public class ProyectoApplication {

    private static ConfigurableApplicationContext applicationContext;

    public static void main(String[] args) {
        // Lanza la aplicación JavaFX
        Application.launch(JavaFXInitializer.class, args);
    }

    public static class JavaFXInitializer extends Application {

        @Override
        public void init() throws Exception {
            // Inicializa el contexto de Spring
            SpringApplicationBuilder builder = new SpringApplicationBuilder(ProyectoApplication.class);
            builder.headless(false); // Permite que JavaFX funcione en modo visual
            applicationContext = builder.run(getParameters().getRaw().toArray(new String[0]));
        }

        @Override
        public void start(Stage primaryStage) {
            // Aquí se puede configurar JavaFX, ya que Spring ya está inicializado
            Platform.runLater(() -> {
                // Aquí puedes acceder a cualquier bean de Spring y realizar la configuración de JavaFX
                 MyController controller = applicationContext.getBean(MyController.class);
                controller.initialize(primaryStage);
            });
        }

        @Override
        public void stop() throws Exception {
            // Cierra el contexto de Spring cuando la aplicación se detiene
            applicationContext.close();
            Platform.exit();
        }
    }
}