# TechStore – Development & Run Guide

## Prerequisitos
- **Java 21** (JDK) y **Maven** (wrapper incluido).
- **Docker** + **docker‑compose** (para PostgreSQL).
- **Keycloak 26.6.1** descargado en `~/Documentos/keycloak-26.6.1` (contiene el script `bin/kc.sh`).

## 1️⃣ Base de datos (PostgreSQL)
```bash
# Desde la raíz del proyecto
docker compose up -d   # levanta el contenedor `postgres` definido en compose.yaml
```
Esto creará la base `techstore` y la tabla `public` con los esquemas generados por Hibernate al iniciar la app.

## 2️⃣ Keycloak (Identity Provider)
### Opción 1 – Manual (recomendado para pruebas rápidas)
```bash
cd ~/Documentos/keycloak-26.6.1
bin/kc.sh start-dev   # arranca Keycloak en modo desarrollo (http://localhost:8080)
```
- **Realm**: `TechStore`
- **Cliente**: `techstore-api`
  - Access Type: **public**
  - Valid Redirect URIs: `http://localhost:8081/*`
- **Roles**: `ROLE_ADMIN`, `ROLE_CUSTOMER`
- **Usuario de prueba** (admin): `admin / admin` (creado al iniciar en modo dev).

### Opción 2 – Script (arranca todo junto)  
Guarda el siguiente script como `start-all.sh` (ver más abajo) y ejecútalo.

## 3️⃣ Spring Boot (API)
```bash
# Desde la raíz del proyecto
./mvnw spring-boot:run   # la app escuchará en http://localhost:8081
```
El `application.properties` está configurado para usar la base de datos del contenedor PostgreSQL y validar los tokens emitidos por Keycloak (ver `spring.security.oauth2.resource-server.jwt.issuer-uri`).

---
## 📜 Script para iniciar todo de una sola vez
Crea el archivo `start-all.sh` en la raíz del proyecto, dale permiso de ejecución (`chmod +x start-all.sh`) y luego ejecútalo:
```bash
./start-all.sh
```
El script hará lo siguiente:
1. Levanta Docker‑Compose (PostgreSQL).
2. Inicia Keycloak en modo dev (background).
3. Espera a que Keycloak esté disponible (máx 30 s).
4. Lanza la API Spring Boot.

---
## 🔧 Tips de depuración
- **Ver logs de Keycloak**: `docker logs -f keycloak` (si lo ejecutas con Docker) o revisa la consola del proceso lanzado con `start-dev`.
- **Ver tablas**: `docker exec -it techstore-postgres psql -U postgres -d techstore -c "\dt"`
- **Re‑iniciar**: `docker compose down && ./start-all.sh`

---
## 📋 Pending Tasks
- [ ] Implement a Global Exception Handler (`com.ecomerce.TechStore.exception`) to handle and format API errors (e.g. resource not found, validation failures) consistently.

---
## ✅ ¡Listo!
Con estos pasos deberías tener la base de datos, el servidor de identidad y la API corriendo en local, listos para usar con tu frontend o herramientas como Postman.
