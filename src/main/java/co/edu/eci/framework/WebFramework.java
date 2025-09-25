package co.edu.eci.framework;

import java.io.IOException;

/**
 * Punto de entrada principal para el Framework Web.
 * Esta clase proporciona métodos estáticos para configurar rutas e iniciar el
 * servidor.
 * Implementa el patrón Facade para proporcionar una interfaz simple para los
 * desarrolladores.
 * 
 * @author Angie Ramos
 * @version 1.0
 */
public class WebFramework {

    private static Router router = new Router();
    private static StaticFileHandler staticFileHandler = new StaticFileHandler();
    private static HttpServer server;
    private static boolean isRunning = false;

    /**
     * Registra una ruta GET con la ruta y el manejador especificados.
     * Este método permite a los desarrolladores definir servicios REST usando
     * expresiones lambda.
     *
     * @param path    La ruta URL para la ruta (p. ej., "/hello")
     * @param handler La función lambda para manejar las solicitudes a esta ruta
     */
    public static void get(String path, RouteHandler handler) {
        router.addRoute("GET", path, handler);
    }

    /**
     * Registra una ruta POST con la ruta y el manejador especificados.
     *
     * @param path    La ruta URL para la ruta
     * @param handler La función lambda para manejar las solicitudes a esta ruta
     */
    public static void post(String path, RouteHandler handler) {
        router.addRoute("POST", path, handler);
    }

    /**
     * Registra una ruta PUT con la ruta y el manejador especificados.
     *
     * @param path    La ruta URL para la ruta
     * @param handler La función lambda para manejar las solicitudes a esta ruta
     */
    public static void put(String path, RouteHandler handler) {
        router.addRoute("PUT", path, handler);
    }

    /**
     * Registra una ruta DELETE con la ruta y el manejador especificados.
     *
     * @param path    La ruta URL para la ruta
     * @param handler La función lambda para manejar las solicitudes a esta ruta
     */
    public static void delete(String path, RouteHandler handler) {
        router.addRoute("DELETE", path, handler);
    }

    /**
     * Establece el directorio donde se encuentran los archivos estáticos.
     * El framework buscará archivos estáticos en el directorio especificado
     * dentro de los recursos del classpath (normalmente target/classes/directory).
     *
     * @param directory La ruta del directorio relativa a los recursos del classpath
     */
    public static void staticfiles(String directory) {
        staticFileHandler.setStaticFilesDirectory(directory);
    }

    /**
     * Inicia el servidor web en el puerto predeterminado (8080).
     * Este método inicia automáticamente el servidor después de la configuración de
     * la ruta.
     */
    public static void start() {
        start(8080);
    }

    /**
     * Inicia el servidor web en el puerto especificado.
     *
     * @param port El número de puerto en el que escuchar
     */
    public static void start(int port) {
        if (isRunning) {
            System.out.println("El servidor ya está en ejecución!");
            return;
        }

        try {
            server = new HttpServer(port, router, staticFileHandler);
            server.start();
            isRunning = true;

            System.out.println("=================================");
            System.out.println("Servidor Web Iniciado!");
            System.out.println("Puerto: " + port);
            System.out.println("Archivos estáticos: " + staticFileHandler.getStaticFilesDirectory());
            System.out.println("Rutas registradas: " + router.getRouteCount());
            System.out.println("URL del servidor: http://localhost:" + port);
            System.out.println("=================================");

        } catch (IOException e) {
            System.err.println("No se pudo iniciar el servidor: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Detiene el servidor web si está en ejecución.
     */
    public static void stop() {
        if (server != null && isRunning) {
            server.stop();
            isRunning = false;
            System.out.println("Servidor detenido.");
        }
    }

    /**
     * Obtiene la instancia del router actual.
     * Útil para pruebas y propósitos de depuración.
     *
     * @return La instancia Router
     */
    public static Router getRouter() {
        return router;
    }

    /**
     * Obtiene la instancia del manejador de archivos estáticos.
     * Útil para pruebas y propósitos de configuración.
     *
     * @return La instancia StaticFileHandler
     */
    public static StaticFileHandler getStaticFileHandler() {
        return staticFileHandler;
    }

    /**
     * Comprueba si el servidor está actualmente en ejecución.
     *
     * @return true si el servidor está en ejecución, false en caso contrario
     */
    public static boolean isRunning() {
        return isRunning;
    }
}