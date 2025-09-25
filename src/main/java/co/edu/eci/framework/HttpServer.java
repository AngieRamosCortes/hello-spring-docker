package co.edu.eci.framework;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * Servidor HTTP con mejoras para manejo de concurrencia y apagado elegante.
 * Esta clase utiliza un pool de hilos para manejar múltiples conexiones
 * simultáneas.
 * 
 * @author Angie Ramos
 * @version 2.0
 */
public class HttpServer {

    private final int port;
    private final Router router;
    private final StaticFileHandler staticFileHandler;
    private ServerSocket serverSocket;
    private ExecutorService threadPool;
    private volatile boolean running = false;
    private Thread serverThread;

    /**
     * Constructor para inicializar el servidor HTTP.
     *
     * @param port              El puerto en el que escuchará el servidor
     * @param router            El router para manejar las rutas
     * @param staticFileHandler El manejador de archivos estáticos
     */
    public HttpServer(int port, Router router, StaticFileHandler staticFileHandler) {
        this.port = port;
        this.router = router;
        this.staticFileHandler = staticFileHandler;
        this.threadPool = Executors.newFixedThreadPool(10);
    }

    /**
     * Inicia el servidor HTTP y comienza a escuchar conexiones.
     *
     * @throws IOException Si no se puede crear el socket del servidor
     */
    public void start() throws IOException {
        serverSocket = new ServerSocket(port);
        running = true;

        serverThread = new Thread(this::acceptConnections);
        serverThread.setName("HTTP-Server-Main");
        serverThread.start();

        System.out.println("Servidor HTTP iniciado en el puerto " + port);
    }

    /**
     * Detiene el servidor HTTP y libera recursos de manera elegante.
     * Este método implementa un apagado elegante esperando a que las tareas
     * actuales terminen.
     */
    public void stop() {
        if (!running) {
            return;
        }

        System.out.println("Iniciando apagado elegante del servidor...");
        running = false;

        try {
            if (serverSocket != null && !serverSocket.isClosed()) {
                serverSocket.close();
            }
        } catch (IOException e) {
            System.err.println("Error al cerrar socket del servidor: " + e.getMessage());
        }

        if (threadPool != null) {
            threadPool.shutdown();
            try {
                if (!threadPool.awaitTermination(10, TimeUnit.SECONDS)) {
                    threadPool.shutdownNow();
                }
            } catch (InterruptedException e) {
                threadPool.shutdownNow();
            }
        }

        System.out.println("Servidor HTTP detenido");
    }

    /**
     * Bucle principal del servidor que acepta conexiones entrantes.
     * Cada conexión se maneja en un hilo separado del pool de hilos.
     */
    private void acceptConnections() {
        while (running) {
            try {
                Socket clientSocket = serverSocket.accept();
                if (running) {
                    threadPool.submit(() -> handleRequest(clientSocket));
                }
            } catch (SocketException e) {
                if (running) {
                    System.err.println("Error de socket al aceptar conexión: " + e.getMessage());
                }
            } catch (IOException e) {
                if (running) {
                    System.err.println("Error al aceptar conexión: " + e.getMessage());
                }
            }
        }
    }

