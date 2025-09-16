# Hello Spring + Docker + AWS (AREP)

## Descripción del proyecto

Pequeña **aplicación web en Spring Boot** (endpoint `/greeting`) **containerizada con Docker** y **desplegada en AWS EC2**.  
El objetivo es practicar: creación del proyecto con **Maven**, empaquetado, **image build** en Docker, **push** a Docker Hub y **deploy** en una instancia de **Amazon Linux**.

Autora: **Angie Julieth Ramos Cortes – Ingeniería de Sistemas**

---

## Getting Started

Estas instrucciones te permiten **clonar**, **compilar** y **ejecutar** el proyecto en local, y después **empaquetarlo en Docker**. Más abajo encontrarás las notas de **despliegue en producción** (AWS EC2).
<img width="921" height="236" alt="image" src="https://github.com/user-attachments/assets/1bf94b45-859a-41b0-b2d0-48c2b9e47ea7" />
<img width="921" height="210" alt="image" src="https://github.com/user-attachments/assets/07ecfec6-fa3b-4e31-a39a-f9ac5cbc77d1" />
<img width="921" height="92" alt="image" src="https://github.com/user-attachments/assets/7eb21ed0-f130-4cfb-a463-979e8ac46e7b" />





### Endpoints

| Método | Ruta        | Descripción                          |
|--------|-------------|--------------------------------------|
| GET    | `/greeting` | Saludo. Soporta Angie  |

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
git clone https://github.com/AngieRamosCortes/hello-spring-docker.git
cd hello-spring-docker
mvn clean package -DskipTests
```

### 2) Ejecutar en local (sin Docker)

#### Windows (PowerShell/CMD)

```sh
java -cp "target/classes;target/dependency/*" co.edu.eci.hello.RestServiceApplication
```

#### macOS/Linux

```sh
java -cp "target/classes:target/dependency/*" co.edu.eci.hello.RestServiceApplication
```

Prueba en tu navegador:

- [http://localhost:4567/hello](http://localhost:4567/hello)
<img width="921" height="110" alt="image" src="https://github.com/user-attachments/assets/fa07238e-5a15-4ccb-8568-be70f69416b7" />

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
<img width="921" height="571" alt="image" src="https://github.com/user-attachments/assets/71bde12e-d736-4a70-aaff-ad43449d42f8" />
<img width="921" height="108" alt="image" src="https://github.com/user-attachments/assets/d6b4adea-7a24-4975-aa13-2b8ac99ea652" />
<img width="921" height="92" alt="image" src="https://github.com/user-attachments/assets/03602dfc-f6ad-44be-af92-e7b032a32adb" />
<img width="921" height="483" alt="image" src="https://github.com/user-attachments/assets/cf4ff879-c34c-4e98-9f7d-ea56c698e5a3" />
<img width="921" height="391" alt="image" src="https://github.com/user-attachments/assets/87742a9c-77e2-4175-835a-96b0d9eb122e" />
<img width="921" height="376" alt="image" src="https://github.com/user-attachments/assets/42061d0d-7d89-43dc-8271-23e27c89c541" />
<img width="921" height="310" alt="image" src="https://github.com/user-attachments/assets/6b7a778f-3a1a-4424-b014-46adc9f7299d" />
<img width="921" height="213" alt="image" src="https://github.com/user-attachments/assets/185855af-1ba6-4bfd-a9d8-27aeb1205d90" />
<img width="921" height="306" alt="image" src="https://github.com/user-attachments/assets/5d4ba002-d952-4c60-b5f8-cfa2d46325a1" />


### 3) Correr el contenedor

El contenedor escucha en `6000` (por `ENV PORT 6000`).

```sh
docker run -d -p 34000:6000 --name hello-container hello-spring-docker:latest
docker ps
```

Prueba:

- [http://localhost:34000/hello](http://localhost:34000/hello)
- [http://localhost:34001/hello](http://localhost:34001/hello)

  <img width="921" height="94" alt="image" src="https://github.com/user-attachments/assets/b8aa856b-64c6-4f20-8411-2bc59333adb4" />
  <img width="921" height="92" alt="image" src="https://github.com/user-attachments/assets/f344ebdc-1438-4f53-ac7e-369fe7684c4a" />
  <img width="921" height="213" alt="image" src="https://github.com/user-attachments/assets/82ab4c68-cd1b-448b-b7e8-f7f30ba0863b" />


---

## Publicar en Docker Hub (opcional, recomendado)

```sh
docker login
docker tag hello-spring-docker:latest angieramoscortes/hello-spring-docker:latest
docker push angieramoscortes/hello-spring-docker:latest
```

---

## Deployment (AWS EC2)
<img width="921" height="306" alt="image" src="https://github.com/user-attachments/assets/de2d048c-157d-4e52-9895-d76c2fbd290f" />
<img width="921" height="227" alt="image" src="https://github.com/user-attachments/assets/10942e93-8838-42a9-8f19-68bebaac7312" />
<img width="921" height="309" alt="image" src="https://github.com/user-attachments/assets/61d5f558-5b6f-4f1e-8b5a-eba36fb55cb5" />
<img width="921" height="109" alt="image" src="https://github.com/user-attachments/assets/a6f5effe-f35b-472b-813a-3f0fe7f972e3" />
<img width="921" height="135" alt="image" src="https://github.com/user-attachments/assets/44536d9a-8938-4737-94e2-c522ee8d14d4" />
<img width="921" height="298" alt="image" src="https://github.com/user-attachments/assets/b615a50b-cce5-499f-b657-da3e76ec9955" />
<img width="921" height="81" alt="image" src="https://github.com/user-attachments/assets/94b729e3-5a08-48fe-8d60-46def10b3c5c" />
![Uploading image.png…]()









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
