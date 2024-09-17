package extraordinaria.models;

import java.io.Serializable;

/**
 * Enumeració amb els diferents tipus de peticions que podem trobar
 * <p>
 * Aquesta classe implementa Serialitzable per poder ser convertida a
 * bytes i poder ser enviada mitjançant sockets.
 */
public enum RequestType implements Serializable {
    /** El tipus INDENTIFY s'utilitza per identificarse en el servidor */
    IDENTIFY,

    /** El tipus NEXT_TASK s'utilitza per sol·licitar una nova tasca */
    NEXT_TASK,

    /** El tipus FINISHED s'utilitza per indicar que s'ha acabat la tasca */
    FINISHED,

    /** El tipus DISCONNECT s'utilitza per informar de la desconnexió del client */
    DISCONNECT,

    /** El tipus SUCCESS s'utilitza per indicar que l'acció s'ha dut a terme correctament */
    SUCCESS,

    /** El tipus ERROR s'utilitza per indicar que l'acció no s'ha dut a terme correctament */
    ERROR,
}
