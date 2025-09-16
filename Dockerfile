# Imagen base con Java 17 (moderna y segura)
FROM openjdk:17

# Directorio de trabajo
WORKDIR /usrapp/bin

# Puerto interno del contenedor (la app lo leerá desde $PORT)
ENV PORT=6000

# Copiar clases compiladas y dependencias
COPY target/classes /usrapp/bin/classes
COPY target/dependency /usrapp/bin/dependency

# Comando para ejecutar la aplicación (classpath de clases + dependencias)
CMD ["java","-cp","./classes:./dependency/*","co.edu.eci.hello.RestServiceApplication"]
