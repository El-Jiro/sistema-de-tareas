package gm.tareas.modelo;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum EstadoTarea {
    PENDIENTE("Pendiente"),
    EN_CURSO("En curso"),
    COMPLETADA("Completada");

    private final String displayName;
}
