package co.edu.eci.framework;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.Map;

/**
 * Representa una solicitud HTTP con métodos para acceder a los datos de la
 * solicitud.
 * Esta clase encapsula toda la información de una solicitud HTTP,
 * incluyendo encabezados y parámetros de consulta.
 * 
 * @author Angie Ramos
 * @version 1.0
 */
public class Request {

    private final String method;
    private final String path;
    private final String queryString;
    private final Map<String, String> headers;
    private final Map<String, String> queryParams;

    /**
     * Constructor para crear un objeto Request a partir de datos de una solicitud
     * HTTP.
     *
     * @param method      El método HTTP (GET, POST, etc.)
     * @param path        La ruta de la solicitud sin la cadena de consulta
     * @param queryString La parte de la cadena de consulta de la URL
     * @param headers     Mapa de encabezados HTTP
     */
    public Request(String method, String path, String queryString, Map<String, String> headers) {
        this.method = method;
        this.path = path;
        this.queryString = queryString;
        this.headers = headers != null ? headers : new HashMap<>();
        this.queryParams = parseQueryString(queryString);
    }

    /**
     * Obtiene el método HTTP de la solicitud.
     *
     * @return El método HTTP como String
     */
    public String getMethod() {
        return method;
    }

    /**
     * Obtiene la ruta de la solicitud sin los parámetros de consulta.
     *
     * @return La ruta como String
     */
    public String getPath() {
        return path;
    }

    /**
     * Obtiene la cadena de consulta de la solicitud.
     *
     * @return La cadena de consulta como String
     */
    public String getQueryString() {
        return queryString;
    }

    /**
     * Obtiene un encabezado de la solicitud por su nombre.
     *
     * @param name El nombre del encabezado
     * @return El valor del encabezado, o cadena vacía si no existe
     */
    public String getHeader(String name) {
        return headers.getOrDefault(name, "");
    }

    /**
     * Obtiene todos los encabezados de la solicitud.
     *
     * @return Un mapa de encabezados
     */
    public Map<String, String> getHeaders() {
        return new HashMap<>(headers);
    }

    /**
     * Obtiene el valor de un parámetro de consulta por su nombre.
     *
     * @param name El nombre del parámetro
     * @return El valor del parámetro, o cadena vacía si no existe
     */
    public String getValues(String name) {
        return queryParams.getOrDefault(name, "");
    }

    /**
     * Parsea la cadena de consulta en un mapa de parámetros.
     *
     * @param queryString La cadena de consulta a parsear
     * @return Un mapa de parámetros y sus valores
     */
    private Map<String, String> parseQueryString(String queryString) {
        Map<String, String> params = new HashMap<>();
        if (queryString == null || queryString.isEmpty()) {
            return params;
        }

        String[] pairs = queryString.split("&");
        for (String pair : pairs) {
            int idx = pair.indexOf("=");
            try {
                String key = idx > 0 ? URLDecoder.decode(pair.substring(0, idx), "UTF-8")
                        : URLDecoder.decode(pair, "UTF-8");
                String value = idx > 0 && pair.length() > idx + 1 ? URLDecoder.decode(pair.substring(idx + 1), "UTF-8")
                        : "";
                params.put(key, value);
            } catch (UnsupportedEncodingException e) {
            }
        }
        return params;
    }
}