package gm.tareas.modelo;

import jakarta.persistence.*;
import lombok.*;


import java.util.Date;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Tarea {

    @Id
    @GeneratedValue(strategy =  GenerationType.IDENTITY)
    private Integer id;

    private String nombre;
    private String responsable;
    @Enumerated(EnumType.STRING)
    private EstadoTarea status;
    private Date fechaLimite;

    @Builder(builderMethodName = "builderSinId")
    public Tarea(String nombre, String responsable, EstadoTarea status, Date fechaLimite) {
        this.nombre = nombre;
        this.responsable = responsable;
        this.status = status;
        this.fechaLimite = fechaLimite;
    }
}
