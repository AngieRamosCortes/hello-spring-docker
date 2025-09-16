# Hello Spring + Docker + AWS (AREP)

## Descripción del proyecto

Pequeña **aplicación web en Spring Boot** (endpoint `/greeting`) **containerizada con Docker** y **desplegada en AWS EC2**.  
El objetivo es practicar: creación del proyecto con **Maven**, empaquetado, **image build** en Docker, **push** a Docker Hub y **deploy** en una instancia de **Amazon Linux**.

Autora: **Angie Julieth Ramos Cortes – Ingeniería de Sistemas**

---

## Getting Started

Estas instrucciones te permiten **clonar**, **compilar** y **ejecutar** el proyecto en local, y después **empaquetarlo en Docker**. Más abajo encontrarás las notas de **despliegue en producción** (AWS EC2).

### Endpoints

| Método | Ruta        | Descripción                          |
|--------|-------------|--------------------------------------|
| GET    | `/greeting` | Saludo. Soporta `?name=<tu_nombre>`  |

---

## Prerequisitos

- **Java 17+**
- **Maven 3.8+**
- **Docker** y **Docker Desktop** (o engine)
- (Opcional) **Docker Hub** para publicar la imagen
- (Para despliegue) Cuenta/entorno de AWS con acceso a EC2

### Verificación rápida

```sh
java -version
mvn -v
docker --version
```

---

## Installing (local)

### 1) Clonar y compilar

```sh
git clone <URL-DE-TU-REPO> hello-spring-docker
cd hello-spring-docker
mvn clean package -DskipTests
```

### 2) Ejecutar en local (sin Docker)

La app toma el puerto de la variable `PORT`. Si no existe, usa `5000`.

#### Windows (PowerShell/CMD)

```sh
java -cp "target/classes;target/dependency/*" co.edu.eci.hello.RestServiceApplication
```

#### macOS/Linux

```sh
java -cp "target/classes:target/dependency/*" co.edu.eci.hello.RestServiceApplication
```

Prueba en tu navegador:

- [http://localhost:5000/greeting](http://localhost:5000/greeting)
- [http://localhost:5000/greeting?name=Angie](http://localhost:5000/greeting?name=Angie)

---

## Empaquetado en Docker

### 1) Dockerfile (en la raíz del proyecto)

```dockerfile
FROM openjdk:17

WORKDIR /usrapp/bin
ENV PORT 6000

COPY target/classes     /usrapp/bin/classes
COPY target/dependency  /usrapp/bin/dependency

# Nota: el main de ejemplo
CMD ["java","-cp","./classes:./dependency/*","co.edu.eci.hello.RestServiceApplication"]
```

### 2) Build de la imagen

```sh
mvn clean package -DskipTests
docker build -t hello-spring-docker:latest .
docker images
```

### 3) Correr el contenedor

El contenedor escucha en `6000` (por `ENV PORT 6000`).

```sh
docker run -d -p 34000:6000 --name hello-container hello-spring-docker:latest
docker ps
```

Prueba:

- [http://localhost:34000/greeting](http://localhost:34000/greeting)
- [http://localhost:34000/greeting?name=Angie](http://localhost:34000/greeting?name=Angie)

---

## Publicar en Docker Hub (opcional, recomendado)

```sh
docker login
docker tag hello-spring-docker:latest angieramoscortes/hello-spring-docker:latest
docker push angieramoscortes/hello-spring-docker:latest
```

---

## Deployment (AWS EC2)

### 0) Preparación

Lanza una EC2 Amazon Linux 2023 (x86_64) t3.micro.

**Security Group (Inbound rules):**

- TCP 22 desde My IP (si usarás SSH).
- TCP 42000 desde 0.0.0.0/0 (o tu IP para mayor restricción).

### 1) Conexión

- EC2 Instance Connect (recomendado en entornos académicos), o
- `ssh -i <tu-llave>.pem ec2-user@<EC2_PUBLIC_DNS>`

### 2) Instalar y arrancar Docker en la instancia

```sh
sudo dnf update -y
sudo dnf install -y docker
sudo systemctl enable --now docker
```

### 3) Ejecutar la imagen publicada

```sh
sudo docker pull angieramoscortes/hello-spring-docker:latest
sudo docker run -d -p 42000:6000 --name firstdockerimageaws angieramoscortes/hello-spring-docker:latest
sudo docker ps
```

### 4) Probar desde tu navegador

- `http://<EC2_PUBLIC_DNS>:42000/greeting`
- `http://<EC2_PUBLIC_DNS>:42000/greeting?name=Angie`

---

## Troubleshooting rápido

- Si la página no carga: revisa el Security Group (42000/TCP) y que el contenedor esté UP (`docker ps`).
- Si no puedes hacer SSH: usa EC2 Instance Connect o verifica que tu red no bloquee el puerto 22 saliente.

---

## Running the tests

Este proyecto de taller no incluye tests automatizados. Para extenderlo:

- **Unit tests (Spring Boot Test):** crear pruebas de los controladores con `@WebMvcTest`.
- **End-to-end:** Testcontainers + RestTemplate/WebTestClient para validar la imagen en un contenedor.

Ejemplo (idea rápida):

```java
// @WebMvcTest(HelloRestController.class)
// MockMvc.perform(get("/greeting?name=Angie")).andExpect(status().isOk())
```

---

## Built With

- **Spring Boot** – Framework web
- **Maven** – Gestión de dependencias y build
- **Docker / Docker Compose** – Contenerización
- **Docker Hub** – Registro de imágenes
- **AWS EC2** – Despliegue en la nube

---

## Contributing

Sugerencias y PRs son bienvenidos.  
Convención de ramas: `feature/*`, `fix/*`. Abre issues con descripción, pasos para reproducir y capturas si aplica.

---

## Versioning

Se recomienda **SemVer**.  
Usa tags en Git (`v1.0.0`, `v1.1.0`, …) y etiquetas (`:v1.0.0`) en Docker Hub.

---

## Authors

**Angie Julieth Ramos Cortes** – Desarrollo y despliegue

---

## License

Este proyecto puede publicarse bajo **MIT**.  
Incluye un archivo LICENSE con el texto de la licencia si decides aplicarla.

---

## Acknowledgments

- Material del taller de virtualización con Docker y AWS
- Profes y compañeros por las pruebas y feedback
- Documentación oficial: Spring Boot, Docker, AWS EC2