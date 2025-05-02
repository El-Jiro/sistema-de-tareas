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
import javafx.scene.input.MouseEvent;
import javafx.util.StringConverter;
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
    //Añadimos un salto de línea
    private static String nl = System.lineSeparator();
    //Inyectamos una instancia de la clase de servicio mediante autowired
    @Autowired
    private TareaServicio tareaServicio;

    /*
    * Creamos atributos privados para enlazar los componentes de la vista,
    * los nombres deberán coincidir con el id que definimos en el archivo xml
    * para que se cree el enlace automáticamente, de lo contrario no funcionará
    * */

    //---------------- Tabla y Columnas ---------------------
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

    //----------------- Elementos del formulario ------------------
    @FXML
    private TextField nombreTareaTexto;
    @FXML
    private TextField responsableTexto;
    @FXML
    private ComboBox<EstadoTarea> estatusSelector;
    @FXML
    private DatePicker fechaSelector;

    private Integer idTareaInterno;

    //Creamos una lista Observable, es decir que se actualizará automáticamente con cada cambio en nuestra base de datos
    private final ObservableList<Tarea> tareasLista = FXCollections.observableArrayList();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        //Configuramos nuestra tabla para que sólo se pueda seleccionar una fila
        tareasTabla.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        configurarColumnas();
        listarTareas();
        incializarComboBox();
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

        //Hacemos lo mismo con la columna de estatus
        estadoColumna.setCellFactory(column-> new TableCell<>(){

            @Override
            protected void updateItem(EstadoTarea estadoTarea, boolean empty) {
                //Si la celda no está vacía, mostraremos el displayName del estatus en vez del elemento real del enum
                super.updateItem(estadoTarea, empty);
                if (empty|| estadoTarea == null){
                    setText("");
                } else {
                    setText(estadoTarea.getDisplayName());
                }
            }
        });

        //Indicamos la propiedad de los objetos Tarea que se cargará en cada columna
        idTareaColumna.setCellValueFactory(new PropertyValueFactory<>("idTarea"));
        nombreTareaColumna.setCellValueFactory(new PropertyValueFactory<>("nombre"));
        responsableColumna.setCellValueFactory(new PropertyValueFactory<>("responsable"));
        estadoColumna.setCellValueFactory(new PropertyValueFactory<>("estatus"));
        fechaColumna.setCellValueFactory(new PropertyValueFactory<>("fechaLimite"));

    }

    private void incializarComboBox(){

        //Añadimos todos los valores de EstadoTarea al selector
        estatusSelector.getItems().addAll(EstadoTarea.values());

        //Mostramos el displayName en vez del valor
        estatusSelector.setConverter(new StringConverter<EstadoTarea>() {
            @Override
            public String toString(EstadoTarea estadoTarea) {
                return estadoTarea != null? estadoTarea.getDisplayName(): "";
            }

            //Obtenemos nuevamente el valor real a partir del displayName
            @Override
            public EstadoTarea fromString(String s) {
                return estatusSelector.getItems().stream()
                        .filter(estadoTarea ->
                                estadoTarea.getDisplayName().equals(s))
                                .findFirst()
                                .orElse(null);
            }
        });

        /*Añadimos esto para debugear

        estatusSelector.setOnAction(actionEvent ->{
            EstadoTarea estadoTarea = estatusSelector.getValue();
            logger.info("Valor real: " + estadoTarea.name() + nl);
            logger.info("Nombre mostrado: " + estadoTarea.getDisplayName());
        });*/
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

    public void agregarTarea(){

        //Comprobamos que los campos de nombre, responsable y estatus no estén vacíos
        if (nombreTareaTexto.getText().isEmpty()){
            mostrarMensaje("Error de validación", "Debe proporcionar un nombre para la tarea",
                    new Alert(Alert.AlertType.ERROR));
            nombreTareaTexto.requestFocus();
            return;
        } else if (responsableTexto.getText().isEmpty()) {
            mostrarMensaje("Error de validación", "Debe proporcionar un responsable para la tarea",
                    new Alert(Alert.AlertType.ERROR));
            return;
        } else if (estatusSelector.getValue() == null){
            mostrarMensaje("Error de validación", "Debe indicar el status de la tarea",
                    new Alert(Alert.AlertType.ERROR));
            return;
        } else {
            //Creamos un objeto Tarea vacío
            var tarea = new Tarea();
            //Actualizamos sus atributos con la información introducida por el usuario
            recolectarDatosFormulario(tarea);
            //Guardamos el objeto en la base de Datos
            tareaServicio.guardarTarea(tarea);
            //Mostramos un mensaje de confirmación
            mostrarMensaje("Nueva tarea creada",
                    "Se ha creado correctamente la tarea: " + tarea.getNombre(),
                    new Alert(Alert.AlertType.INFORMATION));
            //Limpiamos el formulario
            limpiarFormulario();
            //Volvemos a cargar la información de la base de datos
            listarTareas();
        }
    }

    //Creamos un método para reiniciar los campos del formulario
    private void limpiarFormulario() {
        nombreTareaTexto.clear();
        responsableTexto.clear();
        estatusSelector.setValue(null);
        fechaSelector.setValue(null);
    }

    //Creamos un método que reciba un objeto Tarea vacío y actualice sus atributos de acuerdo con los datos del formulario
    private void recolectarDatosFormulario(Tarea tarea) {

        tarea.setNombre(nombreTareaTexto.getText());
        tarea.setResponsable(responsableTexto.getText());
        tarea.setEstatus(estatusSelector.getValue());
        tarea.setFechaLimite(fechaSelector.getValue());
    }

    private void mostrarMensaje(String titulo, String mensaje, Alert alert) {

        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }

    public void cargarTareaFormulario() {

        //Obtenemos la fila seleccionada y la guardamos en un objeto de tipo Tarea
        Tarea tarea = tareasTabla.getSelectionModel().getSelectedItem();

        //Verificamos que realmente se haya seleccionado una fila
        if (tarea != null){
            //Inicializamos el id interno con el valor seleccionado de la tabla:
            idTareaInterno = tarea.getIdTarea();
            //Cargamos el resto de la información en los campos del formulario
            nombreTareaTexto.setText(tarea.getNombre());
            responsableTexto.setText(tarea.getResponsable());
            estatusSelector.setValue(tarea.getEstatus());
            fechaSelector.setValue(tarea.getFechaLimite());
        }
    }

}