    /**
     * Maneja una petición HTTP individual de un socket cliente.
     *
     * @param clientSocket El socket del cliente
     */
    private void handleRequest(Socket clientSocket) {
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                OutputStream outputStream = clientSocket.getOutputStream()) {

            HttpRequestData requestData = parseHttpRequest(reader);
            if (requestData == null) {
                sendErrorResponse(outputStream, 400, "Bad Request");
                return;
            }

            Request request = new Request(
                    requestData.method,
                    requestData.path,
                    requestData.queryString,
                    requestData.headers);
            Response response = new Response();

            Optional<Route> route = router.findRoute(requestData.method, requestData.path);

            if (route.isPresent()) {
                try {
                    String responseBody = route.get().execute(request, response);
                    sendResponse(outputStream, response, responseBody);
                } catch (Exception e) {
                    System.err.println("Error al ejecutar el manejador de ruta: " + e.getMessage());
                    sendErrorResponse(outputStream, 500, "Internal Server Error");
                }
            } else {
                StaticFileHandler.StaticFileResult fileResult = staticFileHandler.serveStaticFile(requestData.path);

                if (fileResult.isFound()) {
                    sendStaticFileResponse(outputStream, fileResult);
                } else {
                    sendErrorResponse(outputStream, 404, "Not Found");
                }
            }

        } catch (IOException e) {
            if (running) {
                System.err.println("Error al manejar la solicitud: " + e.getMessage());
            }
        } finally {
            try {
                clientSocket.close();
            } catch (IOException e) {
                if (running) {
                    System.err.println("Error al cerrar socket cliente: " + e.getMessage());
                }
            }
        }
    }

    /**
     * Parsea la solicitud HTTP desde el reader.
     *
     * @param reader El BufferedReader de donde leer la solicitud
     * @return HttpRequestData con la información de la solicitud, o null si es
     *         inválida
     * @throws IOException Si hay un error al leer
     */
    private HttpRequestData parseHttpRequest(BufferedReader reader) throws IOException {
        String requestLine = reader.readLine();
        if (requestLine == null || requestLine.isEmpty()) {
            return null;
        }

        String[] requestParts = requestLine.split(" ");
        if (requestParts.length != 3) {
            return null;
        }

        String method = requestParts[0];
        String fullPath = requestParts[1];

        String path;
        String queryString = "";
        if (fullPath.contains("?")) {
            int queryIndex = fullPath.indexOf('?');
            path = fullPath.substring(0, queryIndex);
            queryString = fullPath.substring(queryIndex + 1);
        } else {
            path = fullPath;
        }

        Map<String, String> headers = new HashMap<>();
        String headerLine;
        while ((headerLine = reader.readLine()) != null && !headerLine.isEmpty()) {
            int colonIndex = headerLine.indexOf(':');
            if (colonIndex > 0) {
                String headerName = headerLine.substring(0, colonIndex).trim();
                String headerValue = headerLine.substring(colonIndex + 1).trim();
                headers.put(headerName, headerValue);
            }
        }

        return new HttpRequestData(method, path, queryString, headers);
    }

    /**
     * Envía una respuesta HTTP al cliente.
     *
     * @param outputStream El OutputStream donde escribir la respuesta
     * @param response     El objeto Response con los encabezados y estado
     * @param responseBody El cuerpo de la respuesta
     * @throws IOException Si hay un error al escribir
     */
    private void sendResponse(OutputStream outputStream, Response response, String responseBody) throws IOException {
        StringBuilder headers = new StringBuilder();
        headers.append("HTTP/1.1 ").append(response.getStatusCode()).append(" ")
                .append(getStatusMessage(response.getStatusCode())).append("\r\n");
        headers.append("Content-Type: ").append(response.getContentType()).append("\r\n");

        for (Map.Entry<String, String> header : response.getHeaders().entrySet()) {
            headers.append(header.getKey()).append(": ").append(header.getValue()).append("\r\n");
        }

        byte[] bodyBytes = responseBody.getBytes(StandardCharsets.UTF_8);
        headers.append("Content-Length: ").append(bodyBytes.length).append("\r\n");
        headers.append("Connection: close\r\n");
        headers.append("\r\n");

        outputStream.write(headers.toString().getBytes(StandardCharsets.UTF_8));
        outputStream.write(bodyBytes);
        outputStream.flush();
    }

    /**
     * Envía una respuesta de archivo estático al cliente.
     *
     * @param outputStream El OutputStream donde escribir la respuesta
     * @param fileResult   El resultado del archivo estático
     * @throws IOException Si hay un error al escribir
     */
    private void sendStaticFileResponse(OutputStream outputStream, StaticFileHandler.StaticFileResult fileResult)
            throws IOException {
        StringBuilder headers = new StringBuilder();
        headers.append("HTTP/1.1 200 OK\r\n");
        headers.append("Content-Type: ").append(fileResult.getContentType()).append("\r\n");
        headers.append("Content-Length: ").append(fileResult.getContent().length).append("\r\n");
        headers.append("Connection: close\r\n");
        headers.append("\r\n");

        outputStream.write(headers.toString().getBytes(StandardCharsets.UTF_8));
        outputStream.write(fileResult.getContent());
        outputStream.flush();
    }

    /**
     * Envía una respuesta de error al cliente.
     *
     * @param outputStream El OutputStream donde escribir la respuesta
     * @param statusCode   El código de estado HTTP del error
     * @param message      El mensaje de error
     * @throws IOException Si hay un error al escribir
     */
    private void sendErrorResponse(OutputStream outputStream, int statusCode, String message) throws IOException {
        String statusMessage = getStatusMessage(statusCode);
        String responseBody = "<html><body><h1>" + statusCode + " " + statusMessage + "</h1><p>" + message
                + "</p></body></html>";

        StringBuilder headers = new StringBuilder();
        headers.append("HTTP/1.1 ").append(statusCode).append(" ").append(statusMessage).append("\r\n");
        headers.append("Content-Type: text/html; charset=utf-8\r\n");

        byte[] bodyBytes = responseBody.getBytes(StandardCharsets.UTF_8);
        headers.append("Content-Length: ").append(bodyBytes.length).append("\r\n");
        headers.append("Connection: close\r\n");
        headers.append("\r\n");

        outputStream.write(headers.toString().getBytes(StandardCharsets.UTF_8));
        outputStream.write(bodyBytes);
        outputStream.flush();
    }

    /**
     * Obtiene el mensaje correspondiente a un código de estado HTTP.
     *
     * @param statusCode El código de estado HTTP
     * @return El mensaje correspondiente
     */
    private String getStatusMessage(int statusCode) {
        return switch (statusCode) {
            case 200 -> "OK";
            case 201 -> "Created";
            case 204 -> "No Content";
            case 400 -> "Bad Request";
            case 401 -> "Unauthorized";
            case 403 -> "Forbidden";
            case 404 -> "Not Found";
            case 405 -> "Method Not Allowed";
            case 500 -> "Internal Server Error";
            case 501 -> "Not Implemented";
            case 503 -> "Service Unavailable";
            default -> "Unknown Status";
        };
    }

    /**
     * Clase interna para almacenar datos de la solicitud HTTP.
     */
    private static class HttpRequestData {
        final String method;
        final String path;
        final String queryString;
        final Map<String, String> headers;

        HttpRequestData(String method, String path, String queryString, Map<String, String> headers) {
            this.method = method;
            this.path = path;
            this.queryString = queryString;
            this.headers = headers;
        }
    }
}