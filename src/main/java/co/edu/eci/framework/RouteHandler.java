package co.edu.eci.framework;

/**
 * Interfaz funcional que define un manejador de ruta.
 * Utiliza la nueva característica de interfaces funcionales de Java
 * para permitir expresiones lambda como manejadores de ruta.
 * 
 * @author Angie Ramos
 * @version 1.0
 */
@FunctionalInterface
public interface RouteHandler {

    /**
     * Ejecuta el manejador de ruta con la solicitud y respuesta dadas.
     *
     * @param request  La solicitud HTTP
     * @param response La respuesta HTTP
     * @return El cuerpo de la respuesta como String
     * @throws Exception Si ocurre un error al manejar la solicitud
     */
    String handle(Request request, Response response) throws Exception;
}