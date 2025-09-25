package co.edu.eci.framework;

/**
 * Representa una ruta registrada en la aplicación.
 * Esta clase almacena la información de una ruta y su manejador.
 * 
 * @author Angie Ramos
 * @version 1.0
 */
public class Route {

    private final String method;
    private final String path;
    private final RouteHandler handler;

    /**
     * Constructor para crear una ruta.
     *
     * @param method  El método HTTP (GET, POST, etc.)
     * @param path    La ruta URL (por ejemplo, "/hello")
     * @param handler El manejador para esta ruta
     */
    public Route(String method, String path, RouteHandler handler) {
        this.method = method;
        this.path = path;
        this.handler = handler;
    }

    /**
     * Obtiene el método HTTP de esta ruta.
     *
     * @return El método HTTP como String
     */
    public String getMethod() {
        return method;
    }

    /**
     * Obtiene la ruta URL de esta ruta.
     *
     * @return La ruta URL como String
     */
    public String getPath() {
        return path;
    }

    /**
     * Ejecuta el manejador de esta ruta con la solicitud y respuesta dadas.
     *
     * @param request  La solicitud HTTP
     * @param response La respuesta HTTP
     * @return El cuerpo de la respuesta como String
     * @throws Exception Si ocurre un error al manejar la solicitud
     */
    public String execute(Request request, Response response) throws Exception {
        return handler.handle(request, response);
    }
}