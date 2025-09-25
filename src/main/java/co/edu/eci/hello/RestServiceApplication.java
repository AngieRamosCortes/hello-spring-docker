package co.edu.eci.hello;

import co.edu.eci.framework.*;

/**
 * Aplicación principal que utiliza el microframework web para crear un servicio
 * REST.
 * Esta clase configura el servidor y define las rutas disponibles.
 * 
 * @author Angie Ramos
 * @version 1.0
 */
public class RestServiceApplication {

    /**
     * Método principal para iniciar la aplicación.
     * Configura el servidor web con rutas y manejo de solicitudes.
     * 
     * @param args Argumentos de la línea de comandos (no utilizados)
     */
    public static void main(String[] args) {
        configureRoutes();
        startServer(getPort());
        registerShutdownHook();
        waitForever();
    }

    /**
     * Configura las rutas de la aplicación.
     */
    private static void configureRoutes() {
        WebFramework.get("/", (req, resp) -> {
            resp.html();
            return "<!DOCTYPE html>\n" +
                    "<html>\n" +
                    "<head>\n" +
                    "    <title>Bienvenido - Microframework Web</title>\n" +
                    "    <style>\n" +
                    "        body { font-family: Arial, sans-serif; margin: 40px; line-height: 1.6; }\n" +
                    "        h1 { color: #333; }\n" +
                    "        .container { max-width: 800px; margin: 0 auto; }\n" +
                    "        .endpoints { background: #f4f4f4; padding: 20px; border-radius: 5px; }\n" +
                    "        .endpoint { margin-bottom: 10px; }\n" +
                    "        a { color: #0066cc; text-decoration: none; }\n" +
                    "        a:hover { text-decoration: underline; }\n" +
                    "    </style>\n" +
                    "</head>\n" +
                    "<body>\n" +
                    "    <div class=\"container\">\n" +
                    "        <h1>Bienvenido al Microframework Web</h1>\n" +
                    "        <p>Esta es una aplicación de demostración que utiliza un microframework web personalizado.</p>\n"
                    +
                    "        \n" +
                    "        <div class=\"endpoints\">\n" +
                    "            <h2>Endpoints disponibles:</h2>\n" +
                    "            <div class=\"endpoint\"><a href=\"/hello\">/hello</a> - Devuelve un saludo simple</div>\n"
                    +
                    "            <div class=\"endpoint\"><a href=\"/greeting?name=Angie Ramos\">/greeting?name=Angie Ramos</a> - Devuelve un saludo personalizado</div>\n"
                    +
                    "        </div>\n" +
                    "        \n" +
                    "        <p>Desarrollado por Angie Ramos - AREP 2025</p>\n" +
                    "    </div>\n" +
                    "</body>\n" +
                    "</html>";
        });

        WebFramework.get("/hello", (req, resp) -> {
            return "Hello Docker!";
        });

        WebFramework.get("/greeting", (req, resp) -> {
            String name = req.getValues("name");
            if (name.isEmpty()) {
                name = "World";
            }

            return String.format("Hello, %s!", name);
        });
    }

    /**
     * Inicia el servidor en el puerto especificado.
     * 
     * @param port El puerto en el que escuchará el servidor
     */
    private static void startServer(int port) {
        WebFramework.start(port);
        System.out.println("Servidor iniciado en el puerto " + port);
    }

    /**
     * Registra un hook de apagado para detener el servidor de manera elegante al
     * cerrar la aplicación.
     */
    private static void registerShutdownHook() {
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            System.out.println("Apagando el servidor...");
            WebFramework.stop();
        }));
    }

    /**
     * Determina el puerto en el que escuchará el servidor.
     * Primero intenta usar el puerto definido en la variable de entorno PORT.
     * Si no está definida, usa el puerto 4567 por defecto.
     * 
     * @return El número de puerto
     */
    private static int getPort() {
        if (System.getenv("PORT") != null) {
            return Integer.parseInt(System.getenv("PORT"));
        }
        return 4567;
    }

    /**
     * Método que mantiene la aplicación en ejecución indefinidamente.
     * Espera en un objeto de bloqueo para evitar que la aplicación termine.
     */
    private static void waitForever() {
        final Object lock = new Object();
        synchronized (lock) {
            try {
                System.out.println("Servidor ejecutándose. Presiona Ctrl+C para terminar.");
                lock.wait();
            } catch (InterruptedException e) {
                System.out.println("Servidor interrumpido.");
            }
        }
    }
}
