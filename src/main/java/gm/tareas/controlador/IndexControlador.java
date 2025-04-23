package gm.tareas.controlador;

import gm.tareas.modelo.EstadoTarea;
import gm.tareas.modelo.Tarea;
import gm.tareas.servicio.TareaServicio;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.net.URL;
import java.util.Date;
import java.util.ResourceBundle;

@Component
public class IndexControlador implements Initializable {

    //Añadimos un atributo privado de tipo Logger para mandar información a la consola
    private static final Logger logger = LoggerFactory.getLogger(IndexControlador.class);
    //Inyectamos una instancia de la clase de servicio mediante autowired
    @Autowired
    private TareaServicio tareaServicio;

    /*
    * Creamos atributos privados para enlazar los componentes de la vista,
    * los nombres deberán coincidir con el id que definimos en el archivo xml
    * */

    @FXML
    private TableView<Tarea> tareasTabla;
    @FXML
    private TableColumn<Tarea, Integer> idTareaColumna;
    @FXML
    private TableColumn<Tarea, String> nombreTareaColumna;
    @FXML
    private TableColumn<Tarea, String> responsableColumna;
    @FXML
    private  TableColumn<Tarea, EstadoTarea> estadoColumna;
    @FXML
    private TableColumn<Tarea, Date> fechaColumna;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }
}
