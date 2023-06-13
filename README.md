
# Proyecto Devops de la Kibernum Academy

Este proyecto es una aplicación web de Spring Boot para la gestión de estudiantes.

## Descripción

El proyecto implementa un sistema de gestión de estudiantes que permite las operaciones de creación, lectura, actualización y eliminación (CRUD) de los registros de estudiantes. 

Los estudiantes tienen los siguientes campos:

- ID (generado automáticamente)
- Nombre
- Apellido
- Email (único)

## Instalación

Para instalar y ejecutar este proyecto necesitarás tener instalado:

- Java 11
- Maven

Primero, clona el repositorio:

```
git clone https://github.com/yourusername/devops.git
```

Entra al directorio del proyecto:

```
cd devops
```

Para compilar el proyecto y ejecutar las pruebas, ejecuta:

```
mvn clean install
```

Para ejecutar el proyecto:

```
mvn spring-boot:run
```

## Uso

Abre tu navegador y visita `http://localhost:9090` para comenzar a usar la aplicación.


## Ejecución de pruebas

Este proyecto incluye pruebas unitarias y de integración, escritas con JUnit y Mockito.

Para ejecutar las pruebas, necesitas tener Maven instalado en tu máquina. Una vez que lo tengas, puedes usar los siguientes comandos para ejecutar las pruebas:

Para todas las pruebas:

```
mvn test
```

Para pruebas específicas:

Para usuarios de Unix, Linux, macOS:

```
mvn -Dtest=NombreDeLaPrueba test
```

Para usuarios de PowerShell en Windows:

```powershell
mvn test "-Dtest=NombreDeLaPrueba"
```

Reemplaza "NombreDeLaPrueba" con el nombre de la prueba que deseas ejecutar.

Por ejemplo, para ejecutar las pruebas de `StudentTest` y `StudentServiceImplTest` en PowerShell, debes usar el comando:

```powershell
mvn test "-Dtest=StudentTest,StudentServiceImplTest"
```


## Configuración

El archivo `application.properties` se encuentra en `src/main/resources`. Las siguientes son algunas de las propiedades configuradas:

```properties
spring.h2.console.enabled=true
server.port=9090
spring.security.user.name=userdevops
spring.security.user.password=devops
```

Los detalles de la base de datos y Hibernate están comentados. Descomenta y actualiza estos valores según tus necesidades.


## Contribuir

Cualquier persona que desee contribuir a este proyecto es bienvenida. Las contribuciones no se limitan a la programación. Cualquier ayuda con la documentación, la identificación de errores, las sugerencias o las mejoras en el diseño son igualmente apreciadas. Si deseas contribuir a la codificación, puedes hacer un fork del proyecto y enviar una pull request. No olvides incluir pruebas para tu código.


## Licencia

Este proyecto es de código abierto bajo la licencia [MIT](https://opensource.org/licenses/MIT).

