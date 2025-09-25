package co.edu.eci.framework;

import java.util.HashMap;
import java.util.Map;

/**
 * Representa una respuesta HTTP con métodos para establecer las propiedades de
 * la respuesta.
 * Esta clase encapsula los datos de respuesta, incluidos códigos de estado,
 * encabezados y tipos de contenido.
 * 
 * @author Angie Ramos
 * @version 1.0
 */
public class Response {

    private int statusCode;
    private String contentType;
    private final Map<String, String> headers;

    /**
     * Constructor por defecto que inicializa la respuesta con valores
     * predeterminados.
     * Establece el código de estado en 200 OK y el tipo de contenido en text/plain.
     */
    public Response() {
        this.statusCode = 200;
        this.contentType = "text/plain; charset=utf-8";
        this.headers = new HashMap<>();
    }

    /**
     * Establece el código de estado HTTP para la respuesta.
     *
     * @param statusCode El código de estado HTTP (p. ej., 200, 404, 500)
     * @return Este objeto Response para encadenamiento de métodos
     */
    public Response status(int statusCode) {
        this.statusCode = statusCode;
        return this;
    }

    /**
     * Establece el tipo de contenido de la respuesta.
     *
     * @param contentType El tipo MIME para el contenido de la respuesta
     * @return Este objeto Response para encadenamiento de métodos
     */
    public Response type(String contentType) {
        this.contentType = contentType;
        return this;
    }

    /**
     * Configura la respuesta como JSON.
     * Establece el tipo de contenido en application/json.
     *
     * @return Este objeto Response para encadenamiento de métodos
     */
    public Response json() {
        return type("application/json; charset=utf-8");
    }

    /**
     * Configura la respuesta como HTML.
     * Establece el tipo de contenido en text/html.
     *
     * @return Este objeto Response para encadenamiento de métodos
     */
    public Response html() {
        return type("text/html; charset=utf-8");
    }

    /**
     * Agrega un encabezado a la respuesta.
     *
     * @param name  El nombre del encabezado
     * @param value El valor del encabezado
     * @return Este objeto Response para encadenamiento de métodos
     */
    public Response header(String name, String value) {
        headers.put(name, value);
        return this;
    }

    /**
     * Obtiene el código de estado actual.
     *
     * @return El código de estado HTTP
     */
    public int getStatusCode() {
        return statusCode;
    }

    /**
     * Obtiene el tipo de contenido actual.
     *
     * @return El tipo de contenido MIME
     */
    public String getContentType() {
        return contentType;
    }

    /**
     * Obtiene todos los encabezados establecidos.
     *
     * @return Un mapa de encabezados
     */
    public Map<String, String> getHeaders() {
        return headers;
    }
}