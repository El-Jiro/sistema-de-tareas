package gm.tareas.controlador;

import gm.tareas.modelo.EstadoTarea;
import gm.tareas.modelo.Tarea;
import gm.tareas.servicio.TareaServicio;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.net.URL;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
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
    * para que se cree el enlace automáticamente, de lo contrario no funcionará
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
    private TableColumn<Tarea, LocalDate> fechaColumna;

    //Creamos una lista Observable, es decir que se actualizará automáticamente con cada cambio en nuestra base de datos
    private final ObservableList<Tarea> tareasLista = FXCollections.observableArrayList();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        //Configuramos nuestra tabla para que sólo se pueda seleccionar una fila
        tareasTabla.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        configurarColumnas();
        listarTareas();
    }

    private void configurarColumnas() {

        //Creamos un formateador de fecha
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        //Creamos un cellFactoryPersonalizado para la columna de fecha límite
        fechaColumna.setCellFactory(column-> new TableCell<>(){

            /*
            * Sobreescribimos el método updateItem de TableCell, si la celda está vacía,
            * simplemente escribimos una cadena vacía en ella, de lo contrario formateamos
            * la fecha con el formateador que creamos arriba*/
                    @Override
                    protected void updateItem(LocalDate fecha, boolean empty) {
                        super.updateItem(fecha, empty);
                        if (empty || fecha == null){
                            setText("");
                        } else {
                            setText(formatter.format(fecha));
                        }
                    }
                }
        );

        //Indicamos la propiedad de los objetos Tarea que se cargará en cada columna
        idTareaColumna.setCellValueFactory(new PropertyValueFactory<>("idTarea"));
        nombreTareaColumna.setCellValueFactory(new PropertyValueFactory<>("nombre"));
        responsableColumna.setCellValueFactory(new PropertyValueFactory<>("responsable"));
        estadoColumna.setCellValueFactory(new PropertyValueFactory<>("estatus"));
        fechaColumna.setCellValueFactory(new PropertyValueFactory<>("fechaLimite"));

    }

    private void listarTareas() {
        logger.info("Consultando listado de tareas...");
        //Limpiamos la lista
        tareasLista.clear();
        //Añadimos a la lista todos los objetos de tipo tarea que haya en nuestra base de datos
        tareasLista.addAll(tareaServicio.listarTareas());
        //Agregamos los elementos de la lista a nuestra tabla
        tareasTabla.setItems(tareasLista);
    }


}
