package co.edu.eci.hello;

/**
 * Controlador para endpoint de saludo.
 * Nota: Esta clase es solo para referencia. La implementación real del endpoint
 * ahora está en RestServiceApplication usando el microframework.
 * 
 * @author Angie Ramos
 * @version 2.0
 */
public class HelloRestController {

    /**
     * Método de saludo simple que devuelve un mensaje.
     * 
     * @return Mensaje de saludo "Hello Docker!"
     */
    public String hello() {
        return "Hello Docker!";
    }
}
