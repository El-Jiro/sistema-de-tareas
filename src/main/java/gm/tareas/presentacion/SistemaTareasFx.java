package gm.tareas.presentacion;

import gm.tareas.TareasApplication;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.stage.Stage;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ConfigurableApplicationContext;

import java.io.IOException;

public class SistemaTareasFx extends Application {

    private ConfigurableApplicationContext applicationContext;

    //Inicializamos el contexto de Spring antes de que cargue la interfaz gráfica
    @Override
    public void init() {
        this.applicationContext = new SpringApplicationBuilder(TareasApplication.class).run();
    }

    //Recuperamos y cargamos la vista de index.fxml
    @Override
    public void start(Stage primaryStage) {

       try{
           //Creamos un objeto de tipo FXMLLoader a a partir de la plantilla de index
           FXMLLoader loader = new FXMLLoader(TareasApplication.class.getResource("/templates/index.fxml"));
           //Cargamos todos los beans (es decir los objetos de la fábrica de Spring) en loader
           loader.setControllerFactory(applicationContext::getBean);
           //Creamos una Escena a partir del objeto loader
           Scene scene = new Scene(loader.load());
           //Cargamos el css
           scene.getStylesheets().add(getClass().getResource("/styles.css").toExternalForm());
           //Cargamos la escena en el stage o escenario
           primaryStage.setScene(scene);
           //Hacemos visible el stage
           primaryStage.show();
       } catch (Exception e){
           e.printStackTrace();
           mostrarError("Ha ocurrido un error inesperado");
       }
    }

    //Sobreescribimos el método para detener nuestra aplicación
    @Override
    public void stop() {
        //Detenemos nuestra aplicación de Spring, eso incluye cerrar la conexión hacia la base de datos
        applicationContext.stop();
        //Cerramos la aplicación de JavaFX mediante el método estático exit de la clase Platform
        Platform.exit();
    }

    //Mostramos un mensaje de error mediante JavaFX
    private void mostrarError(String mensaje) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }

}
