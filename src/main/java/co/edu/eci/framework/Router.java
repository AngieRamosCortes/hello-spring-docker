package co.edu.eci.framework;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * Enrutador para manejar las rutas y sus manejadores.
 * Esta clase registra las rutas y busca la ruta apropiada para cada solicitud.
 * 
 * @author Angie Ramos
 * @version 1.0
 */
public class Router {

    private final Map<String, Map<String, Route>> routes;

    /**
     * Constructor que inicializa el enrutador.
     */
    public Router() {
        this.routes = new HashMap<>();
    }

    /**
     * Agrega una nueva ruta al enrutador.
     *
     * @param method  El método HTTP (GET, POST, etc.)
     * @param path    La ruta URL (por ejemplo, "/hello")
     * @param handler El manejador para esta ruta
     */
    public void addRoute(String method, String path, RouteHandler handler) {
        Map<String, Route> methodRoutes = routes.computeIfAbsent(method, k -> new HashMap<>());
        methodRoutes.put(path, new Route(method, path, handler));
    }

    /**
     * Busca una ruta que coincida con el método y la ruta especificados.
     *
     * @param method El método HTTP
     * @param path   La ruta URL
     * @return Optional con la ruta si se encuentra, o vacío si no
     */
    public Optional<Route> findRoute(String method, String path) {
        Map<String, Route> methodRoutes = routes.get(method);
        if (methodRoutes == null) {
            return Optional.empty();
        }

        Route route = methodRoutes.get(path);
        return Optional.ofNullable(route);
    }

    /**
     * Obtiene el número total de rutas registradas.
     *
     * @return El número de rutas
     */
    public int getRouteCount() {
        return routes.values().stream()
                .mapToInt(Map::size)
                .sum();
    }
}