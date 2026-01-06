# salesPoint

Proyecto de puntos de ventas en Spring Boot.

## Resumen

Este README recoge la estructura del proyecto, requisitos y los comandos necesarios para compilar, ejecutar, probar y desplegar la aplicación en un entorno Windows (PowerShell). También incluye notas de diagnóstico (por ejemplo: problemas con `springdoc` y `/v3/api-docs`).

## Estructura de carpetas (detallada)

Raíz del proyecto:

- `pom.xml` — configuración Maven y dependencias
- `mvnw`, `mvnw.cmd` — Maven wrapper
- `compose.yaml` — definición opcional de servicios (DB, etc.) para despliegue local con Docker Compose
- `src/`
  - `main/`
    - `java/`
      - `com/ar/sales/point/`
        - `application/`  — casos de uso, servicios (por ejemplo `SalePointCostService`)
          - `service/` (implementaciones de servicios)
          - `port/` (interfaces/puertos de entrada y salida)
        - `domain/`       — modelos de dominio (por ejemplo `SalePoint`, `SalePointCost`)
        - `infrastructure/` — adaptadores técnicos
          - `controller/`  (controladores REST, DTOs)
          - `persistance/` (adaptadores JPA, entidades, repositorios Spring Data)
          - `exception/`   (manejadores globales, excepciones como `ResourceNotFoundException`)
          - `config/`      (configuraciones específicas como OpenAPI)
    - `resources/`
      - `application.properties` (configuración por defecto)
  - `test/`
    - `java/` — tests unitarios y de integración (E2E) organizados por paquete similar a `main`
- `target/` — artefactos generados (jar), informes, y reportes (JaCoCo)


## Requisitos

- JDK 21
- Maven (se recomienda usar el wrapper incluido: `mvnw` / `mvnw.cmd`)
- (Opcional) Docker y Docker Compose para desplegar servicios auxiliares (por ejemplo Postgres)
- Variables de entorno si usas una base de datos externa (Postgres):
  - `SPRING_DATASOURCE_URL` (p.ej. `jdbc:postgresql://localhost:5432/salespoint`)
  - `SPRING_DATASOURCE_USERNAME`
  - `SPRING_DATASOURCE_PASSWORD`

## Compilar y ejecutar (Windows PowerShell)

Abrir PowerShell en la raíz del proyecto (`C:\opt\Spring\salesPoint`) y ejecutar:

- Compilar y ejecutar los tests:

```powershell
.\mvnw.cmd clean package
```

- Compilar sin ejecutar tests (más rápido):

```powershell
.\mvnw.cmd -DskipTests clean package
```

- Ejecutar la aplicación desde el JAR generado:

```powershell
java -jar .\target\sales.point-0.0.1-SNAPSHOT.jar
```

- Ejecutar en modo desarrollo (hot-reload según configuración):

```powershell
.\mvnw.cmd spring-boot:run
```

## Ejecutar tests y ver cobertura

- Ejecutar la suite completa de tests:

```powershell
.\mvnw.cmd test
```

- Generar informe de cobertura JaCoCo (se ejecuta en la fase `verify`):

```powershell
.\mvnw.cmd verify
```

  - El informe HTML queda en `target/site/jacoco/index.html`.
  - También hay un `jacoco.csv` en la misma carpeta con métricas por clase.

- Ejecutar un test concreto (por ejemplo el E2E del controller):

```powershell
.\mvnw.cmd -Dtest=SalePointCostControllerE2eTest test
```

## Variables de entorno y configuración para despliegue

- En producción normalmente usarás una base de datos Postgres. Configura las propiedades en `application.properties` o mediante variables de entorno:

```properties
spring.datasource.url=${SPRING_DATASOURCE_URL}
spring.datasource.username=${SPRING_DATASOURCE_USERNAME}
spring.datasource.password=${SPRING_DATASOURCE_PASSWORD}
```

- Para perfiles (por ejemplo `prod`, `dev`), crea `application-prod.properties`, `application-dev.properties` y arranca la app con `-Dspring.profiles.active=prod` o con la variable `SPRING_PROFILES_ACTIVE`.

## Despliegue con Docker Compose (opcional)

Si quieres levantar servicios auxiliares (por ejemplo Postgres) y la app en contenedores, usa `compose.yaml` (si está presente). Un ejemplo de uso en PowerShell:

```powershell
# Levantar servicios definidos en compose.yaml
docker compose -f compose.yaml up -d

# Parar y eliminar
docker compose -f compose.yaml down
```

Nota: el `compose.yaml` del repo puede requerir ajustes para volúmenes y variables de entorno. Revisa y sustituye credenciales antes de usar en producción.

