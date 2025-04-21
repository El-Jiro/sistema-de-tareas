package gm.tareas.presentacion;

import gm.tareas.TareasApplication;
import javafx.application.Application;
import javafx.stage.Stage;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ConfigurableApplicationContext;

public class SistemaTareasFx extends Application {

    private ConfigurableApplicationContext applicationContext;

    //Inicializamos el contexto de Spring antes de que cargue la interfaz gráfica
    @Override
    public void init() {
        this.applicationContext = new SpringApplicationBuilder(TareasApplication.class).run();
    }

    //Recuperamos la vista de index.fxml
    @Override
    public void start(Stage primaryStage) {

    }
}
