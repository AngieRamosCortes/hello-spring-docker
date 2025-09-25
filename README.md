# Hello Docker y AWS - Servidor Web

Una pequeña **aplicación web** en contenedor con una implementación personalizada de servidor HTTP que proporciona endpoints REST. El proyecto demuestra la contenedorización con Docker y el despliegue en AWS EC2. La aplicación presenta un framework web ligero desarrollado desde cero que proporciona capacidades de enrutamiento similares a frameworks populares como Express.js o Spring Boot, pero con un enfoque minimalista.

## Descripción del Proyecto

Esta aplicación implementa un **framework web personalizado** con **endpoints REST** (incluyendo `/hello`), **contenedorizada con Docker** y **desplegada en AWS EC2**. 
El objetivo es practicar: creación de proyectos con **Maven**, empaquetado, **construcción de imágenes** en Docker, **publicación** en Docker Hub y **despliegue** en una instancia de **Amazon Linux**.

Autora: **Angie Julieth Ramos Cortes – Ingeniería de Sistemas**
Estado actual con todos los requisitos implementados
<img width="1919" height="505" alt="image" src="https://github.com/user-attachments/assets/8e961219-104a-453f-bdca-670c63a3ccca" />
<img width="1919" height="253" alt="image" src="https://github.com/user-attachments/assets/ea337f6e-5385-4eb1-b992-4ee84d051b51" />
<img width="1918" height="260" alt="image" src="https://github.com/user-attachments/assets/ae3c10ae-8af4-4feb-8583-d539dac73429" />


---

## Comenzando

Estas instrucciones te permitirán obtener una copia del proyecto en funcionamiento en tu máquina local para propósitos de desarrollo y pruebas. Consulta la sección de despliegue para conocer cómo desplegar el proyecto en un sistema en vivo.
<img width="921" height="236" alt="image" src="https://github.com/user-attachments/assets/1bf94b45-859a-41b0-b2d0-48c2b9e47ea7" />
<img width="921" height="210" alt="image" src="https://github.com/user-attachments/assets/07ecfec6-fa3b-4e31-a39a-f9ac5cbc77d1" />
<img width="921" height="92" alt="image" src="https://github.com/user-attachments/assets/7eb21ed0-f130-4cfb-a463-979e8ac46e7b" />

### Endpoints

| Método | Ruta        | Descripción                          |
|--------|-------------|--------------------------------------|
| GET    | `/hello`    | Saludo simple. Soporta parámetros de consulta |
| GET    | `/greeting` | Saludo personalizado con parámetro de nombre |

---

## Prerrequisitos

Para construir y ejecutar este proyecto, necesitarás:

- **Java 17+**
- **Maven 3.8+**
- **Docker** y **Docker Desktop** (o engine)
- Cuenta de **Docker Hub** para publicar la imagen
- (Para despliegue) Cuenta de AWS con acceso a EC2

### Verificación Rápida

```sh
java -version
mvn -v
docker --version
```

---

## Instalación

### 1) Clonar y construir el proyecto

```sh
git clone https://github.com/AngieRamosCortes/hello-spring-docker.git
cd hello-spring-docker-main
mvn clean package -DskipTests
```

### 2) Ejecutar en local (sin Docker)

#### Windows (PowerShell/CMD)

```sh
java -cp "target/classes;target/dependency/*" co.edu.eci.hello.RestServiceApplication
```
ó
```sh
mvn clean compile exec:java "-Dexec.mainClass=co.edu.eci.hello.RestServiceApplication"
```

#### macOS/Linux

```sh
java -cp "target/classes:target/dependency/*" co.edu.eci.hello.RestServiceApplication
```

Prueba en tu navegador:

