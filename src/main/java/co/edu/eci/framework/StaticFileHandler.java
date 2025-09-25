package co.edu.eci.framework;

/**
 * Clase para manejar archivos estáticos desde recursos del classpath.
 * 
 * @author Angie Ramos
 * @version 1.0
 */
public class StaticFileHandler {

    private String staticFilesDirectory;
    private static final String DEFAULT_DIRECTORY = "/public";

    /**
     * Constructor que inicializa el manejador de archivos estáticos con el
     * directorio por defecto.
     */
    public StaticFileHandler() {
        this.staticFilesDirectory = DEFAULT_DIRECTORY;
    }

    /**
     * Establece el directorio donde se encuentran los archivos estáticos.
     *
     * @param directory La ruta del directorio relativa a los recursos del classpath
     */
    public void setStaticFilesDirectory(String directory) {
        if (!directory.startsWith("/")) {
            directory = "/" + directory;
        }
        this.staticFilesDirectory = directory;
        System.out.println("Directorio de archivos estáticos configurado en: " + directory);
    }

    /**
     * Obtiene el directorio actual de archivos estáticos.
     *
     * @return La ruta del directorio de archivos estáticos
     */
    public String getStaticFilesDirectory() {
        return staticFilesDirectory;
    }

    /**
     * Sirve un archivo estático desde el directorio configurado.
     *
     * @param path La ruta del archivo solicitado
     * @return StaticFileResult con el contenido y tipo del archivo, o un resultado
     *         no encontrado
     */
    public StaticFileResult serveStaticFile(String path) {
        if ("/".equals(path)) {
            path = "/index.html";
        }

        String fullPath = staticFilesDirectory + path;

        try (var inputStream = getClass().getResourceAsStream(fullPath)) {
            if (inputStream == null) {
                return StaticFileResult.notFound();
            }

            byte[] content = inputStream.readAllBytes();
            String contentType = determineContentType(path);

            return new StaticFileResult(true, content, contentType);
        } catch (Exception e) {
            System.err.println("Error al leer archivo estático: " + e.getMessage());
            return StaticFileResult.notFound();
        }
    }

    /**
     * Determina el tipo de contenido (MIME type) basado en la extensión del
     * archivo.
     *
     * @param path La ruta del archivo
     * @return El tipo de contenido MIME
     */
    private String determineContentType(String path) {
        if (path.endsWith(".html")) {
            return "text/html; charset=utf-8";
        } else if (path.endsWith(".css")) {
            return "text/css; charset=utf-8";
        } else if (path.endsWith(".js")) {
            return "application/javascript; charset=utf-8";
        } else if (path.endsWith(".json")) {
            return "application/json; charset=utf-8";
        } else if (path.endsWith(".png")) {
            return "image/png";
        } else if (path.endsWith(".jpg") || path.endsWith(".jpeg")) {
            return "image/jpeg";
        } else if (path.endsWith(".gif")) {
            return "image/gif";
        } else if (path.endsWith(".ico")) {
            return "image/x-icon";
        } else if (path.endsWith(".svg")) {
            return "image/svg+xml";
        } else {
            return "application/octet-stream";
        }
    }

    /**
     * Clase interna para encapsular el resultado de la búsqueda de un archivo
     * estático.
     */
    public static class StaticFileResult {
        private final boolean found;
        private final byte[] content;
        private final String contentType;

        /**
         * Constructor para un resultado de archivo encontrado.
         */
        public StaticFileResult(boolean found, byte[] content, String contentType) {
            this.found = found;
            this.content = content;
            this.contentType = contentType;
        }

        /**
         * Crea un resultado para un archivo no encontrado.
         */
        public static StaticFileResult notFound() {
            return new StaticFileResult(false, new byte[0], "");
        }

        /**
         * Verifica si el archivo fue encontrado.
         */
        public boolean isFound() {
            return found;
        }

        /**
         * Obtiene el contenido del archivo.
         */
        public byte[] getContent() {
            return content;
        }

        /**
         * Obtiene el tipo de contenido del archivo.
         */
        public String getContentType() {
            return contentType;
        }
    }
}