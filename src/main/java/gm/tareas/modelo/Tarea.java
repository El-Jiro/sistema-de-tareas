package gm.tareas.modelo;

import jakarta.persistence.*;
import lombok.*;


import java.time.LocalDate;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Tarea {

    @Id
    @GeneratedValue(strategy =  GenerationType.IDENTITY)
    private Integer idTarea;

    private String nombre;
    private String responsable;
    @Enumerated(EnumType.STRING)
    private EstadoTarea estatus;
    private LocalDate fechaLimite;

    @Builder(builderMethodName = "builderSinId")
    public Tarea(String nombre, String responsable, EstadoTarea estatus, LocalDate fechaLimite) {
        this.nombre = nombre;
        this.responsable = responsable;
        this.estatus = estatus;
        this.fechaLimite = fechaLimite;
    }
}