- [http://localhost:4567/hello](http://localhost:4567/hello)
<img width="921" height="110" alt="image" src="https://github.com/user-attachments/assets/fa07238e-5a15-4ccb-8568-be70f69416b7" />


## Despliegue

### Construcción de la imagen Docker

#### 1) Usando el Dockerfile

```dockerfile
FROM openjdk:17

WORKDIR /usrapp/bin
ENV PORT 6000

COPY target/classes     /usrapp/bin/classes
COPY target/dependency  /usrapp/bin/dependency

CMD ["java","-cp","./classes:./dependency/*","co.edu.eci.hello.RestServiceApplication"]
```

#### 2) Construir la imagen

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


#### 3) Ejecutar el contenedor

El contenedor escucha en el puerto `6000` (definido por `ENV PORT 6000`).

```sh
docker build -t hello-spring-docker:latest .
docker run -d -p 34000:6000 --name hello-container hello-spring-docker:latest
docker ps
```

Prueba:

- [http://localhost:34001/hello](http://localhost:34000/hello)
- [http://localhost:34002/hello](http://localhost:34001/hello)

  <img width="921" height="94" alt="image" src="https://github.com/user-attachments/assets/b8aa856b-64c6-4f20-8411-2bc59333adb4" />
  <img width="921" height="92" alt="image" src="https://github.com/user-attachments/assets/f344ebdc-1438-4f53-ac7e-369fe7684c4a" />
  <img width="921" height="213" alt="image" src="https://github.com/user-attachments/assets/82ab4c68-cd1b-448b-b7e8-f7f30ba0863b" />


---

### Publicación en Docker Hub

```sh
docker login
docker tag hello-spring-docker:latest angieramoscortes/hello-spring-docker:latest
docker push angieramoscortes/hello-spring-docker:latest
```

---

### Despliegue en AWS EC2
<img width="921" height="306" alt="image" src="https://github.com/user-attachments/assets/de2d048c-157d-4e52-9895-d76c2fbd290f" />
<img width="921" height="227" alt="image" src="https://github.com/user-attachments/assets/10942e93-8838-42a9-8f19-68bebaac7312" />
<img width="921" height="309" alt="image" src="https://github.com/user-attachments/assets/61d5f558-5b6f-4f1e-8b5a-eba36fb55cb5" />
<img width="921" height="109" alt="image" src="https://github.com/user-attachments/assets/a6f5effe-f35b-472b-813a-3f0fe7f972e3" />
<img width="921" height="135" alt="image" src="https://github.com/user-attachments/assets/44536d9a-8938-4737-94e2-c522ee8d14d4" />
<img width="921" height="298" alt="image" src="https://github.com/user-attachments/assets/b615a50b-cce5-499f-b657-da3e76ec9955" />
<img width="921" height="81" alt="image" src="https://github.com/user-attachments/assets/94b729e3-5a08-48fe-8d60-46def10b3c5c" />










#### 1) Lanzar una instancia EC2

Lanza una instancia Amazon Linux 2023 (x86_64) t3.micro con los siguientes ajustes del grupo de seguridad:

- Puerto TCP 22 desde tu IP (para acceso SSH)
- Puerto TCP 42000 desde 0.0.0.0/0 (o tu IP para mayor seguridad)

#### 2) Conectarse a la instancia

Usa cualquiera de estas opciones:
- EC2 Instance Connect (recomendado para entornos académicos), o
- `ssh -i AWSLab.pem ec2-user@ec2-13-217-225-133.compute-1.amazonaws.com`

#### 3) Instalar y iniciar Docker en la instancia

```sh      
sudo dnf update -y
sudo dnf install -y docker
sudo systemctl enable --now docker
```

#### 4) Ejecutar la imagen Docker publicada

```sh
sudo docker pull angieramoscortes/hello-spring-docker:latest
sudo docker run -d -p 42000:6000 --name firstdockerimageaws angieramoscortes/hello-spring-docker:latest
sudo docker ps
```

#### 5) Probar desde tu navegador

- `http://ec2-13-217-225-133.compute-1.amazonaws.com:42000/hello`
- `http://ec2-13-217-225-133.compute-1.amazonaws.com:42000/hello?name=Angie`

---

## Arquitectura

Este proyecto implementa un framework web personalizado y ligero con los siguientes componentes:

### Estructura de Clases

1. **WebFramework**: Una clase fachada que proporciona métodos estáticos para el registro de rutas y la gestión del servidor.
2. **HttpServer**: Maneja conexiones HTTP con un pool de hilos para peticiones concurrentes.
3. **Router**: Gestiona el registro y emparejamiento de rutas para diferentes métodos HTTP.
4. **Request/Response**: Clases que encapsulan los datos de solicitud y respuesta HTTP.
5. **RouteHandler**: Una interfaz funcional para los manejadores de rutas.
6. **StaticFileHandler**: Sirve archivos estáticos desde los recursos del classpath.
7. **RestServiceApplication**: Clase principal de la aplicación que configura las rutas e inicia el servidor.

### Patrones de Diseño Utilizados

- **Patrón Fachada**: WebFramework proporciona una interfaz simplificada para los componentes subyacentes del framework.
- **Patrón Estrategia**: RouteHandler permite diferentes implementaciones de lógica de manejo de solicitudes.
- **Patrón Comando**: Route encapsula una solicitud como un objeto.

### Diagrama de Arquitectura

```
┌──────────────────┐      ┌─────────────┐      ┌────────────────┐
│ RestApplication  │ usa  │ WebFramework│ usa  │   HttpServer   │
│  (Clase Principal)─────▶│   (Fachada) │─────▶│(Manejador HTTP)│
└──────────────────┘      └─────────────┘      └────────────────┘
                               │                       │
                               │                       │
                               ▼                       ▼
                          ┌─────────┐           ┌────────────┐
                          │  Router │◀─────────▶│PoolDeHilos│
                          │         │           │            │
                          └─────────┘           └────────────┘
                               │
                               │
            ┌─────────────────┬┴─────────────────┐
            │                 │                  │
            ▼                 ▼                  ▼
      ┌──────────┐     ┌────────────┐    ┌───────────────┐
      │ Request  │     │  Response  │    │StaticFileHndlr│
      └──────────┘     └────────────┘    └───────────────┘
```

## Solución de problemas

- Si la página no carga: comprueba el Grupo de Seguridad (42000/TCP) y verifica que el contenedor esté en ejecución (`docker ps`).
- Si no puedes hacer SSH: usa EC2 Instance Connect o verifica que tu red no bloquee el puerto 22 saliente.

---

## Construido Con

* **Java 17** - Lenguaje de programación
* **Maven** - Gestión de dependencias y herramienta de construcción
* **Docker / Docker Compose** - Contenedorización
* **Docker Hub** - Registro de contenedores
* **AWS EC2** - Despliegue en la nube

---

## Autores

* **Angie Julieth Ramos Cortes** - *Desarrollo y despliegue*

---

## Licencia

Este proyecto está licenciado bajo la Licencia MIT - ver el archivo LICENSE.md para más detalles

---

## Agradecimientos

* Materiales del taller de virtualización con Docker y AWS
* Documentación oficial: Docker, AWS EC2
