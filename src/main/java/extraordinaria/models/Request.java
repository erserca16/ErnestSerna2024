package extraordinaria.models;

import java.io.Serializable;

/**
 * Classe que representa una petició o resposta entre el servidor i el client.
 * <p>
 * Aquesta classe implementa Serialitzable per poder ser convertida a
 * bytes i poder ser enviada mitjançant sockets.
 */
public class Request implements Serializable {
    /**
     * Tipus de petició (SEND/CHANGE_NAME/LIST/SUCCESS/ERROR)
     * @see RequestType
     */
    private final RequestType type;

    /**
     * Objecte adjuntat en la petició
     */
    private final Object object;

    /**
     * Constructor de la petició
     * @param type Tipus de la petició
     * @param object Objecte adjunt
     */
    public Request(RequestType type, Object object) {
        this.type = type;
        this.object = object;
    }

    /**
     * Constructor de la petició
     * @param type Tipus de la petició
     */
    public Request(RequestType type) {
        this(type, null);
    }

    public RequestType getType() {
        return type;
    }

    public Object getObject() {
        return object;
    }
}
